package com.example.daniaapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

    public class IntentService extends android.app.IntentService {
        private static final String CHANNEL_ID = "CHANNEL_3";
        private static final int REQUEST_CODE = 1;
        private static final int NOTIFICATION_ID = 1;

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public IntentService(String name) {
            super(name);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            //phase 1
            createNotificationChannel();
            Notification.Builder nBuilder = new Notification.Builder(this);
            nBuilder.setSmallIcon(R.drawable.musicicon);
            nBuilder.setContentTitle("Come stream songs on Android Music!");
            nBuilder.setContentText("This is your daily reminder to listen to your favorite songs.");

            // what to do when the notification is clicked
            Intent intent1 = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            // connects the pending intent with the notification object,
            //when the user clicks on the notification the pending intent is called
            nBuilder.setContentIntent(pendingIntent);

            nBuilder.setChannelId(CHANNEL_ID);

            Notification notification = nBuilder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            managerCompat.notify(NOTIFICATION_ID, notification);

        }



        private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.app_name);
                String description = getString(R.string.hello_blank_fragment);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }