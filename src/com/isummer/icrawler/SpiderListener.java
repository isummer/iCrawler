package com.isummer.icrawler;

public interface SpiderListener {

    public void onSuccess(Page page);

    public void onError(Page page);
}
