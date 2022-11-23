package com.example.mediaplayerjfx;

import javafx.concurrent.Task;
import javafx.scene.media.MediaPlayer;


public class TaskerMedia extends Task<Integer> {

    private final int end;
    private int start;
    private final MediaPlayer mediaPlayer;
    public TaskerMedia(int start, int end, MediaPlayer mediaPlayer) {
        this.end = end;
        this.start = start;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    protected Integer call() throws Exception {
        while (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            continue;
        }
        playNextTrack();
        return null;
    }

        public void playNextTrack() {
        mediaPlayer.play();
        }
    }

