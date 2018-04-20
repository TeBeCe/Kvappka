package com.example.martin.login;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.preference.PreferenceActivity;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    Button btn;
    String Json;
    ListView list;
    String myJSON;
    TextFieldsClass textFieldsClass;

    private static final String TAG_RESULTS="users";
    private static final String TAG_ID = "id";
    private static final String TAG_FNAME = "first_name";
    private static final String TAG_LNAME = "last_name";
    private static final String TAG_ADD ="email";

    static Context context;
    static   Intent refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refresh = new Intent(this, SettingsActivity.class);

        context = getBaseContext();
        Fragment fragment = new SettingsScreen();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState==null){
            fragmentTransaction.add(R.id.setting_activity_content,fragment,"settings_fragment");
            fragmentTransaction.commit();

        }
        else
            fragment = getFragmentManager().findFragmentByTag("settings_fragment");
        //list = (ListView) findViewById(R.id.listView);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView
                .setNavigationItemSelectedListener(this);

        View header=navigationView
                .getHeaderView(0);

        textFieldsClass = new TextFieldsClass();
        textFieldsClass.setNames(header, context, 4, navigationView);
       // findViewById(R.id.button4).setOnClickListener(this);
      // personList = new ArrayList<HashMap<String,String>>();
       // getData();
    }





    public static class SettingsScreen extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);
        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            //Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
           // toast.show();

            if(s.equals("languagePref")) {
                String langPref = sharedPreferences.getString("languagePref", "xxx");
                if (langPref.equals("0")) {
                    changeLang("sk");
                } else {
                    changeLang("en");
                }
            }

        }
        public void changeLang(String lang) {
            if (lang.equalsIgnoreCase(""))
                return;

            Locale myLocale = new Locale(lang);
            Locale.setDefault(myLocale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = myLocale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(refresh);

        }
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        System.out.println(parent.getItemAtPosition(position));
        // DO SOMETHING or in your case
        //startActivity(new Intent(<the correct intent>);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the profile action
            Intent myintent = new Intent(this,
                    ProfileActivity.class);
            startActivity(myintent);

        } else if (id == R.id.nav_posts) {

        } else if (id == R.id.nav_friends) {
            // Handle the profile action
            Intent myintent = new Intent(this,
                    FriendsActivity.class);
            startActivity(myintent);

        } else if (id == R.id.nav_donations) {
            Intent myintent = new Intent(this,
                    DonationsActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_settings) {
            Intent myintent = new Intent(this,
                    SettingsActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_places){
            Intent myintent = new Intent(this,
                    PlacesActivity.class);
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.button4:

                startActivity(intent);*/
        }
    }
}
