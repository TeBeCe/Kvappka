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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

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

public class DonationsActivity extends AppCompatActivity implements View.OnClickListener,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, NavigationView.OnNavigationItemSelectedListener{
    Calendar mCalendar;
    int year,month,day;
    ImageButton btn;
    private RecyclerView recyclerView;
    private List<Calendar> calendarList;
    private DonationsListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private TextFieldsClass textFieldsClass;
    private TextView emptyView,donationsText,avarageDonationsText;
    private Context contextApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        Toolbar toolbar = findViewById(R.id.toolbar_donate);
        emptyView = findViewById(R.id.emptyView);
        setSupportActionBar(toolbar);

        mCalendar = Calendar.getInstance();
        donationsText = (TextView)findViewById(R.id.noOfDonations);
        avarageDonationsText = (TextView)findViewById(R.id.averageDonations);
        btn = (ImageButton)findViewById(R.id.toolbar_button_add);
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
        textFieldsClass.setNames(header,context,3,navigationView);

        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        day = mCalendar.get(Calendar.DAY_OF_MONTH);


        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        calendarList = new ArrayList<>();
        Calendar c1 = GregorianCalendar.getInstance();
        Calendar c2 = GregorianCalendar.getInstance();
        Calendar c3 = GregorianCalendar.getInstance();
        Calendar c4 = GregorianCalendar.getInstance();
        Calendar c5 = GregorianCalendar.getInstance();
        Calendar c6 = GregorianCalendar.getInstance();
        Calendar c7 = GregorianCalendar.getInstance();
        Calendar c8 = GregorianCalendar.getInstance();
        Calendar c9 = GregorianCalendar.getInstance();
        c1.set(2018,Calendar.JANUARY,15);
        c2.set(2019,Calendar.JANUARY,15);
        c3.set(2012,Calendar.MARCH,28);
        c4.set(2013,Calendar.MARCH,28);
        c5.set(2015,Calendar.MARCH,28);
        c6.set(2014,Calendar.MARCH,28);
        c7.set(2016,Calendar.MARCH,28);
        c8.set(2017,Calendar.MARCH,28);
        c9.set(2012,Calendar.DECEMBER,28);
        calendarList.add(c1);
        calendarList.add(c2);
        calendarList.add(c3);
        calendarList.add(c4);
        calendarList.add(c5);
        calendarList.add(c6);
        calendarList.add(c7);
        calendarList.add(c8);
        calendarList.add(c9);

        Collections.sort(calendarList,Collections.reverseOrder());
       // calendarList = new ArrayList<>();
        mAdapter = new DonationsListAdapter(this, calendarList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        if (calendarList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }



        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        List<String> statsList = new ArrayList<>();
        statsList = getStats(calendarList);
        donationsText.setText(statsList.get(1));
        avarageDonationsText.setText(statsList.get(0));

    }
    public List<String> getStats(List<Calendar> calendarList){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEditor = sharedPreferences.edit();

        mEditor.putLong("lastDonationDate",calendarList.get(0).getTimeInMillis());
        mEditor.putInt("numberOfDonations",calendarList.size());

        mEditor.apply();
            Calendar birthdate = new GregorianCalendar().getInstance();
            birthdate.set(1994,Calendar.MARCH,28);
            birthdate.set(Calendar.YEAR,2012);

            Calendar today = new GregorianCalendar().getInstance();
            int diffYear = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + today.get(Calendar.MONTH) - birthdate.get(Calendar.MONTH);
            int timesPossible = diffMonth/3;
            float averageDonationPerYear = calendarList.size()/(float)(timesPossible/4);
            //Toast.makeText(getApplicationContext(),"time"+ calendarList.size() + "possible" + timesPossible/4 + "averagePerYr" + averageDonationPerYear ,Toast.LENGTH_LONG).show();
            List<String> populateDonationStatistic  = new ArrayList<>();
            populateDonationStatistic.add(""+averageDonationPerYear);
            populateDonationStatistic.add(calendarList.size()+"");

        return  populateDonationStatistic;
    }
    @Override
    public void onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(DonationsActivity.this,
                                                                    new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                Calendar calendarik = new GregorianCalendar();
                calendarik.set(Calendar.YEAR,year);
                calendarik.set(Calendar.MONTH,monthOfYear);
                calendarik.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                mAdapter.addItem(calendarik);
                calendarList =  mAdapter.returnList();
                if (calendarList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        },year,month,day);
        Toast.makeText(getApplicationContext(),"xxx",Toast.LENGTH_LONG).show();
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
            //String name = calendarList.get(viewHolder.getAdapterPosition()).get;
            String name = "testTODO";

            // backup of removed item for undo purpose
            final Calendar deletedItem = calendarList.get(viewHolder.getAdapterPosition());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the profile action
            Intent myintent = new Intent(this,
                    ProfileActivity.class);
            startActivity(myintent);

        }
        else if (id == R.id.nav_posts) {
            Intent myintent = new Intent(this,
                    PostsActivity.class);
            startActivity(myintent);
        }
        else if (id == R.id.nav_friends) {
            // Handle the profile action
            Intent myintent = new Intent(this,
                    FriendsActivity.class);
            startActivity(myintent);

        }
        else if (id == R.id.nav_donations) {
            Intent myintent = new Intent(this,DonationsActivity.class);
            startActivity(myintent);
        }
        else if (id == R.id.nav_settings) {
            Intent myintent = new Intent(this,
                    SettingsActivity.class);
            startActivity(myintent);
        }
        else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_places){
            System.out.println("places");
            Intent myintent = new Intent(this,PlacesActivity.class);
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

