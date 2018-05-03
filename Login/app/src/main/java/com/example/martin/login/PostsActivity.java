package com.example.martin.login;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    private ListView postsListView;
    private TextFieldsClass textFieldsClass;
    private ArrayList<HashMap<String, String>> postsList;
    private HashMap<String, String> post;
    private List<PostEntity> postList = new ArrayList<>();;
    private ImageButton btn;
    private RecyclerView recyclerView;
    private PostsListAdapter mAdapter;
    private String nameUser,content,bloodGroupUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_posts);
        setSupportActionBar(toolbar);
        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        nameUser = getPreference.getString("userName","null");
        bloodGroupUser = getPreference.getString("bloodGroup","nul");
        recyclerView = (RecyclerView)findViewById(R.id.postsListRecycleView);

        btn = (ImageButton)findViewById(R.id.toolbar_button_add);
        btn.setOnClickListener(this);

        Context context = getApplicationContext();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView
                .setNavigationItemSelectedListener(this);
        View header = navigationView
                .getHeaderView(0);

        textFieldsClass = new TextFieldsClass();
        textFieldsClass.setNames(header, context, 1, navigationView);

        /*PostEntity pE = new PostEntity();
        PostEntity pE2 = new PostEntity();
        pE.setContent("Lorem Ipsum Lorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem "
                + "IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem "
                + "IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem "
                + "IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem "
                + " https://android-developers.googleblog.com/2008/03/linkify-your-text.html Ipsum");
        pE.setDate(Calendar.getInstance());
        pE.setName("Martin Bachraty");
        pE.setId(0);
        postList.add(pE);
        pE2.setDate(Calendar.getInstance());
        pE2.setName("John Deer");
        pE2.setContent("Kratky content");
        pE2.setId(1);
        postList.add(pE2);*/
        volleyGetDonations();
        mAdapter = new PostsListAdapter(this,postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = "Remind to donate ";
            String description = "Channel to remind to donate";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel("ididid", name, importance);
            mChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "ididid")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("titel")
                .setContentText("context")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        //notificationManager.notify(70, mBuilder.build());

        Intent nIntent = new Intent(PostsActivity.this,AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar notifyCalendar = Calendar.getInstance();
        notifyCalendar.add(Calendar.SECOND, 10);

        PendingIntent pi = PendingIntent.getBroadcast(this, 69, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notifyCalendar.getTimeInMillis(), pi);

        }
    }
    public void addPost(){
        final Calendar addPostCal = new GregorianCalendar().getInstance();
        final EditText taskEditText = new EditText(this);

        taskEditText.setLines(2);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new post")
                .setMessage("Write your request here")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PostEntity pE = new PostEntity();
                        content = String.valueOf(taskEditText.getText());
                        volleyAddPost(content,"1",String.valueOf(addPostCal.getTimeInMillis()));
                        pE.setContent(content);
                        pE.setName(nameUser);
                        pE.setDate(addPostCal);
                        mAdapter.addItem(pE);
                        postList = mAdapter.returnList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public  void  volleyAddPost(final String content,final String id,final String datex) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/posts/add";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_user","1");
                params.put("content", content);
                params.put("date", datex);
                params.put("blood_group", bloodGroupUser);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }
    public  void  volleyGetDonations() {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/posts";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            response = "{posts:" + response + "}";
                            JSONObject jObject = new JSONObject(response);
                            JSONArray jArray = jObject.getJSONArray("posts");
                            for (int i=0; i < jArray.length(); i++)
                            {
                                try {
                                    JSONObject oneObject = jArray.getJSONObject(i);
                                    // Pulling items from the array
                                    String contentJson = oneObject.getString("content");
                                    String dateJson = oneObject.getString("date");
                                    String nameJson = oneObject.getString("first_name");
                                    String bloodGroupJson = oneObject.getString("blood_group");
                                    String idJson = oneObject.getString("id");
                                    Calendar cal = Calendar.getInstance();
                                    PostEntity pE = new PostEntity();
                                    pE.setName(nameJson);
                                    pE.setContent(contentJson);
                                    pE.setId(idJson);
                                    cal.setTimeInMillis(Long.valueOf(dateJson));
                                    pE.setDate(cal);
                                    pE.setBloodGroup(bloodGroupJson);
                                    mAdapter.addItem(pE);
                                    postList = mAdapter.returnList();

                                } catch (JSONException e) {
                                    // Oops
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
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
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.toolbar_button_add:{
                addPost();
                break;
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            Intent logOut = new Intent(PostsActivity.this,LoginActivity.class);
            logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logOut);
            finish();
        } else if (id == R.id.nav_info) {
            Intent myIntent = new Intent(this,
                    InfoActivity.class);
            startActivity(myIntent);
        }else if (id == R.id.nav_places){
            Intent myintent = new Intent(this,
                    PlacesActivity.class);
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
