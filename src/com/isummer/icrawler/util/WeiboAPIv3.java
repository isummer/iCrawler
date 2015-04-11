package com.isummer.icrawler.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiboAPIv3 {

	public final static int DEFAULT_TIMEOUT = 10 * 1000;
	public final static String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0";

	public final static String baseUrl = "http://login.weibo.cn/login/";

	public static String getSinaCookie(String account, String passwd)
			throws Exception {

		Map<String, String> params = new HashMap<String, String>();

		String cookie = "";

		Document original = Jsoup.connect(baseUrl)
				.header("User-Agent", userAgent).get();

		Element form = original.select("form").get(0);
		Elements inputs = form.select("input");
		for (Element input : inputs) {
			if (input.attr("name").equals("mobile")) {
				params.put(input.attr("name"), account);
			} else if (input.attr("type").equals("password")) {
				params.put(input.attr("name"), passwd);
			} else if (input.attr("type").equals("checkbox")) {
				params.put(input.attr("name"), "on");
			} else if (input.attr("type").equals("hidden")) {
				params.put(input.attr("name"), input.attr("value"));
			} else if (input.attr("type").equals("submit")) {
				params.put(input.attr("name"), input.attr("value"));
			}
		}

		Connection conn = Jsoup.connect(baseUrl + form.attr("action"));
		conn.header("Host", "login.weibo.cn");
		conn.header("User-Agent", userAgent);
		conn.header("Content-Type", "application/x-www-form-urlencoded");

		conn.data(params);

		Response response = conn.data(params).timeout(DEFAULT_TIMEOUT)
				.method(Method.POST).execute();

		Map<String, String> cookies = response.cookies();
		Iterator<Entry<String, String>> it = cookies.entrySet().iterator();
		while (it.hasNext()) {
			cookie += it.next().toString() + ";";
		}
		return cookie;
	}
}
