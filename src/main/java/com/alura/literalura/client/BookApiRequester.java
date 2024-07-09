package com.alura.literalura.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class BookApiRequester {

    private static final Logger logger = LoggerFactory.getLogger(BookApiRequester.class);
    private static final String API_URL ="https://gutendex.com/books/";

    public String fetchBooks() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching books from API", e);
            return null;
        }
    }
}
