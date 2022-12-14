package com.example.mediaplayerjfx;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class Track {
    private final Mp3File mp3File;

    public Track(Mp3File mp3File) {
        this.mp3File = mp3File;
    }

    public long getTrackLength() {
        return mp3File.getLengthInSeconds();
    }

    public BufferedImage getImage() throws IOException, URISyntaxException {
        if (mp3File.hasId3v2Tag()) {
            ID3v2 id3v2tag = mp3File.getId3v2Tag();
            byte[] imageData = id3v2tag.getAlbumImage();
            // converting the bytes to an image
            if (imageData != null) return ImageIO.read(new ByteArrayInputStream(imageData));
        }
        return null;
    }
}
