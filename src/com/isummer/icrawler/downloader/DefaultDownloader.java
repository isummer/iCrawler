package com.isummer.icrawler.downloader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import com.isummer.icrawler.Crawler;
import com.isummer.icrawler.Page;
import com.isummer.icrawler.Task;

public class DefaultDownloader extends Downloader{

	@Override
	public Page download(String url, Task task) {
		boolean isSave = false;
		Document doc = null;
		try {
			Connection conn = Crawler.connect(url, task.getSite());
			doc = conn.get();
			if(isSave) {
				String response = doc.html();
				if(response != null) {
					String filePath = "e:\\crawler-data\\" + getFileNameByUrl(url, conn.response().contentType());
					saveToLocal(response, filePath);
				}
			}
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
