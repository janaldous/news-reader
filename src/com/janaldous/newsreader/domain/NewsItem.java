package com.janaldous.newsreader.domain;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewsItem {
	
	private String title;
	private String author;
	private Date timestamp;
	private String description;
	private String link;
	private String hash;
	
}
