package com.example.mediaplayerjfx;
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
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    Media media;
    MediaPlayer mediaPlayer;
    String currentTime, currentTimeString, fullTimeString;
    double start;
    Tasker tasker = new Tasker(0, 250);
    int[] data;
    String[] tracks;

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
        File fileMedia = new File("src/Tracklista");
        tracks = getDirTracks(fileMedia);
        playlistView.getItems().addAll(tracks);

        currentSongTimeLabel.setText("0,00 : 0,00");
        currentSongTitleLabel.setText("Current Song: - ");

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
    public String[] getDirTracks(File directory) {
        tracks = new String[(int) directory.length()];
        return tracks = directory.list();
    }
}
