package com.isummer.icrawler.scheduler;
import java.util.LinkedList;


public class Queue {
	
	private LinkedList<Object> queue = new LinkedList<Object>();
	
	public void enQueue(Object o) {
		queue.addLast(o);
	}
	
	public Object deQueue() {
		return queue.removeFirst();
	}

	public boolean contains(Object o) {
		return queue.contains(o);
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public int size() {
		return queue.size();
	}
}
