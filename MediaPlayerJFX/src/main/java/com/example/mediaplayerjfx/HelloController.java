package com.example.mediaplayerjfx;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.media.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

public class HelloController implements Initializable {
    Media media;
    MediaPlayer mediaPlayer;
    String currentTime, currentTimeString, fullTimeString;
    double start;
    Tasker tasker = new Tasker(0, 250);
    int[] data;
    String[] tracks;
    Track track;
    TrackList trackList;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ListView<String> playlistView;
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private ImageView musicImage, nextSongImage;
    @FXML
    private Button pauseButton, playButton;
    @FXML
    private Label nextSongLabel, currentSongTimeLabel, currentSongTitleLabel;
    @FXML
    private ProgressBar songProgressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trackList = new TrackList("Główny album");
        try {
            trackList.createTracks();
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            throw new RuntimeException(e);
        }
        playlistView.getItems().addAll(tracks);
        currentSongTimeLabel.setText("0,00 : 0,00");
        currentSongTitleLabel.setText("Current Song: - " + currentSongTitleLabel);
    }
    @FXML
    public void handleButtonStartAction(ActionEvent actionEvent) {
        currentTimeDisplay();
        mediaPlayer.play();
        playButton.setDisable(true);
        pauseButton.setDisable(false);
    }
    @FXML
    public void handleButtonStopAction(ActionEvent actionEvent) {
        data = tasker.getData();
        tasker.cancel();
        mediaPlayer.pause();
        playButton.setDisable(false);
        pauseButton.setDisable(true);
    }

    public void currentTimeDisplay() {
        if (tasker.isCancelled()) {
            tasker = new Tasker(data[0], data[1]);
        }
        tasker.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                start = t1;
                currentTimeString = String.format("%.2f", start / 100);
                currentTime = currentTimeString + " : " + fullTimeString;
                currentSongTimeLabel.setText(currentTime);
            }
        });
        songProgressBar.progressProperty().bind(tasker.progressProperty());
        Thread th = new Thread(tasker);
        th.setDaemon(true);
        th.start();

    }
    public void currentTrack(Track generatedTrack) {

    }
    public void currentTrack() {
        if (tasker.isCancelled()) {
            tasker = new Tasker(data[0], data[1]);
        }
        tasker.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {

            }
        });
    }

}
