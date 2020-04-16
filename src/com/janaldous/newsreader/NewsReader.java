package com.janaldous.newsreader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.google.common.io.BaseEncoding;
import com.janaldous.newsreader.domain.NewsItem;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class NewsReader {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			String doh = "http://www.doh.gov.ph/press-releases";
			String inquirer = "https://www.inquirer.net/fullfeed";
			String philstar = "https://www.philstar.com/rss/headlines";
			
			List<String> rssFeeds = new ArrayList<>();
			rssFeeds.add("https://www.inquirer.net/fullfeed");
			rssFeeds.add("https://www.philstar.com/rss/headlines");
			
			URL feedSource = new URL(philstar);
			SyndFeedInput input = new SyndFeedInput();

			HttpURLConnection httpConnections = getHTTPConnections(feedSource);
//			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnections.getInputStream()));
//			StringBuilder stringBuilder = new StringBuilder();
//
//			String line = null;
//			for (int i = 0; i < 50 && (line = reader.readLine()) != null; i++) {
//				stringBuilder.append(line + "\n");
//				System.out.println(line);
//			}
			try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
				MongoDatabase database = mongoClient.getDatabase("newsreader");

				MongoCollection<Document> collection = database.getCollection("newsItems");
				assert(collection != null);

				SyndFeed feed = input.build(new XmlReader(httpConnections));
				List<SyndEntry> entries = new ArrayList<>(feed.getEntries());
				entries.stream().map(x -> {
					String hash = toHash(x.getTitle(), x.getPublishedDate(), x.getAuthor());
					return NewsItem.builder().title(x.getTitle()).author(x.getAuthor()).description(x.getDescription().getValue())
							.timestamp(x.getPublishedDate()).link(x.getLink()).hash(hash).build();
				}).forEach(x -> {
					Document doc = new Document();
					doc.put("md5hash", x.getHash());
					if (!collection.find(doc).iterator().hasNext()) {
						Document document = new Document();
						document.put("title", x.getTitle());
						document.put("author", x.getAuthor());
						document.put("description", x.getDescription());
						document.put("timestamp", x.getTimestamp());
						document.put("link", x.getLink());
						document.put("md5hash", x.getHash());
						collection.insertOne(document);
						System.out.println(x);
						System.out.println();
					}
				});
			}

			httpConnections.disconnect();
		} catch (IllegalArgumentException | FeedException | IOException e) {
			e.printStackTrace();
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

	private static HttpURLConnection getHTTPConnections(URL url) {
		try {
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
			return httpcon;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
