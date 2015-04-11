package com.isummer.icrawler.util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class HttpUtil {
	
	public final static int DEFAULT_TIMEOUT = 5*1000;
	public final static String DEFAULT_CHARSET = "UTF-8";
	
	public static String cookie = null;
	
	public static enum Method {
        GET, POST
    }
	
	public static String contentType;
	
	public static String post(String taskUrl, Map<String, String> params) {
		String response = "";
		try {
			HttpURLConnection conn = connect(taskUrl, Method.POST, null);
			
			OutputStream os = conn.getOutputStream();
			if (params != null && !params.isEmpty()) {
				String query = query(params);
				IOUtils.write(query, os, DEFAULT_CHARSET);
			}
			
			int resultCode = conn.getResponseCode();
			if(resultCode == HttpURLConnection.HTTP_OK) {
				contentType = conn.getContentType();
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
	
	public static String get(String taskUrl, Map<String, String> params) {
		String response = "";
		if (params != null && !params.isEmpty()) {
			String query = query(params);
			taskUrl += "?" + query;
		}
		try{
			HttpURLConnection conn = connect(taskUrl, Method.GET, null);
			
			int resultCode = conn.getResponseCode();
			if(resultCode == HttpURLConnection.HTTP_OK) {
				contentType = conn.getContentType();
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
	
	public static String getContentType() {
		return contentType;
	}
		
	private static String checkBom(String string) {
		if(string.charAt(0) == 0xfeff) {
			string = string.substring(1);
		}
		return string;
	}
	private static HttpURLConnection connect(String urlStr, Method method, Map<String, String> properties) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
		if(cookie != null) {
			conn.setRequestProperty("Cookie", cookie);
		}
		conn.setUseCaches(false);
		if(properties != null) {
			for(Map.Entry<String, String> entry : properties.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		conn.connect();
		return conn;
	}
	
	private static String query(Map<String, String> params) {
		String query = "";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			query += entry.getKey() + "=" + entry.getValue() + "&";
		}
		query = query.substring(0, query.length()-1);
		return query;
	}
}