package com.example.suomitunti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Generate the time for today
        int suomiTuntiHour = TimeFromDateGenerator.generateStartHourForDay(Calendar.getInstance());

        // Set the time as text in the activity
        TextView timeDisplay = findViewById(R.id.main_time_display);
        timeDisplay.setText(suomiTuntiHour + ":00");
        Log.d(TAG, "Updating time display");

        // Set up the notification switch
        final SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), Context.MODE_PRIVATE);
        final Switch notifSwitch = findViewById(R.id.notification_switch);

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Set the text and its color
                if(isChecked) {
                    notifSwitch.setText(getString(R.string.notif_switch_enabled));
                    notifSwitch.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                } else {
                    notifSwitch.setText(getString(R.string.notif_switch_disabled));
                    notifSwitch.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.disabledGrey));
                }

                // Save the state in preferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(getString(R.string.notif_enabled_pref_id), isChecked);
                editor.apply();

                // Make sure that the alarms update
                AlarmScheduler alarmScheduler = new AlarmScheduler(MainActivity.this);
                alarmScheduler.setAlarm();
            }
        });
        notifSwitch.setChecked(preferences.getBoolean(getString(R.string.notif_enabled_pref_id), true));

        // Make sure the notifications are set to go off
        AlarmScheduler alarmScheduler = new AlarmScheduler(this);
        alarmScheduler.setAlarm();
    }

}
