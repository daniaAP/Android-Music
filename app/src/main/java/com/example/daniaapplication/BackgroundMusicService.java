package com.example.daniaapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusicService extends Service {
    //create service(to get inheritance and manifest written): app> new> service> service

    MediaPlayer player;

    public BackgroundMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        player = MediaPlayer.create( this, R.raw.beatlesgolden);
        player.setLooping( true );
        player.start();

        return START_STICKY; //try playing again if ended by mistake
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

}