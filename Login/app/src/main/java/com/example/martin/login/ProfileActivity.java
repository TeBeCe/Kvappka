package com.example.martin.login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Calendar lastDon;
    private PopulateMedals populateMedals;
    private TextView nameView, emailView, profileName, profileBornView, profileBloodView, profileEmailView;
    TextView badgeLvl1Num,badgeLvl2Num,badgeLvl3Num,badgeLvl4Num,badgeLvl5Num;
    TextView badgeLvl1Date,badgeLvl2Date,badgeLvl3Date,badgeLvl4Date,badgeLvl5Date;
    String userId;
    TextView countDonations,lastDonation,daysToDonate;
    private ImageView headerImg;
    private int[] manBadges;
    private int[] womanBadges;
    private List<Calendar> donationList = new ArrayList<>();
    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_drawer);
        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = getPreference.getString("userId","null");
        imageViewProfile = findViewById(R.id.circularImage);


        badgeLvl1Num = (TextView) findViewById(R.id.badgeLvl1Number);
        badgeLvl2Num = (TextView) findViewById(R.id.badgeLvl2Number);
        badgeLvl3Num = (TextView) findViewById(R.id.badgeLvl3Number);
        badgeLvl4Num = (TextView) findViewById(R.id.badgeLvl4Number);
        badgeLvl5Num = (TextView) findViewById(R.id.badgeLvl5Number);

        badgeLvl1Date = (TextView) findViewById(R.id.badgeLvl1Date);
        badgeLvl2Date = (TextView) findViewById(R.id.badgeLvl2Date);
        badgeLvl3Date = (TextView) findViewById(R.id.badgeLvl3Date);
        badgeLvl4Date = (TextView) findViewById(R.id.badgeLvl4Date);
        badgeLvl5Date = (TextView) findViewById(R.id.badgeLvl5Date);

        countDonations =(TextView) findViewById(R.id.cardViewTxtLeftUp);
        lastDonation = (TextView)findViewById(R.id.cardViewTxtMiddleUp);
        daysToDonate = (TextView)findViewById(R.id.cardViewTxtRightUp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView
                .setNavigationItemSelectedListener(this);
        navigationView.getMenu()
                .getItem(0)
                .setChecked(true);
        View header = navigationView
                .getHeaderView(0);
        notificationPosts();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        //Json = postData("");
        //contactList = decodeJson(Json);
        nameView = (TextView) header.findViewById(R.id.nameDrawer1);
        emailView = (TextView) header.findViewById(R.id.emailDrawer1);
        headerImg = (ImageView) header.findViewById(R.id.circularImage);

        nameView.setText(getPreference.getString("userName","error_no_name"));
        emailView.setText(getPreference.getString("email","error_no_mail"));
        Intent intent = getIntent();
        profileEmailView = (TextView) findViewById(R.id.profileEmail);
        profileBornView = (TextView) findViewById(R.id.profileBornDate);
        profileBloodView = (TextView) findViewById(R.id.profileBloodType);
        profileName = (TextView) findViewById(R.id.profileFullName);

        profileName.setText(getPreference.getString("userName","error_no_name"));
        profileEmailView.setText(getPreference.getString("email","error_no_mail"));
        String time = getPreference.getString("birthDate","0");
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(Long.valueOf(time));
        profileBornView.setText(format1.format(calendar.getTime()));
        profileBloodView.setText(getPreference.getString("bloodGroup","error_no_blood_group"));

        Context context = getApplicationContext();
        populateMedals = new PopulateMedals(context);
        volleyGetDonationsToPopulate(getPreference.getString("id","null"));
        //notification();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(context.openFileInput("myProfile"));
            headerImg.setImageBitmap(bitmap);
            imageViewProfile.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void notificationPosts(){
        Intent nIntent = new Intent(ProfileActivity.this,AlarmReceiver.class);
        Calendar calenderPost = Calendar.getInstance();
        calenderPost.set(Calendar.HOUR_OF_DAY,11);
        calenderPost.set(Calendar.MINUTE,30);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 96, nIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calenderPost.getTimeInMillis(),1000*60*60*24, pi);
    }

    public void notification(Calendar notifyCalendar){
        Intent nIntent = new Intent(ProfileActivity.this,AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notifyCalendar = Calendar.getInstance();
        notifyCalendar.add(Calendar.MONTH,2);
        notifyCalendar.add(Calendar.DAY_OF_YEAR,23);
        notifyCalendar.set(Calendar.HOUR_OF_DAY, 9);
        System.out.println(notifyCalendar.getTime().toString());

        PendingIntent pi = PendingIntent.getBroadcast(this, 69, nIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notifyCalendar.getTimeInMillis(), pi);
    }

    public  void  volleyGetDonationsToPopulate(final String id) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/donations/"+userId;
        final StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            response = "{donations:" + response + "}";
                            JSONObject jObject = new JSONObject(response);
                            JSONArray jArray = jObject.getJSONArray("donations");
                            for (int i=0; i < jArray.length(); i++)
                            {
                                try {
                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    // Pulling items from the array
                                    String oneObjectsItem = oneObject.getString("location");
                                    String oneObjectsItem2 = oneObject.getString("date");
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTimeInMillis(Long.valueOf(oneObjectsItem2));
                                    donationList.add(cal);

                                } catch (JSONException e) {
                                    // Oops
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                        populateMedals.populateMedals(badgeLvl1Num,badgeLvl2Num,badgeLvl3Num,badgeLvl4Num
                                ,badgeLvl5Num,badgeLvl1Date,badgeLvl2Date,badgeLvl3Date,badgeLvl4Date,badgeLvl5Date,
                                daysToDonate,lastDonation,countDonations,donationList);
                        notification(populateMedals.getLastDonate());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
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
            Intent myintent = new Intent(this,
                    PostsActivity.class);
            startActivity(myintent);
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
        } else if (id == R.id.nav_places){
            System.out.println("places");
            Intent myintent = new Intent(this,
                    PlacesActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_info) {
            Intent myIntent = new Intent(this,
                    InfoActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            LogOutDisposeData logD = new LogOutDisposeData(this);
            logD.makeDispose();
            Intent logOut = new Intent(this,
                    LoginActivity.class);
            logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //mEditor.putBoolean("loggedIn",true);
            //mEditor.apply();
            startActivity(logOut);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

}
