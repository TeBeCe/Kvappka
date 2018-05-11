package com.example.martin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import com.facebook.share.Share;

import java.util.Locale;

/**
 * Created by Martin on 14. 7. 2017.
 */

public class SplashActivity extends AppCompatActivity {
    private TextFieldsClass textFieldsClass;
    private SharedPreferences getPreference;
    private SharedPreferences.Editor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mEditor = getPreference.edit();
        String langPref = getPreference.getString("languagePref", "xxx");

        if (langPref.equals("0")) {
            changeLang("sk");
        } else {
            changeLang("en");
        }

        if(!getPreference.getBoolean("alreadyOnBoarded",false)){
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
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}