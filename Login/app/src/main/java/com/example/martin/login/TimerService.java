package com.example.martin.login;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Zuzka on 15.07.2017.
 */

public class TimerService extends IntentService {

    public TimerService(){
        super("Timer Service");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("timer","Timer zacal");
    }
    @Override
public int onStartCommand(Intent intent,int flags,int startId){
    super.onStartCommand(intent,flags,startId);

    return START_STICKY;
}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       if (intent==null) {
           int time = 5;
           for (int i = 0;i<time; i++){
               Log.v("timer","i (intent is null) = " + i);
               try {
                   Thread.sleep(100);
               } catch (Exception e) {

               }
           }

           NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
           nb.setContentText("Timer done.");
           nb.setContentTitle("Hi!");
           nb.setSmallIcon(R.mipmap.ic_menu_friends);
           NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
           nm.notify(1,nb.build());
           return;
       }
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        int time = intent.getIntExtra("time",0);
        for(int i =0;i<time;i++){

            Log.v("timer","i (intent is not null) = " + i);
            try {
                Thread.sleep(1000);
            }
            catch (Exception e){

            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("message","Counting done...");
        receiver.send(1234,bundle);
    }
}
