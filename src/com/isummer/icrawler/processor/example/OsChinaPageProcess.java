package com.isummer.icrawler.processor.example;

import java.util.HashSet;
import java.util.Set;

import com.isummer.icrawler.Page;
import com.isummer.icrawler.Site;
import com.isummer.icrawler.Crawler;
import com.isummer.icrawler.connection.UserAgent;
import com.isummer.icrawler.pipeline.SplitWordPipeline;
import com.isummer.icrawler.processor.PageProcessor;

public class OsChinaPageProcess implements PageProcessor {

	private Site site = Site.me().setUserAgent(UserAgent.Mozilla)
			.setCharset(Site.DEFAULT_CHARSET).setTimeOut(Site.DEFAULT_TIMEOUT);

	@Override
	public void process(Page page) {
		//System.out.println("***-" + page.getUrl() + "-***");

	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public Set<String> linkFilter(Page page) {
		Set<String> dataSet = new HashSet<String>();
		for (String link : page.getLinks()) {
			if (link.startsWith("http://www.oschina.net")) {
				dataSet.add(link);
			}
		}

		return dataSet;
	}
	
	

	public static void main(String[] args) {

		Crawler.create(new OsChinaPageProcess()).addPipelile(new SplitWordPipeline())
				.addSeed("http://www.oschina.net/").threads(3).start();
	}
}
