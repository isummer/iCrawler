package com.isummer.icrawler.pipeline;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.isummer.icrawler.Page;
import com.isummer.icrawler.Task;

public class SplitWordPipeline implements Pipeline{

	public SplitWordPipeline() {
		initAnsj();
	}
	
	private void initAnsj() {
		ToAnalysis.parse("");
	}
	
	@Override
	public void process(Page page, Task task) {
		System.out.println();
		List<Term> splits = ToAnalysis.parse(page.getDocument().text());
		/*
		for(Term word : splits) {
			System.out.print (word.getName()+" | ");
		}
		*/
		System.out.println(splits);
		System.out.println();
	}

}
