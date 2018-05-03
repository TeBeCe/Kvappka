package com.example.martin.login;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DonationsActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, NavigationView.OnNavigationItemSelectedListener {
    private Calendar mCalendar;
    private int year, month, day;
    private ImageButton btn;
    private RecyclerView recyclerView;
    private DonationsListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private TextFieldsClass textFieldsClass;
    private TextView emptyView, donationsText, averageDonationsText;
    private Context contextApp;
    private String[] listItems;
    private List<DonationsEntity> donationsEntityList = new ArrayList<>();;
    private SimpleDateFormat format1= new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        Toolbar toolbar = findViewById(R.id.toolbar_donate);
        emptyView = findViewById(R.id.emptyView);
        setSupportActionBar(toolbar);

        listItems = getResources().getStringArray(R.array.donationPlacesArray);
        mCalendar = Calendar.getInstance();

        donationsText = (TextView) findViewById(R.id.noOfDonations);
        averageDonationsText = (TextView) findViewById(R.id.averageDonations);
        btn = (ImageButton) findViewById(R.id.toolbar_button_add);
        btn.setOnClickListener(this);

        Context context = getApplicationContext();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        contextApp = getApplicationContext();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView
                .setNavigationItemSelectedListener(this);
        View header = navigationView
                .getHeaderView(0);
        textFieldsClass = new TextFieldsClass();
        textFieldsClass.setNames(header, context, 3, navigationView);

        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        day = mCalendar.get(Calendar.DAY_OF_MONTH);


        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        Calendar c1 = GregorianCalendar.getInstance();
        Calendar c2 = GregorianCalendar.getInstance();
        Calendar c3 = GregorianCalendar.getInstance();
        Calendar c4 = GregorianCalendar.getInstance();
        Calendar c5 = GregorianCalendar.getInstance();
        Calendar c6 = GregorianCalendar.getInstance();
        Calendar c7 = GregorianCalendar.getInstance();
        Calendar c8 = GregorianCalendar.getInstance();
        Calendar c9 = GregorianCalendar.getInstance();
        c3.set(2013, Calendar.JANUARY, 25);
        c2.set(2012, Calendar.OCTOBER, 24);
        c1.set(2012, Calendar.MARCH, 28);
        c4.set(2013, Calendar.AUGUST, 26);
        c5.set(2015, Calendar.MARCH, 2);
        c6.set(2016, Calendar.JULY, 21);
        c7.set(2016, Calendar.NOVEMBER, 3);
        c8.set(2017, Calendar.AUGUST, 7);
        c9.set(2017, Calendar.NOVEMBER, 6);

        DonationsEntity dE1 = new DonationsEntity();
        DonationsEntity dE2 = new DonationsEntity();
        DonationsEntity dE3 = new DonationsEntity();
        DonationsEntity dE4 = new DonationsEntity();
        DonationsEntity dE5 = new DonationsEntity();
        DonationsEntity dE6 = new DonationsEntity();
        DonationsEntity dE7 = new DonationsEntity();
        DonationsEntity dE8 = new DonationsEntity();
        DonationsEntity dE9 = new DonationsEntity();

        dE1.setCalendar(c1);
        dE1.setPlace("Trnava");
        dE2.setCalendar(c2);
        dE2.setPlace("Trnava");
        dE3.setCalendar(c3);
        dE3.setPlace("Trnava");
        dE4.setCalendar(c4);
        dE4.setPlace("Trnava");
        dE5.setCalendar(c5);
        dE5.setPlace("Trnava");
        dE6.setCalendar(c6);
        dE6.setPlace("Trnava");
        dE7.setCalendar(c7);
        dE7.setPlace("Trnava");
        dE8.setCalendar(c8);
        dE8.setPlace("Trnava");
        dE9.setCalendar(c9);
        dE9.setPlace("Trnava");


        volleyGetDonations("1");
       /* donationsEntityList.add(dE1);
        donationsEntityList.add(dE2);
        donationsEntityList.add(dE3);
        donationsEntityList.add(dE4);
        donationsEntityList.add(dE5);
        donationsEntityList.add(dE6);
        donationsEntityList.add(dE7);
        donationsEntityList.add(dE8);
        donationsEntityList.add(dE9);*/

        Collections.sort(donationsEntityList,Collections.reverseOrder());

        mAdapter = new DonationsListAdapter(this, donationsEntityList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

       /* if (donationsEntityList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {*/
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        //}

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        List<String> statsList = new ArrayList<>();
        statsList = getStats(donationsEntityList);
        //donationsText.setText(statsList.get(1));
        //avarageDonationsText.setText(statsList.get(0));

    }

    public List<String> getStats(List<DonationsEntity> donationsEntityList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        if (donationsEntityList.size() == 0) {
            mEditor.putLong("lastDonationDate", sharedPreferences.getLong("birthDateLong", 0));
        } else {
            mEditor.putLong("lastDonationDate", donationsEntityList.get(0).getCalendar().getTimeInMillis());
        }
        mEditor.putInt("numberOfDonations", donationsEntityList.size());
        mEditor.apply();

        Calendar birthDate = new GregorianCalendar().getInstance();
        birthDate.setTimeInMillis(sharedPreferences.getLong("birthDateLong", 0));
        Toast toast = Toast.makeText(getApplicationContext(), birthDate.getTime().toString(), Toast.LENGTH_LONG);
        toast.show();
        birthDate.add(Calendar.YEAR, 18);
        toast = Toast.makeText(getApplicationContext(), birthDate.getTime().toString(), Toast.LENGTH_LONG);
        toast.show();
        Calendar today = new GregorianCalendar().getInstance();
        int diffYear = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
        int timesPossible = diffMonth / 3;
        float averageDonationPerYear = donationsEntityList.size() / (float) (timesPossible / 4);
        List<String> populateDonationStatistic = new ArrayList<>();
        populateDonationStatistic.add("" + averageDonationPerYear);
        populateDonationStatistic.add(donationsEntityList.size() + "");
        averageDonationsText.setText(String.valueOf(averageDonationPerYear));
        donationsText.setText(String.valueOf(donationsEntityList.size()));
        return populateDonationStatistic;
    }

    @Override
    public void onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(DonationsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        final Calendar calendarik = new GregorianCalendar();
                        calendarik.set(Calendar.YEAR, year);
                        calendarik.set(Calendar.MONTH, monthOfYear);
                        calendarik.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        if (donationsEntityList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }

                        AlertDialog.Builder mBuilder = new AlertDialog
                                .Builder(DonationsActivity.this);
                        mBuilder.setTitle(R.string.dialogTitleDonationPlace);
                        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), listItems[i],
                                        Toast.LENGTH_LONG).show();
                        DonationsEntity tempDe = new DonationsEntity();
                        tempDe.setCalendar(calendarik);
                        tempDe.setPlace(listItems[i]);
                        mAdapter.addItem(tempDe);
                        donationsEntityList = mAdapter.returnList();
                        getStats(donationsEntityList);
                        volleyAddDonation(listItems[i],"1",String.valueOf(calendarik.getTimeInMillis()));
                        dialogInterface.dismiss();
                            }
                        });
                        mBuilder.show();
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getWindow();
        datePickerDialog.show();
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DonationsListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = format1.format(donationsEntityList.get(viewHolder.getAdapterPosition())
                    .getCalendar().getTime());
            //String name = "testTODO";

            // backup of removed item for undo purpose
            final DonationsEntity deletedItem = donationsEntityList.get(viewHolder
                    .getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + getString(R.string.donationsListRemoved), Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.setDuration(10000);
            snackbar.show();
        }
    }

    public  void  volleyAddDonation(final String location,final String id,final String datex) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/donation/add";
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
                params.put("id","1");
                params.put("location", location);
                params.put("date", datex);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    public  void  volleyGetDonations(final String id) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/donations/"+id;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
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
                                    DonationsEntity de = new DonationsEntity();
                                    de.setPlace(oneObjectsItem);
                                    cal.setTimeInMillis(Long.valueOf(oneObjectsItem2));
                                    de.setCalendar(cal);
                                    mAdapter.addItem(de);
                                    donationsEntityList = mAdapter.returnList();

                                } catch (JSONException e) {
                                    // Oops
                                }
                            }
                            getStats(donationsEntityList);
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
            Intent logOut = new Intent(this,
                    LoginActivity.class);
            logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logOut);
            finish();
        } else if (id == R.id.nav_info) {
            Intent myIntent = new Intent(this,
                    InfoActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_places) {
            Intent myintent = new Intent(this,
                    PlacesActivity.class);
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

