package com.example.daniaapplication;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MusicPlayerActivity extends AppCompatActivity {

    private RecyclerView songsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songsRecyclerView = findViewById(R.id.songs_recycler_view);

        // Assuming you have a Song model and have initialized a List<Song> somewhere
        List<Song> songList = new ArrayList<>(); // Populate this with your songs
        songList.add(new Song("Song 1", "Amy", R.drawable.amy));
        songList.add(new Song("Song 2", "Amy", R.drawable.amy));
        songList.add(new Song("Song 3", "Amy", R.drawable.amy));
        songList.add(new Song("Song 4", "Amy", R.drawable.amy));
        songList.add(new Song("Song 5", "Amy", R.drawable.amy));

        String artist = getIntent().getStringExtra("album");
        List<String> songs = Constants.SONGS.get(artist);


        // Set up the RecyclerView
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongsAdapter adapter = new SongsAdapter(songList);
        songsRecyclerView.setAdapter(adapter);
    }
}
