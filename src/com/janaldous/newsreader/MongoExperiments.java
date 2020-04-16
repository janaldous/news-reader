package com.janaldous.newsreader;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoExperiments {
	public static void main(String[] args) {
		
		try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
			MongoDatabase database = mongoClient.getDatabase("newsreader");
			
			MongoCollection<Document> collection = database.getCollection("newsItems");
			assert(collection != null);
			
			AtomicInteger i = new AtomicInteger();
			collection.find().forEach((Document d) -> {
				i.getAndIncrement();
				System.out.println(MessageFormat.format("[{0}] {1}", i, d.get("title")));
				System.out.println(d.get("link"));
				System.out.println();
			});
			System.out.println("end");
		}
	}
}
