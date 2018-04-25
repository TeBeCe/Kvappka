package com.example.martin.login;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Martin on 12. 4. 2018.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, ProfileActivity.class);

        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean langPref = getPreference.getBoolean("allowNotifyPref", false);
          Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(langPref), Toast.LENGTH_SHORT);
          toast.show();
        if (android.os.Build.VERSION.SDK_INT > 16) {
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(SplashActivity.class);
            taskStackBuilder.addNextIntent(notificationIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                CharSequence name = "Channel 1 ";
                String description = "Channel 1";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel("ididid", name, importance);
                mChannel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "ididid")
                    .setSmallIcon(R.drawable.logoplus)
                    .setContentTitle(getApplicationContext().getResources().getString(R.string.notification7DaysTitle))
                    .setContentText(getApplicationContext().getResources().getString(R.string.notification7DaysContent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            mBuilder.setAutoCancel(true);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(69,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            mBuilder.setContentIntent(pendingIntent);
            /*Notification.Builder builder = new Notification.Builder(context);
            Notification notification = builder.setAutoCancel(true)
                    .setContentTitle("DEMO")
                    .setContentText("test test test")
                    .setTicker("DEmo")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);*/
            notificationManager.notify(69,mBuilder.build());

        }
    }
}
