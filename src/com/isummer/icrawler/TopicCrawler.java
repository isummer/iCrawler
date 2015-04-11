package com.isummer.icrawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.isummer.icrawler.util.WeioboParser;
import com.isummer.icrawler.util.WeiboAPIv3;

public class TopicCrawler {
	
	private static String getHtml(String original) {
		String html = "";
		try {

			Pattern pattern = Pattern.compile("FM.view\\(\\{(.*?)\\}\\)");
			Matcher matcher = pattern.matcher(original);
			
			boolean result = matcher.find();
			while(result) {
				
				String segment = matcher.group();
				result = matcher.find();
				
				String jsonStr = segment.substring(8, segment.length()-1);
				if(jsonStr.length()<=2) continue;
				JSONObject json = JSON.parseObject(jsonStr);
				if(json.containsKey("html")) { 
					html += json.getString("html")+"\r\n";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return html;
	}

	public static void main(String[] args) {
		
		try {
			
			String cookie = WeiboAPIv3.getSinaCookie("微博帐号",
					"微博密码");

			String original = TSpider
					.connect("http://d.weibo.com/102803_ctg1_3299_-_ctg1_3299?page=1")
					.header("User-Agent", 
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)")
					.cookie("Cookie", cookie).get();

			String html = getHtml(original);
			
			parse(html);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void parse(String html) {
		Document doc = Jsoup.parse(html);
		WeioboParser.parse(doc);
	}
}
