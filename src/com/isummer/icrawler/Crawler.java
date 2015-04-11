package com.isummer.icrawler;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;

import com.isummer.icrawler.connection.Connection;
import com.isummer.icrawler.downloader.DefaultDownloader;
import com.isummer.icrawler.downloader.Downloader;
import com.isummer.icrawler.pipeline.ConsolePipeline;
import com.isummer.icrawler.pipeline.Pipeline;
import com.isummer.icrawler.processor.PageProcessor;
import com.isummer.icrawler.processor.threadpool.CountableThreadPool;
import com.isummer.icrawler.scheduler.Scheduler;

public class Crawler {

	public final static int MAX_HANDLE_TASK_COUNT = 50;
	private final static int DEFAULT_SLEEP_TIME = 30000;

	private CountableThreadPool executor;

	private ExecutorService executorService;

	private PageProcessor processor;

	private Downloader downloader;

	protected List<Pipeline> pipelines = new ArrayList<Pipeline>();

	private Scheduler scheduler = new Scheduler();

	private int threads = 1;

	private ReentrantLock newUrlLock = new ReentrantLock();

	private Condition newUrlCondition = newUrlLock.newCondition();

	private List<SpiderListener> spiderListeners;

	private Crawler(PageProcessor processor) {
		this.processor = processor;
	}

	public static Crawler create(PageProcessor processor) {
		Crawler crawler = new Crawler(processor);
		return crawler;
	}
	
	public static Connection connect(String url, Site site) {
		Connection conn = new Connection();
		conn.setTargetUrl(url);
		if(site != null) {
			if (site.getHeaders().size() > 0) {
				for (String key : site.getHeaders().keySet()) {
					conn.header(key, site.getHeaders().get(key));
				}
			}
			if (site.getUserAgent() != null) {
				conn.header("User-Agent", site.getUserAgent().getValue());
			}
			if (site.getCookies().size() > 0) {
				conn.cookies(site.getCookies());
			}
			if (site.getTimeOut() != 0) {
				conn.timeout(site.getTimeOut());
			}
		}
		
		return conn;
	}
	
	public static Connection connect(String url) {
		return connect(url, null);
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public PageProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(PageProcessor processor) {
		this.processor = processor;
	}
	
	public Crawler addPipelile(Pipeline pipeline) {
		pipelines.add(pipeline);
		return this;
	}

	public synchronized Crawler addSeed(String seed) {
		scheduler.addUnVisitedUrl(seed);
		return this;
	}

	public Crawler addSeeds(List<String> seeds) {
		for (String seed : seeds) {
			addSeed(seed);
		}
		signalNewUrl();
		return this;
	}

	public int getThreads() {
		return threads;
	}

	public Crawler threads(int threads) {
		this.threads = threads;
		return this;
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public Crawler setDownloader(Downloader downloader) {
		this.downloader = downloader;
		return this;
	}

	protected void initComponent() {
		if (downloader == null) {
			this.downloader = new DefaultDownloader();
		}
		if (pipelines.isEmpty()) {
			pipelines.add(new ConsolePipeline());
		}
		if (executor == null || executor.isShutdown()) {
			if (executorService != null && !executorService.isShutdown()) {
				executor = new CountableThreadPool(threads, executorService);
			} else {
				executor = new CountableThreadPool(threads);
			}
		}
	}

	public void start() {

		initComponent();
		
		System.out.println("crawling start ...");

		while (!scheduler.unVisitedUrlListIsEmpty()
				&& scheduler.getVisitedUrlsCount() <= 1000) {
			String visitUrl = (String) scheduler.unVisitedUrlListDequeue();
			System.out.println(visitUrl);
			Spider spider = new Spider(visitUrl);
			executor.execute(spider);
			waitNewUrl();
		}
	}

	private void waitNewUrl() {
		newUrlLock.lock();
		try {
			// double check
			if (executor.getThreadAlive() == 0) {
				return;
			}
			newUrlCondition.await(DEFAULT_SLEEP_TIME, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			newUrlLock.unlock();
		}
	}

	private void signalNewUrl() {
		try {
			newUrlLock.lock();
			newUrlCondition.signalAll();
		} finally {
			newUrlLock.unlock();
		}
	}

	public void close() {
		destroyEach(downloader);
		destroyEach(processor);
		for (Pipeline pipeline : pipelines) {
			destroyEach(pipeline);
		}
		executor.shutdown();
	}

	private void destroyEach(Object object) {
		if (object instanceof Closeable) {
			try {
				((Closeable) object).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class Spider extends Thread {

		private String url;
		private Task task;
		private Page result;

		public Spider(String url) {
			this.url = url;
			task = new Task() {

				@Override
				public Site getSite() {
					return processor.getSite();
				}
			};
		}

		@Override
		public void run() {
			try {
				result = processRequest(url, task);
				onSuccess(result);
			} catch (Exception e) {
				result = new Page(url, null);
				onError(result);
			} finally {
				signalNewUrl();
			}
		}
	}

	private Page processRequest(String url, Task task) {

		Page page = downloader.download(url, task);
		if (page == null) {
			sleep(task.getSite().getSleepTime());
			onError(page);
			return null;
		}
		scheduler.addVisitedUrl(page.getUrl());
		processor.process(page);
		Set<String> links = processor.linkFilter(page);
		for (String link : links) {
			scheduler.addUnVisitedUrl(link);
		}
		for (Pipeline pipeline : pipelines) {
			pipeline.process(page, task);
		}
		sleep(task.getSite().getSleepTime());
		return page;
	}

	private void onSuccess(Page result) {
		if (CollectionUtils.isNotEmpty(spiderListeners)) {
			for (SpiderListener spiderListener : spiderListeners) {
				spiderListener.onSuccess(result);
			}
		}
	}

	private void onError(Page result) {
		if (CollectionUtils.isNotEmpty(spiderListeners)) {
			for (SpiderListener spiderListener : spiderListeners) {
				spiderListener.onError(result);
			}
		}
	}

	private void sleep(int sleepTime) {
		while (sleepTime-- > 0) {};
	}
}
