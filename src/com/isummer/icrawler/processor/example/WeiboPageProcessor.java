package com.isummer.icrawler.processor.example;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.isummer.icrawler.Crawler;
import com.isummer.icrawler.Page;
import com.isummer.icrawler.Site;
import com.isummer.icrawler.connection.UserAgent;
import com.isummer.icrawler.pipeline.SplitWordPipeline;
import com.isummer.icrawler.processor.PageProcessor;
import com.isummer.icrawler.util.WeiboAPIv3;

public class WeiboPageProcessor implements PageProcessor {

	private Site site;

	public WeiboPageProcessor(Site site) {
		this.site = site;
	}

	@Override
	public Set<String> linkFilter(Page page) {
		Set<String> dataSet = new HashSet<String>();
		Elements links = page.getDocument().select("a[href]");
		for (Element link : links) {
			if (link.attr("abs:href").startsWith("http://weibo.cn/comment")
					|| link.attr("abs:href").startsWith(
							"http://weibo.cn/repost")) {
				dataSet.add(link.attr("abs:href"));
			} else if (link.text().startsWith("@")) {
				dataSet.add(link.attr("abs:href"));
			}
		}

		return dataSet;
	}

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

	}

	@Override
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public static void main(String[] args) {

		try {
			String cookie = WeiboAPIv3.getSinaCookie("425664759@qq.com",
					"52156i7Q");

			Site site = Site.me().setUserAgent(UserAgent.Mozilla)
					.addCookie("Cookie", cookie)
					.setCharset(Site.DEFAULT_CHARSET)
					.setTimeOut(Site.DEFAULT_TIMEOUT);

			Crawler crawler = Crawler.create(new WeiboPageProcessor(site)).addPipelile(new SplitWordPipeline());
			crawler.threads(3);

			for (int i = 1; i <= 1; i++) {
				//crawler.addSeed("http://weibo.cn/liuchao1990?vt=4&page=" + i);
				crawler.addSeed("http://weibo.cn/zhyj?vt=4&page=" + i);
			}

			crawler.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
