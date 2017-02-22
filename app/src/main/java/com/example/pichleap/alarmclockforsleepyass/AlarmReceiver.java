package com.example.pichleap.alarmclockforsleepyass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pichleap on 12/16/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("In Alarm Receiver", "Yay!");

        String getExtraString = intent.getExtras().getString("extra");
        Log.e("Key is ", getExtraString);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        // pass the extra string from main activity to the RingtonePlayingService
        serviceIntent.putExtra("extra", getExtraString);

        context.startService(serviceIntent);


    }
}
