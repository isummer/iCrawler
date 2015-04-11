package com.isummer.icrawler.util;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlUtils {

	public static Set<String> extractLinks(Document doc) {
		Set<String> dataSet = new HashSet<String>();
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			dataSet.add(link.attr("abs:href"));
		}

		return dataSet;
	}
}
