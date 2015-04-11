package com.isummer.icrawler.util;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class SplitWordUtils {
	
	public static List<Term> parse(String str) {
		return ToAnalysis.parse(str);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
