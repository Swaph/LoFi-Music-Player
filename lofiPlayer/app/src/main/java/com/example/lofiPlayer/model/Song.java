/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lofiPlayer.model;

/**
 * Song class representing the details of a song.
 */
public class Song {
    private Long id;
    private String title;
    private String artist;
    private String filePath;
    private int playCount;
    private String albumArtPath;
    private double duration; // Duration of the song in seconds

    // No-arg constructor
    public Song() {}

    public Song(Long id, String title, String artist, String filePath, int playCount, String albumArtPath, double duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.playCount = playCount;
        this.albumArtPath = albumArtPath;
        this.duration = duration;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
