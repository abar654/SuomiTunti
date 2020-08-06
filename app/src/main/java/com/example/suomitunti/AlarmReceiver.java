package com.example.suomitunti;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOT_CODE = 1;
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Alarm going off!");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create a channel if required
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.channel_name),
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.channel_description));
            notificationManager.createNotificationChannel(channel);
        }

        //TODO: Include the time frame in the description (should be sent in the intent)

        //Set the content of the notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, context.getString(R.string.channel_name))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(context.getString(R.string.alarm_not_title))
                        .setContentText(context.getString(R.string.alarm_not_message))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        //Create an intent for the MainActivity
        Intent resultIntent = new Intent(context, MainActivity.class);

        //Create the backstack for the activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        //Get the pending intent WITH backstack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOT_CODE, notificationBuilder.build());

        //Set the next alarm
        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
        alarmScheduler.setAlarm();
    }
}
