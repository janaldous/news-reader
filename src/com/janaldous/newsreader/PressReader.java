package com.janaldous.newsreader;

import java.util.ArrayList;
import java.util.List;

import com.janaldous.newsreader.process.DohPressReleaseReader;

public class PressReader {
	public static void main(String[] args) {
		List<String> rssFeeds = new ArrayList<>();
		rssFeeds.add("https://www.doh.gov.ph/press-releases");
		
		DohPressReleaseReader reader = new DohPressReleaseReader();
		reader.getPressRelease(rssFeeds.get(0));
	}
}
