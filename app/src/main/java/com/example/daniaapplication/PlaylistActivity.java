package com.example.daniaapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
public class PlaylistActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, PlayingFragment.PlayingFragmentListener {
    private ListView listView;//display
    private ArrayList<Song> songs;//DATA
    private ArrayAdapter<Song> arrayAdapter;//Adapter
    private MediaPlayer player;
    private PlayingFragment fragment;
    private Song playing;
    private Button playPause, shuffle;
    private int index;
    private SeekBar seekBar;
    private TextView place, duration;
    private Handler mHandler = new Handler();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

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
        fragment.setSong(String.valueOf(song.getImage()), song.getName());
        index = position;
        fragment.changeIcon(true);
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
        Toast.makeText(this, "onItemLongClick", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     *
     */
    @Override
    public void next() {
        Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    @Override
    public void prev() {
        Toast.makeText(this, "prev", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    @Override
    public void playPause() {
        Toast.makeText(this, "play", Toast.LENGTH_SHORT).show();
    }


    public void loadPlaylist(){
        final String uid = mAuth.getCurrentUser().getUid().toString();
        mDatabase.getReference("Playlists").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Map> songList = (List<Map>) snapshot.getValue();
                songs = new ArrayList<>();
                int index = 0;
                for (Map rawSong : songList) {
                    int resId = Utilities.imageToResId(PlaylistActivity.this, rawSong.get("image").toString());
                    Song song = new Song(rawSong.get("name").toString(),
                            rawSong.get("artist").toString(),
                            resId,
                            rawSong.get("file").toString());
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

}

