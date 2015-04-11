package com.isummer.icrawler.downloader;

import com.isummer.icrawler.Page;
import com.isummer.icrawler.Task;

public abstract class Downloader {

    public abstract Page download(String url, Task task);

}
