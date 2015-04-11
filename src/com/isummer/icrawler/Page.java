package com.isummer.icrawler;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Page {

	private String url;
	
	private Document document;
	
	private Set<String> linkSet = new HashSet<String>();
	
	private String json;
	
	public Page(String url, Document document) {
		this.url = url;
		this.document = document;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Document getDocument() {
		return document;
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}

	public Set<String> getLinks() {
		Elements links = document.select("a[href]");
		for (Element link : links) {
			linkSet.add(link.attr("abs:href"));
		}
		return linkSet;
	}

	public String getHtml() {
		return document.html();
	}

	public String getJson() {
		return json;
	}
	
}
