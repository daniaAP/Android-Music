package com.example.daniaapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, MediaPlayer.OnCompletionListener, PlayingFragment.PlayingFragmentListener, SeekBar.OnSeekBarChangeListener
public class PlaylistActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener,
        PlayingFragment.PlayingFragmentListener {
    private ListView listView;//display
    private ArrayList<Song> songs;//DATA
    private ArrayAdapter<Song> arrayAdapter;//Adapter
    private MediaPlayer mediaPlayer;
    private PlayingFragment fragment;
    private Song playing;
    private Button playPause, shuffle;
    private int index;
    private SeekBar seekBar;
    private TextView place, duration;
    private Handler mHandler = new Handler();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        loadPlaylist();

        fragment = (PlayingFragment) getSupportFragmentManager().findFragmentById(R.id.frPlaying);
    }

    /**
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = (Song) listView.getItemAtPosition(position);
        fragment.setSong(String.valueOf(song.getImage()), song);
        index = position;

        loadAndPlaySong(song);
    }

    private void loadAndPlaySong(Song song) {
        int songResId = Utilities.songToResId(PlaylistActivity.this, song.getFile());
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            this.isPlaying = false;
        }
        try {
            mediaPlayer = MediaPlayer.create(this, songResId);
        }catch (Resources.NotFoundException ex){
            Toast.makeText(this, "Song not found", Toast.LENGTH_SHORT).show();
            return;
        }

        playPause(song);
    }

    /**
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Song song = arrayAdapter.getItem(position);
        final String uid = mAuth.getCurrentUser().getUid().toString();

        mDatabase.getReference("Playlists").child(uid).child(song.getKey()).removeValue();
        loadPlaylist();
        return true;
    }

    /**
     *
     */
    @Override
    public void next() {
        if (index == arrayAdapter.getCount() - 1) {
            return;
        }
        Song song = (Song) listView.getItemAtPosition(++index);
        fragment.setSong(String.valueOf(song.getImage()), song);

        loadAndPlaySong(song);
    }

    /**
     *
     */
    @Override
    public void prev() {
        if (index == 0) {
            return;
        }
        Song song = (Song) listView.getItemAtPosition(--index);
        fragment.setSong(String.valueOf(song.getImage()), song);

        loadAndPlaySong(song);
    }

    /**
     *
     */
    @Override
    public void playPause(Song song) {
        isPlaying = !isPlaying;
        fragment.changeIcon(isPlaying);

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.pause();
            Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
        }


    }


    public void loadPlaylist() {
        final String uid = mAuth.getCurrentUser().getUid().toString();
        mDatabase.getReference("Playlists").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Map> songList = (Map<String, Map>) snapshot.getValue();
                songs = new ArrayList<>();
                for (String key : songList.keySet()) {
                    Map<String, String> rawSong = songList.get(key);
                    int resId = Utilities.imageToResId(PlaylistActivity.this, String.valueOf(rawSong.get("image")));
                    Song song = new Song(rawSong.get("name").toString(),
                            rawSong.get("artist").toString(),
                            resId,
                            rawSong.get("file") != null ? rawSong.get("file").toString() : "",
                            key);
                    songs.add(song);
                }
                arrayAdapter = new SongArrayAdapter(PlaylistActivity.this, R.layout.custom_row, songs);
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
}

