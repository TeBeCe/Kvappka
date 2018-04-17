package com.example.martin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.share.Share;

import java.util.Locale;

/**
 * Created by Martin on 14. 7. 2017.
 */

public class SplashActivity extends AppCompatActivity {
    private TextFieldsClass textFieldsClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        String langPref = getPreference.getString("languagePref", "xxx");
       // Toast toast = Toast.makeText(getApplicationContext(), langPref, Toast.LENGTH_SHORT);
        //toast.show();

        if (langPref.equals("0")) {
            changeLang("sk");
        } else {
            changeLang("en");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("onBoardBoolean",MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean("alreadyOnBoarded",true);
        mEditor.apply();
        sharedPreferences = getSharedPreferences("onBoardBoolean",MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("alreadyOnBoarded",false)){
            Toast toast = Toast.makeText(getApplicationContext(), sharedPreferences.getBoolean("alreadyOnBoarded",false)+"", Toast.LENGTH_SHORT);
            toast.show();

            Intent intent1 = new Intent(this, OnBoardingActivity.class);
            startActivity(intent1);
            finish();
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void setNotificationReminderDonate(){
        textFieldsClass = new TextFieldsClass();

        //textFieldsClass.getClosestPossibleDonation();


    }
}