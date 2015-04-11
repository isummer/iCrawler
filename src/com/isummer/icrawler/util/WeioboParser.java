package com.isummer.icrawler.util;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WeioboParser {

	public static void parse(Document doc) {

		// Document doc =
		// Jsoup.connect("http://weibo.com/u/5382682905").get();
		// 获取微博列表 <div tbinfo="ouid=5382682905" ……
		Elements weiboList = doc.select("div[tbinfo]");

		String nickname = "";
		// 获取用户ID <div tbinfo="ouid=5382682905" ……
		String usercard = weiboList.attr("tbinfo").substring(5);

		print("\nItems Count:(%d)\n", weiboList.size());
		// 遍历并格式输出每条微博
		for (Element item : weiboList) {
			Elements detail = item.select("div.WB_detail");
			Elements handle = item.select("div.WB_handle");

			if (detail.select("div.WB_info a").size() > 0) {
				nickname = detail.select("div.WB_info a").attr("nick-name");
				// usercard =
				// detail.select("div.WB_info a").attr("usercard");
			} else {
				nickname = detail.select("div.WB_text.W_f14").attr("nick-name");
			}

			String content = detail.select("div.WB_text.W_f14").text();

			if (detail.select("div").size() > 2) {
				// 转播或贴图
			}

			Elements from = detail.select("div.WB_from.S_txt2");
			String date = from.select("a[date]").attr("title");
			String type = from.select("a[action-type]").text();

			Elements nodes = handle.select("span.pos");
			String collect = nodes.select("span.line.S_line1").first().text();
			String forward = nodes.select("span[node-type=forward_btn_text]")
					.text();
			String comment = nodes.select("span[node-type=comment_btn_text]")
					.text();
			// int prise =
			// Integer.getInteger(nodes.select("span[node-type=like_status]").select("em").text(),
			// 0);
			String priseCount = nodes.select("span[node-type=like_status]")
					.select("em").text().trim();
			int prise = 0;
			if (priseCount.length() != 0) {
				prise = Integer.parseInt(priseCount);
			}

			print(" %s(%s):", nickname, usercard);
			print("  * %s *", content);
			print(" %s 来自:%s", date, type);
			print(" %s  %s  %s  赞 %d", collect, forward, comment, prise);
			print("");
		}
	}

	public static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
