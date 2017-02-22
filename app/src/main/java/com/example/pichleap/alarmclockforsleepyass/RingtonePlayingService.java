package com.example.pichleap.alarmclockforsleepyass;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Pichleap on 12/16/16.
 */
public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("LocalService", "Received start id " + startId + ":" + intent);

        // fetch extra string value
        String state = intent.getExtras().getString("extra");
        Log.e("Rington state is ", state);


        assert state != null;

        if(state.equals("alarm on")) {
            Log.e("Rington state is ", state);
            this.startId = 1;
        } else if(state.equals("alarm off")) {
            Log.e("Rington state is ", state);
            this.startId = 0;
        } else {
            this.startId = 0;
        }


        Log.e("Start id is :" + this.startId, "");
        // if there is no music playing and user pressed "alarm on"
        // ringtone should start playing
        if(!this.isRunning && this.startId == 1) {
            Log.e("there is no music ", "alarm on");
            // create an instance of media player
            mediaPlayer = MediaPlayer.create(this, R.raw.iphoneremix);
            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;

            // notification
            // set up the notification service
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // set up an intent that goes to the main activity
            Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
            // set up pending intent
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

            // make notification parameters
            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setContentIntent(pending_intent_main_activity)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setAutoCancel(true)
                    .build();

            // set up the notification call command
            notificationManager.notify(0, notification_popup);
        }

        // if there is music playing and user pressed "alarm off"
        // rington should stop playing
        else if (this.isRunning && this.startId == 0) {
            Log.e("there is music ", "alarm off");
            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;

        }

        // if user pressed any other random buttons
        // if there is no music playing and user pressed "alarm off"
        // do nothing
        else if (!this.isRunning && this.startId == 0) {
            this.isRunning = false;
            this.startId = 0;
        }

        // if there is music playing and user pressed "alarm on"
        // do nothing
        else if (this.isRunning && this.startId == 1) {
            this.isRunning = true;
            this.startId = 1;
        }

        // other possible cases
        else {

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }
}
