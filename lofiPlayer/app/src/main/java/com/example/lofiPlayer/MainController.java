/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lofiPlayer;

import com.example.lofiPlayer.model.Song;
import com.example.lofiPlayer.service.SongService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.List;

public class MainController {

    @FXML
    private VBox mainContent;

    @FXML
    private TextField searchFieldResults;

    @FXML
    private ListView<String> songListView;

    @FXML
    private ImageView albumArt;

    @FXML
    private Text songTitle;

    @FXML
    private Text songArtist;

    @FXML
    private Slider progressSlider;

    @FXML
    private Text currentTime;

    @FXML
    private Text totalTime;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Button playPauseButton;

    private MediaPlayer mediaPlayer;
    private SongService songService = new SongService();
    private List<Song> songs;
    private int currentSongIndex = -1;

    public void initialize() {
        loadSongs();
        setupEventListeners();
    }

    private void loadSongs() {
        songs = songService.getAllSongs();
        refreshPlaylist();
    }

    private void setupEventListeners() {
        songListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && songListView.getSelectionModel().getSelectedIndex() >= 0 && !songs.isEmpty()) {
                Song song = songs.get(songListView.getSelectionModel().getSelectedIndex());
                playSong(song);
            }
        });

        progressSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (progressSlider.isValueChanging() && mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
            }
        });

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        searchFieldResults.setOnAction(event -> searchSongs());
    }

    private void playSong(Song song) {
        if (song != null) {
            String filePath = Paths.get(song.getFilePath()).toUri().toString();
            try {
                Media media = new Media(filePath);
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                mediaPlayer = new MediaPlayer(media);

                // Add listener to increment play count when the song starts playing
                mediaPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                    if (newStatus == Status.PLAYING && oldStatus != Status.PLAYING) {
                        songService.incrementPlayCount(song.getId());
                        song.setPlayCount(song.getPlayCount() + 1); // Locally increment play count
                        refreshPlaylist();
                    }
                });

                mediaPlayer.play();
                mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                    progressSlider.setValue(newTime.toSeconds());
                    currentTime.setText(formatTime(newTime.toSeconds()));
                });
                mediaPlayer.setOnReady(() -> {
                    progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
                    totalTime.setText(formatTime(mediaPlayer.getTotalDuration().toSeconds()));
                });

                updatePlayerUI(song);
                currentSongIndex = songs.indexOf(song);
            } catch (Exception e) {
                System.err.println("Error loading media: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseButton.setText("Play");
            } else {
                mediaPlayer.play();
                playPauseButton.setText("Pause");
            }
        }
    }

    private void updatePlayerUI(Song song) {
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        totalTime.setText(formatTime(mediaPlayer.getTotalDuration().toSeconds()));
        if (song.getAlbumArtPath() != null && !song.getAlbumArtPath().isEmpty()) {
            albumArt.setImage(new Image(Paths.get(song.getAlbumArtPath()).toUri().toString())); // Load from local path
        } else {
            albumArt.setImage(null); // Set to a default image or null
        }
    }

    private String formatSong(Song song) {
        return song.getTitle() + " - " + song.getArtist() + " (Played " + song.getPlayCount() + " times)";
    }

    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int secs = (int) seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void refreshPlaylist() {
        songListView.getItems().clear();
        for (Song song : songs) {
            songListView.getItems().add(formatSong(song));
        }
    }

    @FXML
    private void searchSongs() {
        String query = searchFieldResults.getText();
        if (query.isEmpty()) {
            loadSongs(); // Reload all songs if search query is empty
        } else {
            List<Song> searchResults = songService.searchSongs(query);
            songs = searchResults; // Update the songs list with search results
            refreshPlaylist();
        }
    }
}
