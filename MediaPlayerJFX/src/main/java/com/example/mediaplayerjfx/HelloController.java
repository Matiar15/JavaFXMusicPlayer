package com.example.mediaplayerjfx;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    Media media;
    MediaPlayer mediaPlayer;
    String currentTimeString, timeLeftString;
    String currentSongTitle, nextSongTitle;
    double start;
    int[] data;
    int currentTrackIndex = 0;
    int nextTrackIndex = 1;
    int startOrStop = 0;
    private boolean stoppedRunning;
    Tasker tasker;
    TrackList trackList;
    File[] fileCollection;
    ArrayList<Track> trackMp3Collection;
    ArrayList<File> fileTrackCollection;
    DataSingleton data_1 = DataSingleton.getInstance();

    @FXML
    private Slider volumeSlider, timeSlider;
    @FXML
    private Label albumLabel;
    @FXML
    private ImageView songImage;
    @FXML
    private ListView<String> playlistView;
    @FXML
    private Button playPauseButton, previousButton, nextButton, darkLightButton, muteButton;
    @FXML
    private Label nextSongLabel, currentTimeLabel, timeLeftLabel, currentSongLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StackPane.setAlignment(currentTimeLabel, Pos.CENTER_LEFT );
        StackPane.setAlignment(timeLeftLabel, Pos.CENTER_RIGHT );
        trackList = new TrackList(data_1.getFile().getName(), data_1.getFile()); // getting data from DataSingleton,
                                                                                 // from first stage
        fileCollection = trackList.listDirectoryFiles();
        try {
            trackMp3Collection = trackList.setListOfMp3Files(fileCollection);
        } catch (InvalidDataException | UnsupportedTagException | IOException e) {
            throw new RuntimeException(e);
        }
        fileTrackCollection = trackList.setListOfTracksFiles(fileCollection);
        albumLabel.setText(trackList.getDirName());
        for (File file1 : fileTrackCollection) {
            playlistView.getItems().add(nameWithoutFileTag(file1));
        }
        setMediaPlayer();
        setTrackImage(songImage);
        setCurrentSongLabel();
        setTemplateTime(currentTimeLabel, timeLeftLabel);
        playlistView.getSelectionModel().select(currentTrackIndex);
        isNextTrack();
    }
    
    @FXML
    public void handlePlaylistViewSelection(MouseEvent click) {
        if (click.getClickCount() == 2) {
            setIndexValues("none");
        }
    }
    
    @FXML
    public void handleButtonStartStopAction(ActionEvent actionEvent) {
        if (playPauseButton.getText().equals(">")) {
            playAudio();
            playPauseButton.setText("||");
        } else {
            playPauseButton.setText(">");
            tasker.cancel();
            mediaPlayer.pause();
        }
        startOrStop++;
    }
    
    @FXML
    public void handleButtonPreviousAction(ActionEvent actionEvent) {
        setIndexValues("previous");
    }
    
    @FXML
    public void handleButtonNextAction(ActionEvent actionEvent) {
        setIndexValues("next");
    }
    
    @FXML
    public void handleButtonDarkLight(ActionEvent actionEvent) {

    }
    
    @FXML
    public void handleButtonMuteAction(ActionEvent actionEvent) {

    }

    @FXML
    public void setVolume() {
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100.0f);
            }
        });
    }
    
    @FXML
    public void stopSlider() {
        timeSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stoppedRunning = true;
                data = tasker.getData();
                tasker.cancel();
                mediaPlayer.pause();
                mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
            }
        });
    }
    
    @FXML
    public void skipTrack() {
        timeSlider.setOnMouseReleased((EventHandler<Event>) eventEvent -> {
            stoppedRunning = false;
            currentTimeDisplay();
            mediaPlayer.play();
            currentTimeString = secondsToMinutes((int) timeSlider.getValue());
            currentTimeLabel.setText(currentTimeString);
        });
    }

    public void currentTimeDisplay() {
        if (tasker.isCancelled()) {
            data = tasker.getData();
            data[0] = (int) timeSlider.getValue();
            tasker = new Tasker(data[0], data[1]);
        }
        tasker.valueProperty().addListener((observableValue, integer, t1) -> {
            start = t1;
            resetSliderValue(timeSlider);
            if (!stoppedRunning) timeSlider.setValue(start);
            currentTimeString = secondsToMinutes((int) start);
            currentTimeLabel.setText(currentTimeString);
            timeLeftString = secondsToMinutes(trackMp3Collection.get(currentTrackIndex).getTrackLength());
            timeLeftLabel.setText(timeLeftString);
            timeSlider.setDisable(start == trackMp3Collection.get(currentTrackIndex).getTrackLength());
        });
        Thread th = new Thread(tasker);
        th.setDaemon(true);
        th.start();
    }
    
    public String secondsToMinutes(long seconds) {
        int mins, secs;
        mins = (int) (seconds % 3600) / 60;
        secs = (int) seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public String nameWithoutFileTag(File fileName) { return fileName.getName().replace(".mp3", ""); }

    public void isNextTrack() {
        try {
            nextSongTitle = nameWithoutFileTag(fileTrackCollection.get(nextTrackIndex));
            nextSongLabel.setText(nextSongTitle);
        }
        catch (Exception IndexOutOfBoundsException) {
            nextSongLabel.setText(" - ");
        }
    }

    public void setMediaPlayer() {
        media = new Media(fileTrackCollection.get(currentTrackIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        tasker = new Tasker(0, (int) trackMp3Collection.get(currentTrackIndex).getTrackLength());
    }
    public void playAudio() {
        mediaPlayer.play();
        currentTimeDisplay();
    }
    public void stopMediaPlayer() {
        tasker.cancel();
        mediaPlayer.stop();
    }

    public void setCurrentSongLabel() {
        currentSongTitle = nameWithoutFileTag(fileTrackCollection.get(currentTrackIndex));
        currentSongLabel.setText(currentSongTitle);
    }

    public void resetSliderValue(Slider slider) {
        slider.setMin(0);
        slider.setMax(trackMp3Collection.get(currentTrackIndex).getTrackLength());
    }
    public void resetSliderValue(Slider slider, int value, boolean isDisabled) {
        slider.setMin(0);
        slider.setMax(trackMp3Collection.get(currentTrackIndex).getTrackLength());
        slider.setValue(value);
        slider.setDisable(isDisabled);
    }
    public void setTemplateTime(Label currentTimeLabel, Label timeLeftLabel) {
        currentTimeLabel.setText("00:00");
        String timeLeft = secondsToMinutes(trackMp3Collection.get(currentTrackIndex).getTrackLength());
        timeLeftLabel.setText(timeLeft);
    }
    public void setTrackImage(ImageView songImage) {
        try {
            songImage.setImage(SwingFXUtils.toFXImage(trackMp3Collection.get(currentTrackIndex).getImage(), null));
        } catch (IOException | URISyntaxException | NullPointerException exception) {
            songImage.setImage(new Image(new File("src/main/java/com/example/mediaplayerjfx/ikonka1.png").toURI().toString()));
        }
    }

    public void setIndexValues(String indexValue) {
        switch (indexValue) {
            case ("next") -> {
                if (currentTrackIndex == (fileTrackCollection.size() - 1)) currentTrackIndex = -1;
                currentTrackIndex++;
                nextTrackIndex++;
            }
            case ("previous") -> {
                if (currentTrackIndex == 0) currentTrackIndex = fileTrackCollection.size();
                currentTrackIndex--;
                nextTrackIndex = currentTrackIndex + 1;
            }
            case ("none") -> {
                currentTrackIndex = playlistView.getSelectionModel().getSelectedIndex();
                nextTrackIndex = currentTrackIndex + 1;
            }
        }
        playPauseButton.setText(">");
        stopMediaPlayer();
        setMediaPlayer();
        if (nextTrackIndex == fileTrackCollection.size()) nextTrackIndex = 0;
        setCurrentSongLabel();
        isNextTrack();
        setTemplateTime(currentTimeLabel, timeLeftLabel);
        playlistView.getSelectionModel().select(currentTrackIndex);
        resetSliderValue(timeSlider, 0, false);
        setTrackImage(songImage);
    }
}
