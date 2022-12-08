package com.example.mediaplayerjfx;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    Media media;
    MediaPlayer mediaPlayer;
    String currentTime, currentTimeString, fullTimeString;
    String currentSongTitle, nextSongTitle;
    double start;
    int[] data;
    int currentTrackIndex = 0;
    int nextTrackIndex = 1;
    Tasker tasker;
    TrackList trackList;
    File[] fileCollection;
    ArrayList<Track> trackCollection;
    ArrayList<File> fileTrackCollection;
    DataSingleton data_1 = DataSingleton.getInstance();
    @FXML
    private Label defaultSongLabel;
    @FXML
    private Slider volumeSlider, timeSlider;
    @FXML
    private Label albumLabel;
    @FXML
    private ListView<String> playlistView;
    @FXML
    private Button pauseButton, nextButton, playButton;
    @FXML
    private Label nextSongLabel, currentSongTimeLabel, currentSongTitleLabel, nextSongLabelUpdate;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trackList = new TrackList(data_1.getFile().getName(), data_1.getFile());
        fileCollection = trackList.setTrackFileCollection();
        try {
            trackCollection = trackList.setTrackCollection(fileCollection);
        } catch (InvalidDataException | UnsupportedTagException | IOException e) {
            throw new RuntimeException(e);
        }
        fileTrackCollection = trackList.setFileTrackCollection(fileCollection);
        albumLabel.setText(trackList.getDirName());
        for (File file1 : fileTrackCollection) {
            playlistView.getItems().add(file1.getName().replace(".mp3", ""));
        }
        media = new Media(fileTrackCollection.get(currentTrackIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        tasker = new Tasker(0, (int) trackCollection.get(currentTrackIndex).getTrackLength());
        fullTimeString = String.valueOf(secondsToMinutes(trackCollection.get(currentTrackIndex).getTrackLength()));
        currentSongTimeLabel.setText("00,00 : " + fullTimeString);
        playlistView.getSelectionModel().select(currentTrackIndex);
        try {
            nextSongTitle = fileTrackCollection.get(nextTrackIndex).getName().replace(".mp3", "");
            nextSongLabelUpdate.setText(nextSongTitle);
        }
        catch (Exception IndexOutOfBoundsException) {
            nextSongLabelUpdate.setText("-");
        }
    }
    @FXML
    public void handleButtonStartAction(ActionEvent actionEvent) {
        playAudio();
        currentSongTitle = fileTrackCollection.get(currentTrackIndex).getName().replace(".mp3", "");
        currentSongTitleLabel.setText(currentSongTitle);
        try {
            nextSongTitle = fileTrackCollection.get(nextTrackIndex).getName().replace(".mp3", "");
            nextSongLabelUpdate.setText(nextSongTitle);
        }
        catch (Exception IndexOutOfBoundsException) {
            nextSongLabelUpdate.setText("-");
        }
    }
    @FXML
    public void handleButtonStopAction(ActionEvent actionEvent) {
        data = tasker.getData(); // getting data from previous pause
        tasker.cancel();
        mediaPlayer.pause();
        playButton.setDisable(false);
        pauseButton.setDisable(true);
    }
    @FXML
    public void handleButtonNextAction(ActionEvent actionEvent) {
        if (currentTrackIndex == (fileTrackCollection.size() - 1)) currentTrackIndex = -1;
        currentTrackIndex++;
        nextTrackIndex++;
        if (nextTrackIndex == (fileTrackCollection.size())) nextTrackIndex = 0;
        mediaPlayer.stop();
        tasker.cancel();
        media = new Media(fileTrackCollection.get(currentTrackIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        tasker = new Tasker(0, (int) trackCollection.get(currentTrackIndex).getTrackLength());
        currentSongTitle = fileTrackCollection.get(currentTrackIndex).getName().replace(".mp3", "");
        currentSongTitleLabel.setText(currentSongTitle);
        try {
            nextSongTitle = fileTrackCollection.get(nextTrackIndex).getName().replace(".mp3", "");
            nextSongLabelUpdate.setText(nextSongTitle);
        }
        catch (Exception IndexOutOfBoundsException) {
            nextSongLabelUpdate.setText("There is only one song");
        }
        fullTimeString = secondsToMinutes(trackCollection.get(currentTrackIndex).getTrackLength());
        currentSongTimeLabel.setText("00,00 : " + fullTimeString);
        playButton.setDisable(false);
        pauseButton.setDisable(false);
        playlistView.getSelectionModel().select(currentTrackIndex);
    }
    public void currentTimeDisplay() {
        if (tasker.isCancelled()) {
            tasker = new Tasker(data[0], data[1]);
        }
        tasker.valueProperty().addListener((observableValue, integer, t1) -> {
            start = t1;
            timeSlider.setMin(0);
            timeSlider.setMax(trackCollection.get(currentTrackIndex).getTrackLength());
            timeSlider.setValue(start);
            currentTimeString = secondsToMinutes((int) start);
            fullTimeString = secondsToMinutes(trackCollection.get(currentTrackIndex).getTrackLength());
            currentTime = currentTimeString + " : " + fullTimeString;
            currentSongTimeLabel.setText(currentTime);
        });
        Thread th = new Thread(tasker);
        th.setDaemon(true);
        th.start();
    }
    public void playAudio() {
        currentTimeDisplay();
        mediaPlayer.play();
        playButton.setDisable(true);
        pauseButton.setDisable(false);
    }
    public String secondsToMinutes(long seconds) {
        int mins, secs;
        mins = (int) (seconds % 3600) / 60;
        secs = (int) seconds % 60;
        return String.format("%02d,%02d", mins, secs);
    }
}
