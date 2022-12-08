package com.example.mediaplayerjfx;

import com.mpatric.mp3agic.Mp3File;
public class Track {
    private final Mp3File mp3File;
    public Track(Mp3File mp3File) {
        this.mp3File = mp3File;
    }
    public long getTrackLength() {
        return mp3File.getLengthInSeconds();
    }
}

