package com.isummer.icrawler.processor;

import java.util.Set;

import com.isummer.icrawler.Page;
import com.isummer.icrawler.Site;

public interface PageProcessor {
	
	public abstract Set<String> linkFilter(Page page);

    public void process(Page page);

    public Site getSite();
}
