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
        Boolean notifyPref = getPreference.getBoolean("allowNotifyPref", false);
          Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(notifyPref), Toast.LENGTH_SHORT);
          toast.show();

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(SplashActivity.class);
            taskStackBuilder.addNextIntent(notificationIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                CharSequence nameReminder = "Remind to donate";
                String descriptionReminder = "Channel for reminder to donate";
                int importanceReminder = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannelReminder = new NotificationChannel("ReminderChannel", nameReminder, importanceReminder);
                mChannelReminder.setDescription(descriptionReminder);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManagerReminder = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManagerReminder.createNotificationChannel(mChannelReminder);

                CharSequence namePosts = "Remind to donate";
                String description = "Channel for reminder to donate";
                int importancePosts = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannelPosts = new NotificationChannel("PostChannel", namePosts, importancePosts);
                mChannelPosts.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManagerPosts = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManagerPosts.createNotificationChannel(mChannelPosts);
            }

            NotificationCompat.Builder mBuilderPost = new NotificationCompat.Builder(context,"PostChannel")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(getApplicationContext().getResources().getString(R.string.notificationPostTitle))
                    .setContentText(getApplicationContext().getResources().getString(R.string.notificationPostContent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            mBuilderPost.setAutoCancel(true);
            PendingIntent pendingIntentPost = taskStackBuilder.getPendingIntent(96,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManagerCompat notificationManagerPost = NotificationManagerCompat.from(context);
            mBuilderPost.setContentIntent(pendingIntentPost);
            if(notifyPref)
            notificationManagerPost.notify(96,mBuilderPost.build());

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "ReminderChannel")
                    .setSmallIcon(R.drawable.logoplus)
                    .setContentTitle(getApplicationContext().getResources().getString(R.string.notification7DaysTitle))
                    .setContentText(getApplicationContext().getResources().getString(R.string.notification7DaysContent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            mBuilder.setAutoCancel(true);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(69,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            mBuilder.setContentIntent(pendingIntent);
            if(notifyPref)
            notificationManager.notify(69,mBuilder.build());


    }
}
