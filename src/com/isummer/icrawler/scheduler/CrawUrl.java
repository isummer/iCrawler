package com.isummer.icrawler.scheduler;

import java.io.Serializable;

public class CrawUrl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String orignal;
	
	private String url;
	
	private int statusCode;
	
	private String charSet;
	
	private String type;
	
	private String urlReferences;
	
	private int layer;

	public String getOrignal() {
		return orignal;
	}

	public void setOrignal(String orignal) {
		this.orignal = orignal;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrlReferences() {
		return urlReferences;
	}

	public void setUrlReferences(String urlReferences) {
		this.urlReferences = urlReferences;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
}
