package com.ajaysinghdewari.devd.models;

/**
 * Created by Ajay on 25-02-2017.
 */

public class Song {

    private long id;
    private String title;
    private String artist;
    private String image;

    public Song(long songID, String songTitle, String songArtist, String songImage) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        image=songImage;
    }

    public String getImage() {
        return image;
    }
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
