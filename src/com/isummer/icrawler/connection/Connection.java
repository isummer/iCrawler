package com.isummer.icrawler.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class Connection {
	
	public final static int DEFAULT_TIMEOUT = 5*1000;
	public final static String DEFAULT_CHARSET = "UTF-8";
	
	public String targetUrl;
	
	public Map<String, String> headers = null;
	
	public Map<String, String> params = null;
	
	public String cookie = null;
	
	public Map<String, String> cookies = null;
	
	public int timeout = DEFAULT_TIMEOUT;
	
	public String charset = DEFAULT_CHARSET;
	
	public String contentType;
	
	private HttpURLConnection conn;
	
	public static enum Method {
        GET, POST
    }
	
	public Method method;

	/*
	public static Connection connect(String url) throws IOException {
		Connection connection = new Connection();
		connection.setTargetUrl(url);
		return connection;
	}
	*/
	public Connection header(String key, String value) {
		if(headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(key, value);
		return this;
	}
	
	public Connection header(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}
	
	public Connection cookie(String cookie) {
		this.cookie = cookie;
		cookie("Cookie", cookie);
		return this;
	}
	
	public Connection cookie(String name, String value) {
		if(cookies == null) {
			cookies = new HashMap<String, String>();
		}
		cookies.put(name, value);
		return this;
	}
	
	public Connection cookies(Map<String, String> cookies) {
		this.cookies = cookies;
		return this;
	}
	
	public Connection data(Map<String, String> params) {
		this.params = params;
		return this;
	}
	
	public Connection timeout(int miliseconds) {
		this.timeout = miliseconds;
		return this;
	}
	
	public Connection charset(String charset) {
		this.charset = charset;
		return this;
	}
	
	public String get() throws IOException {
		this.method = Method.GET;
		if (params != null && !params.isEmpty()) {
			String query = query(params);
			targetUrl += "?" + query;
		}
		return _connect();
	}
	
	public String post() throws IOException {
		this.method = Method.POST;
		return _connect();
	}
	
	public void setTargetUrl(String url) {
		this.targetUrl = url;
	}
	
	private String _connect() throws IOException {
		URL url = new URL(targetUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(DEFAULT_TIMEOUT);
		conn.setConnectTimeout(DEFAULT_TIMEOUT);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		switch(method) {
		case GET:
			conn.setRequestMethod("GET");
			break;
		case POST:
			conn.setRequestMethod("POST");
			break;
		}
		if(cookies != null && !cookies.isEmpty()) {
			for(Map.Entry<String, String> entry : cookies.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		if(timeout != 0) {
			conn.setConnectTimeout(timeout);
		}
		if(charset != null) {
			conn.setRequestProperty("Accept-Charset", charset);
		}
		conn.setUseCaches(false);
		if(headers != null && !headers.isEmpty()) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		conn.connect();
		String response = "";
		try{		
			if(method == Method.POST) {
				OutputStream os = conn.getOutputStream();
				if (params != null && !params.isEmpty()) {
					String query = query(params);
					IOUtils.write(query, os, DEFAULT_CHARSET);
				}
			}
			int resultCode = conn.getResponseCode();
			if(resultCode == HttpURLConnection.HTTP_OK) {
				this.contentType = conn.getContentType();
				InputStream is = conn.getInputStream();
				response = checkBom(IOUtils.toString(is, DEFAULT_CHARSET));
				is.close();
			}
			else{
				conn.disconnect();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	private String query(Map<String, String> params) {
		String query = "";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			query += entry.getKey() + "=" + entry.getValue() + "&";
		}
		query = query.substring(0, query.length()-1);
		return query;
	}
	
	public String getContentType() {
		return contentType;
	}
		
	private String checkBom(String string) {
		if(string.charAt(0) == 0xfeff) {
			string = string.substring(1);
		}
		return string;
	}
}
