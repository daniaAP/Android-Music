package com.example.daniaapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
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

import android.os.Handler;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnAlbumClickListener {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int MESSAGE_DELAY = 5000; //  5 seconds

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
    private int importance;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance();
        loadAlbums();
        initElements();
        Intent i = getIntent();
        // removed name and image
        //user = new User("image","name","email", "pass");
        playlist = findViewById(R.id.playlist);

        feedback = findViewById(R.id.feedback);


        Optional<User> optionalUser = Optional.ofNullable(user);
        optionalUser.map(User::toString);
        playlist.setOnClickListener(this);
        feedback.setOnClickListener(this);
        // Set up a handler to send a message after  5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code will execute after  5 seconds
                Toast.makeText(HomeActivity.this, "listen to your favorite artists", Toast.LENGTH_SHORT).show();
            }
        }, MESSAGE_DELAY);
        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel for my notifications";
            importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(channel);
        }

        // Schedule the alarm to trigger  5 seconds after the app is opened
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);
    }


    public class MyAlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Build and display the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notification Title")
                    .setContentText("Notification Text")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
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

//        swNetworkConnected = findViewById(R.id.swNetworkConnected);
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
     * @param album
     */
    @Override
    public void onAlbumClick(Album album) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra("album", album.getName());
        startActivity(intent);
    }


}