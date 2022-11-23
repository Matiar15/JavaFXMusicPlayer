package com.example.mediaplayerjfx;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;

public class Track {
    private Mp3File mp3File;
    private String trackName;

    Track(Mp3File mp3File) {
        this.mp3File = mp3File;
        this.trackName = getTrackName(this.mp3File);
    }

    public long getTrackLenght(Mp3File mp3File) {
        return mp3File.getLengthInSeconds();
    }

    public void getID3v2Tag(Mp3File mp3File) {
        ID3v2 id3v2Tag = mp3File.getId3v2Tag();
        byte[] imageData = id3v2Tag.getAlbumImage();
    }

    public String getTrackName(Mp3File mp3File) {
        if (mp3File.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3File.getId3v1Tag();
            return id3v1Tag.getTrack();
        } else return null;
    }
}

