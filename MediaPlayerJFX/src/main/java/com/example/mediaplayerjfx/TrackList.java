package com.example.mediaplayerjfx;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TrackList {
    private String dirName;
    private final File directory = new File(System.getProperty("user.dir") + "\\MediaPlayerJFX\\src\\main\\java\\MainAlbum") ;
    private final LinkedHashSet<Track> trackSet = new LinkedHashSet<>();
    private final LinkedHashSet<String> tracksNames = new LinkedHashSet<>();
    public TrackList(String dirName) {
        this.dirName = dirName;
    }

    public void createTracks() throws InvalidDataException, UnsupportedTagException, IOException {
        File[] trackCollection = directory.listFiles();

        for (int i = 0; i < Objects.requireNonNull(trackCollection).length; i++) {
            trackSet.add(new Track(new Mp3File(trackCollection[i].getAbsolutePath())));
            tracksNames.add(trackSet[i].get)
        }
        Collections.shuffle((List<?>) trackSet);

    }

}
