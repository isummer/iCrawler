package com.isummer.icrawler.pipeline;

import com.isummer.icrawler.Page;
import com.isummer.icrawler.Task;

public interface Pipeline {

    public void process(Page page, Task task);
}
