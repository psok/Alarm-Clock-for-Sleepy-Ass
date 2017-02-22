package com.example.pichleap.alarmclockforsleepyass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // make an alarm manager
    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView updateText;
    Context context;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        // initialize our alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // initialize our time picker
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        //initialize our text update box
        updateText = (TextView) findViewById(R.id.update_text);

        // create an instance of calendar
        final Calendar calendar = Calendar.getInstance();

        // create intent to the Alarm Receiver class
        final Intent alarmIntent = new Intent(this.context, AlarmReceiver.class);

        // initialize alarm on button
        Button alarmOn = (Button) findViewById(R.id.alarm_on);

        // Create an onClick listener to start the alarm
        alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set calendar instance from the hour and minute we picked from the time picker
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                String stringHour = String.valueOf(hour);
                String stringMinute = String.valueOf(minute);
                String suffix = "AM";
                if(hour > 12) {
                    stringHour = String.valueOf(hour - 12);
                    suffix = "PM";
                }

                if(minute < 10) {
                    stringMinute = "0" + String.valueOf(minute);
                }

                setAlarmText("Alarm is set to " + stringHour + ":" + stringMinute + " " + suffix);

                // put extra string into alarmIntent
                // tells the clock that you pressed the alarm on
                alarmIntent.putExtra("extra", "alarm on");

                // create a pending intent to delay the alarm intent until the specific calendar time
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        // initialize alarm off button
        Button alarmOff = (Button) findViewById(R.id.alarm_off);
        // Create an onClick listener to stop the alarm
        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmText("alarm off!");

                // cancel the alarm
                alarmManager.cancel(pendingIntent);

                // put extra string into alarmIntent
                // tells the clock that you pressed the alarm off
                alarmIntent.putExtra("extra", "alarm off");

                // stop the ringtone
                sendBroadcast(alarmIntent);
            }
        });

    }

    private void setAlarmText(String text) {
        updateText.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
