package com.isummer.icrawler.downloader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.isummer.icrawler.Crawler;
import com.isummer.icrawler.Page;
import com.isummer.icrawler.Task;
import com.isummer.icrawler.connection.Connection;

public class DefaultDownloader extends Downloader{

	@Override
	public Page download(String url, Task task) {
		boolean isSave = false;
		Document doc = null;
		String response = "";
		try {
			Connection conn = Crawler.connect(url, task.getSite());
			response = conn.get();
			if(isSave) {
				if(response != null) {
					String filePath = "e:\\crawler-data\\" + getFileNameByUrl(url, conn.getContentType());
					saveToLocal(response, filePath);
				}
			}
			doc = Jsoup.parse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc != null ? new Page(url,doc) : null;
	}
	
	public String getFileNameByUrl(String url, String contentType) {
		url = url.substring(7);
		if(contentType.indexOf("html") != -1) {
			url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			return url;
		} else {
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "." +
					contentType.substring(contentType.lastIndexOf("/")+1);
		}
	}
	
	private void saveToLocal(String response, String filePath) {
		try {
			DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(filePath)));
			IOUtils.write(response, output);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
