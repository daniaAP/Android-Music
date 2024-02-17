package com.example.daniaapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//after implements ,
public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAlbumClickListener {

    Context context;
    Activity activity;
    Switch swNetworkConnected;

    IntentFilter filter;

    InternetConnectionReceiver internetConnectionReceiver;
    IntentFilter intentConnectionFilter;

    private Button playlist, feedback;
    private User user;
    private RecyclerView recyclerView;//display
    private ArrayList<Album> albums;//DATA
    private ArrayAdapter<Album> arrayAdapter;//Adapter
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initElements();


        Intent intent = getIntent();
        // removed name and image
        //user = new User("image","name","email", "pass");
        playlist = findViewById(R.id.playlist);
        feedback = findViewById(R.id.feedback);


        loadAlbums();

//        albumList.add(new Album("Album 1", "ABBA ", R.drawable.abba));
//        albumList.add(new Album("Album 2", "Fine Line", R.drawable.fineline));
//        albumList.add(new Album("Album 3", "Harry's House", R.drawable.harryshouse));
//        albumList.add(new Album("Album 4", "Micheal jackson", R.drawable.michealjackson));
//        albumList.add(new Album("Album 5", "Midnight Memories", R.drawable.midnightmemories));
//        albumList.add(new Album("Album 6", "Queen", R.drawable.queen));
//        albumList.add(new Album("Album 7", "Taylor Swift", R.drawable.taylorswift));
//        albumList.add(new Album("Album 8", "Avril Lavigne ", R.drawable.avrillavegne));

        Optional<User> optionalUser = Optional.ofNullable(user);
        optionalUser.map(User::toString);
        playlist.setOnClickListener(this);
        feedback.setOnClickListener(this);

    }

    private void loadAlbums() {
        DatabaseReference dbRef = mDatabase.getReference("Albums");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Album> albumList = new ArrayList<>();
                List<Map> rawAlbums = (List<Map>) snapshot.getValue();
                for (Map rawAlbum : rawAlbums) {
                    int imageResId = Utilities.imageToResId(HomeActivity.this, rawAlbum.get("image").toString());
                    Album album = new Album(rawAlbum.get("name").toString(),
                            rawAlbum.get("artist").toString(),
                            imageResId);
                    albumList.add(album);
                }
                HomeAdapter adapter = new HomeAdapter(HomeActivity.this, albumList);

                recyclerView = findViewById(R.id.recycler_view_playlist);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initElements() {
        activity = HomeActivity.this;
        context = HomeActivity.this;

        swNetworkConnected = findViewById(R.id.swNetworkConnected);
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        internetConnectionReceiver = new InternetConnectionReceiver();
        intentConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }


    protected void onStart() {
        super.onStart();
        registerReceiver(internetConnectionReceiver, intentConnectionFilter);

    }


    protected void onStop() {
        super.onStop();
        unregisterReceiver(internetConnectionReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v == playlist) {
            Intent intent = new Intent(HomeActivity.this, PlaylistActivity.class);
            startActivity(intent);
        } else if (v == feedback) {
            Toast.makeText(this, "works", Toast.LENGTH_LONG).show();
            sendFeedback();
        }

    }

    public void sendFeedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set a title for alert dialog
        builder.setTitle("Enjoying Android Music?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(i);
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Display the alert dialog on interface
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.myAccount) {
            Intent intent = new Intent(this, MyAccountActivity.class);
            //intent.putExtra("user",user);
            startActivity(intent);
        }
        if (id == R.id.Music) {
            String text = item.getTitle().toString();
            Intent svc = new Intent(this, BackgroundMusicService.class);
            if (text.equals("Play Music")) {
                startService(svc);
                item.setTitle("Stop Music");
            } else {
                stopService(svc);
                item.setTitle("Play Music");
            }
        }
        if (id == R.id.sign) {
            Intent intent = new Intent(this, SignupActivity.class);
            mAuth.signOut();
            startActivity(intent);
            finish();
        }
        return true;

    }

    /**
     * @param s
     */
    @Override
    public void onAlbumClick(Album album) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra("album", album.getName());
        startActivity(intent);
    }


}