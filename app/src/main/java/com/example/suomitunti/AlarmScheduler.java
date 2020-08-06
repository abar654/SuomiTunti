package com.example.suomitunti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class AlarmScheduler extends Service {

    private AlarmManager mAlarmManager;
    private PendingIntent mNotifyPendingIntent;
    private Context mContext;

    private static final int NOTIFICATION_INTENT_CODE = 1;
    private static final String TAG = "AlarmScheduler";

    public AlarmScheduler(Context context) {
        super();
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        mNotifyPendingIntent = PendingIntent.getBroadcast(mContext, NOTIFICATION_INTENT_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void setAlarm() {

        // Check if notifications are enabled
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.preferences_filename), Context.MODE_PRIVATE);
        if(!preferences.getBoolean(mContext.getString(R.string.notif_enabled_pref_id), true)) {
            // Deactivate the alarm and return so we do not set a new one
            if(mNotifyPendingIntent != null) {
                Log.d(TAG, "Canceling alarms");
                mAlarmManager.cancel(mNotifyPendingIntent);
            }
            return;
        }

        // Calculate what time the notification should go off today
        Calendar notificationTime = Calendar.getInstance();
        int suomiTuntiHour = TimeFromDateGenerator.generateStartHourForDay(notificationTime);
        notificationTime.set(Calendar.MILLISECOND, 0);
        notificationTime.set(Calendar.SECOND, 0);
        notificationTime.set(Calendar.MINUTE, 0);
        notificationTime.set(Calendar.HOUR_OF_DAY, suomiTuntiHour);

        // If the notification should have already gone off today then set for tomorrow
        if(notificationTime.compareTo(Calendar.getInstance()) < 0) {
            // Set for tomorrow
            notificationTime.roll(Calendar.DATE, true);
            suomiTuntiHour = TimeFromDateGenerator.generateStartHourForDay(notificationTime);
            notificationTime.set(Calendar.HOUR_OF_DAY, suomiTuntiHour);
        }

        // Create the alarm for the next notification
        Long alarmTime = notificationTime.getTimeInMillis();
        Log.d(TAG, "Setting alarm for: " + alarmTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, mNotifyPendingIntent);
        } else {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, mNotifyPendingIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
