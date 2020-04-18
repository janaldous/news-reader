package com.janaldous.newsreader.process;

import java.util.List;

import com.janaldous.newsreader.domain.NewsItem;

public interface WriteProcess {
	
	public void process(List<NewsItem> newsItems);
	
}
