package com.example.martin.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Martin on 10. 5. 2018.
 */

public class LogOutDisposeData {
    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor mEditor;
    String LOGGED_IN = "loggedIn";
    String USER_ID = "userId";
    String USER_NAME ="userName";
    String EMAIL ="email";
    String GENDER ="gender";
    String BIRTHDATE = "birthDate";
    String BLOODGROUP ="bloodGroup";

    public LogOutDisposeData(Context context){
        this.context = context;
    }

    public void makeDispose(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = sharedPreferences.edit();
        mEditor.putBoolean(LOGGED_IN,false);
        mEditor.putString(USER_ID,"null");
        mEditor.putString(USER_NAME,"null");
        mEditor.putString(EMAIL,"null");
        mEditor.putString(GENDER,"null");
        mEditor.putString(BIRTHDATE,"null");
        mEditor.putString(BLOODGROUP,"null");
        mEditor.apply();
    }

}
