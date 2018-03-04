package com.example.martin.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        MessageReceiver receiver = new MessageReceiver(new Message());

        Intent intent = new Intent(this , TimerService.class);
        intent.putExtra("time",10);
        intent.putExtra("receiver",receiver);
        startService(intent);
    }
    public class Message{
        public void displayMessage(int resultCode,Bundle resultData){
                String message = resultData.getString("message");
            Toast.makeText(ServiceActivity.this,resultCode+" "+message,Toast.LENGTH_SHORT).show();
        }
    }
}

