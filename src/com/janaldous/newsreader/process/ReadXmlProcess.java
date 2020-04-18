package com.janaldous.newsreader.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadXmlProcess {

	public void process(String webisteURL) {
		try {
			URL feedSource = new URL(webisteURL);
			HttpURLConnection httpConnections = getHTTPConnections(feedSource);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnections.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			
			String line = null;
			for (int i = 0; i < 50 && (line = reader.readLine()) != null; i++) {
				stringBuilder.append(line + "\n");
				System.out.println(line);
			}
		} catch (IOException e) {
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
	
}
