package com.example.martin.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Zuzka on 15.07.2017.
 */
@SuppressLint("ParcelCreator")
public class MessageReceiver extends ResultReceiver {
   private ServiceActivity.Message message;

    public MessageReceiver(ServiceActivity.Message message){
        super(new Handler());

        this.message = message;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        message.displayMessage(resultCode, resultData);

    }
}

