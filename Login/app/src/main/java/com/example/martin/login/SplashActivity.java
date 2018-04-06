package com.example.martin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Martin on 14. 7. 2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        String langPref = getPreference.getString("languagePref", "xxx");
        Toast toast = Toast.makeText(getApplicationContext(), langPref, Toast.LENGTH_SHORT);
        //toast.show();

        if (langPref.equals("0")) {
            changeLang("sk");
        } else {
            changeLang("en");
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
}