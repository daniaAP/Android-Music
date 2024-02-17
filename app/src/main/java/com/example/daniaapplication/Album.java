package com.example.daniaapplication;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

class Album implements Comparable<Album>, Serializable {
    private String name;
    private String artist;
    private Integer image;
    private ArrayList<Song> songs;

    public Album() {
    }

    public Album(String name, String artist, Integer image) {
        this.artist = artist;
        this.image = image;
        this.name = name;
        songs = new ArrayList<Song>();
    }

    public void addSong(String name) {
        songs.add(new Song(name, this.artist, this.image));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", image='" + image + '\'' +
                ", songs=" + songs +
                '}';
    }

    @Override
    public int compareTo(Album o) {
        int i = 0;
        while (i < this.artist.length() && i < o.getArtist().length()) {
            if (this.artist.charAt(i) > o.getArtist().charAt(i))
                return 1;
            else if (this.artist.charAt(i) < o.getArtist().charAt(i))
                return -1;
            i++;
        }
        return 0;
    }
}
