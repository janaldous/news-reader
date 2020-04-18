package com.janaldous.newsreader.process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.io.BaseEncoding;
import com.janaldous.newsreader.domain.NewsItem;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class ReadProcess {

	public List<NewsItem> read(String webisteURL) {
		try {
			URL feedSource = new URL(webisteURL);
			HttpURLConnection httpConnections = getHTTPConnections(feedSource);

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpConnections));
			List<SyndEntry> entries = new ArrayList<>(feed.getEntries());
			List<NewsItem> newsItems = entries.stream().map(x -> {
				String hash = toHash(x.getTitle(), x.getPublishedDate(), x.getAuthor());
				return NewsItem.builder().title(x.getTitle()).author(x.getAuthor())
						.description(x.getDescription().getValue()).timestamp(x.getPublishedDate()).link(x.getLink())
						.hash(hash).build();
			}).collect(Collectors.toList());

			httpConnections.disconnect();
			return newsItems;
		} catch (IllegalArgumentException | FeedException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private HttpURLConnection getHTTPConnections(URL url) {
		try {
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
			return httpcon;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String toHash(String title, Date date, String author) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update((title + date.toString() + author).getBytes());
			byte[] digest = md.digest();
			return BaseEncoding.base16().encode(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
