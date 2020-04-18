package com.janaldous.newsreader.process;

import java.util.List;

import org.bson.Document;

import com.janaldous.newsreader.domain.NewsItem;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class WriteMongoDBProcess implements WriteProcess {

	@Override
	public void process(List<NewsItem> newsItems) {
		// set up
		try (MongoClient mongoClient = new MongoClient("localhost")) {
			MongoDatabase database = mongoClient.getDatabase("newsreader");

			MongoCollection<Document> collection = database.getCollection("newsItems");
			assert(collection != null);
			
			// write
			newsItems.stream().forEach(x -> {
				System.out.println(x.getTitle());
				Document doc = new Document();
				doc.put("md5hash", x.getHash());
				
				System.out.println(!collection.find(doc).iterator().hasNext());
				
				if (!collection.find(doc).iterator().hasNext()) {
					Document document = new Document();
					document.put("title", x.getTitle());
					document.put("author", x.getAuthor());
					document.put("description", x.getDescription());
					document.put("timestamp", x.getTimestamp());
					document.put("link", x.getLink());
					document.put("md5hash", x.getHash());
					collection.insertOne(document);
				}
			});
		}
	}

}
