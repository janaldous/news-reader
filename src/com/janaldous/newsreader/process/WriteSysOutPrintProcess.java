package com.janaldous.newsreader.process;

import java.util.List;

import com.janaldous.newsreader.domain.NewsItem;

public class WriteSysOutPrintProcess implements WriteProcess {

	@Override
	public void process(List<NewsItem> newsItems) {
		newsItems.stream().forEach(x -> {
			System.out.println(x.getTitle());
			System.out.println();
		});
	}

}
