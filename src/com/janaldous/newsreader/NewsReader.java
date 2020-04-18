package com.janaldous.newsreader;

import java.util.ArrayList;
import java.util.List;

import com.janaldous.newsreader.process.ReadProcess;
import com.janaldous.newsreader.process.WriteMongoDBProcess;
import com.janaldous.newsreader.process.WriteProcess;
import com.janaldous.newsreader.process.WriteSysOutPrintProcess;

public class NewsReader {

	public static void main(String[] args) {
		List<String> rssFeeds = new ArrayList<>();
		rssFeeds.add("https://www.inquirer.net/fullfeed");
		rssFeeds.add("https://www.philstar.com/rss/headlines");
		rssFeeds.add("https://data.gmanews.tv/gno/rss/news/metro/feed.xml");
		rssFeeds.add("https://www.interaksyon.com/feed/");
		

		ReadProcess readProcess = new ReadProcess();
//		WriteProcess writeProcess = new WriteMongoDBProcess();
		WriteProcess writeProcess = new WriteSysOutPrintProcess();
		for (String website : rssFeeds) {
			writeProcess.process(readProcess.read(website));
		}
	}

}
