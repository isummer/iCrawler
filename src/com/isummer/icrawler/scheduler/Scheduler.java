package com.isummer.icrawler.scheduler;

import java.util.HashSet;
import java.util.Set;


public class Scheduler {
	
	private Set<String> visitedUrlList = new HashSet<String>();
	
	private Queue unVisitedUrlList = new Queue();
	
	public Queue getUnVisitedUrlList() {
		return unVisitedUrlList;
	}
	
	public synchronized void addVisitedUrl(String url) {
		visitedUrlList.add(url);
	}
	
	public void removeVisitedUrl(String url) {
		visitedUrlList.remove(url);
	}
	
	public Object unVisitedUrlListDequeue() {
		return unVisitedUrlList.deQueue();
	}
	
	public synchronized void addUnVisitedUrl(String url) {
		if(url != null && !url.trim().equals("")
				&& !visitedUrlList.contains(url)
				&& !unVisitedUrlList.contains(url))
			
			unVisitedUrlList.enQueue(url);
	}
	
	public int getVisitedUrlsCount() {
		return visitedUrlList.size();
	}
	
	public boolean unVisitedUrlListIsEmpty() {
		return unVisitedUrlList.isEmpty();
	}
	
}
