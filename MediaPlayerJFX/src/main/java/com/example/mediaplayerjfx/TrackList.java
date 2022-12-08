package com.example.mediaplayerjfx;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TrackList {

    private final String dirName;
    private final File directory;
    public TrackList(String dirName, File directory) {
        this.dirName = dirName;
        this.directory = directory;
    }
    public String getDirName() {
        return dirName;
    }
    public File[] setTrackFileCollection() { return directory.listFiles(); }
    public ArrayList<File> setFileTrackCollection(File[] fileCollection) {
        ArrayList<File> fileTrackCollection = new ArrayList<>();
        for (File file : fileCollection) {
            fileTrackCollection.add(new File(file.getAbsolutePath()));
        }
        return fileTrackCollection;
    }
    public ArrayList<Track> setTrackCollection(File[] fileCollection) throws InvalidDataException, UnsupportedTagException, IOException {
        ArrayList<Track> trackCollection = new ArrayList<>();
        for (File file : fileCollection) {
            trackCollection.add(new Track(new Mp3File(file)));
        }
        return trackCollection;
    }
}
