/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lofiPlayer.service;

import com.example.lofiPlayer.model.Song;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Admin
 */

public class SongService {

    private static final String SERVER_URL = "http://localhost:8080/api/songs";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SongService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Song> getAllSongs() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Arrays.asList(objectMapper.readValue(response.body(), Song[].class));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Song> searchSongs(String query) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL + "/search?query=" + query))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Arrays.asList(objectMapper.readValue(response.body(), Song[].class));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Song findSongByTitle(String title) {
        List<Song> songs = searchSongs(title);
        return songs.isEmpty() ? null : songs.get(0);
    }

    public void incrementPlayCount(Long songId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SERVER_URL + "/" + songId + "/incrementPlayCount"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
