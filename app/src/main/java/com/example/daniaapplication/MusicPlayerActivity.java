package com.example.daniaapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MusicPlayerActivity extends AppCompatActivity implements OnSongClickListener {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private RecyclerView songsRecyclerView;
    ImageView imageViewAlbumArt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        imageViewAlbumArt = findViewById(R.id.album_art);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        songsRecyclerView = findViewById(R.id.songs_recycler_view);

//
        String albumName = getIntent().getStringExtra("album");
        loadAlbum(albumName);



        // Assuming you have a Song model and have initialized a List<Song> somewhere
        List<Song> songList = new ArrayList<>(); // Populate this with your songs
//        songList.add(new Song("Song 1", "Amy", R.drawable.amy));
//        songList.add(new Song("Song 2", "Amy", R.drawable.amy));
//        songList.add(new Song("Song 3", "Amy", R.drawable.amy));
//        songList.add(new Song("Song 4", "Amy", R.drawable.amy));
//        songList.add(new Song("Song 5", "Amy", R.drawable.amy));

        // Set up the RecyclerView
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongsAdapter adapter = new SongsAdapter(songList);
        songsRecyclerView.setAdapter(adapter);
    }

    private void loadAlbum(String albumName) {
        DatabaseReference dbRef = mDatabase.getReference("Albums");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Map> rawAlbums = (List<Map>)snapshot.getValue();
                for(Map rawAlbum: rawAlbums){
                    final String currentAlbumName = rawAlbum.get("name").toString();
                    if(currentAlbumName.equals(albumName)){
                        loadAlbumArt(rawAlbum);

                        loadSongs(rawAlbum);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAlbumArt(Map rawAlbum) {
        int resId = Utilities.imageToResId(MusicPlayerActivity.this, rawAlbum.get("image").toString());
        imageViewAlbumArt.setImageResource(resId);
    }

    private void loadSongs(Map rawAlbum) {
        List<Map> rawSongs = (List<Map>) rawAlbum.get("songs");
        int resId = Utilities.imageToResId(MusicPlayerActivity.this, rawAlbum.get("image").toString());
        List<Song> songList = new ArrayList<>(); // Populate this with your songs
        for(Map rawSong: rawSongs){

            Song song = new Song(
                    rawSong.get("name").toString(),
                    rawAlbum.get("artist").toString(),
                    resId,
                    rawSong.get("file").toString()
            );

            songList.add(song);
        }
        // Set up the RecyclerView
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongsAdapter adapter = new SongsAdapter(songList);
        adapter.setListener(MusicPlayerActivity.this);
        songsRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onSongClick(Song s) {
        Toast.makeText(this, "Song click", Toast.LENGTH_SHORT).show();
        //TODO play music
    }

    @Override
    public void onSongLongClick(Song s) {
        final String uid = mAuth.getCurrentUser().getUid().toString();
        mDatabase.getReference("Playlists").child(uid).push().setValue(s);

        Toast.makeText(this, "Song added to Playlist", Toast.LENGTH_SHORT).show();

    }
}
