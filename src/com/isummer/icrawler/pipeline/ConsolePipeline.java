package com.isummer.icrawler.pipeline;

import com.isummer.icrawler.Page;
import com.isummer.icrawler.Task;

public class ConsolePipeline implements Pipeline {

    @Override
    public void process(Page page, Task task) {
        System.out.println("get page: " + page.getUrl());
        System.out.println(page.getDocument().text());
    }
}
