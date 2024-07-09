package com.alura.literalura.service;

import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public void fetchAndSaveBooks() throws IOException {
        String apiUrl = "https://gutendex.com/books/";

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inline.toString());
        JsonNode resultsNode = rootNode.get("results");

        if (resultsNode.isArray()) {
            Iterator<JsonNode> elements = resultsNode.elements();
            while (elements.hasNext()) {
                JsonNode bookNode = elements.next();
                Libro libro = new Libro();
                libro.setTitulo(bookNode.get("title").asText());
                if (bookNode.get("authors").isArray() && bookNode.get("authors").size() > 0) {
                    libro.setAutor(bookNode.get("authors").get(0).get("name").asText());
                } else {
                    libro.setAutor("Unknown");
                }
                libro.setDescargas(0); // Valor predeterminado para descargas
                libroRepository.save(libro);
            }
        }
    }

    public List<Libro> getAllBooks() {
        return libroRepository.findAll();
    }

    public List<Libro> getBooksByAuthor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }
}
