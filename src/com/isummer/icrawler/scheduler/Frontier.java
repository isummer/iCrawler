package com.isummer.icrawler.scheduler;

public interface Frontier {
	
	public CrawUrl getNext() throws Exception;
	public boolean putUrl(CrawUrl url) throws Exception;

}
