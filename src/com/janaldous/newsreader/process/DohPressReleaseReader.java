package com.janaldous.newsreader.process;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.janaldous.newsreader.domain.NewsItem;

public class DohPressReleaseReader {
	
	public List<NewsItem> getPressRelease(String websites) {
		
		try {
			Document doc = Jsoup.connect("https://www.doh.gov.ph/press-releases").get();
			doc.select(".view-content").forEach((org.jsoup.nodes.Element x) -> {
				System.out.println(x.select(".views-field-title").text());
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
