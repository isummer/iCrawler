package com.isummer.icrawler.connection;

public enum UserAgent {

	Mozilla("Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
	
	private String value;

	private UserAgent(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
