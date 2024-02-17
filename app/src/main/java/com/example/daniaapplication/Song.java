package com.example.daniaapplication;

import java.io.Serializable;

public class Song implements Comparable<Song>, Serializable {
    private String name;
    private String artist;
    private String file;
    private Integer image;

    private String key;

    public Song(String name, String artist, Integer image) {
        this.name = name;
        this.artist = artist;
        this.image = image;
    }

    public Song(String name, String artist, Integer image, String file) {
        this(name, artist, image);
        this.file = file;
    }
    public Song(String name, String artist, Integer image, String file, String key) {
        this(name, artist, image, file);
        this.key = key;

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

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int compareTo(Song o) {
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

    public String getDate() {
        return null;
    }

    public String getAlbum() {
        return null;
    }

    public String getTime() {
        return null;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
