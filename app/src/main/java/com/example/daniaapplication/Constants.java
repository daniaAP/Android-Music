package com.example.daniaapplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final HashMap<String, List<String>> SONGS;
    static {
        SONGS = new HashMap<>();
        SONGS.put("Amy", Arrays.asList("Song 1", "Song 2", "Song 3", "Song 4"));
        SONGS.put("The Beatles", Arrays.asList("Song 1", "Song 2", "Song 3", "Song 4"));
    }

}
