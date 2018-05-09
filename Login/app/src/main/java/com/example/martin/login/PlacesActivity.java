package com.example.martin.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlacesActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener {

    LocationManager locationManager;
    TextView dayOfWeek, name, address, mail, webLink;
    private GoogleMap mMap;
    private int day;
    TextFieldsClass textFieldsClass;
    JSONArray jsonArray;
    ArrayList<LatLng> markersArrayList = new ArrayList<>();
    JSONObject tappedMarker;
    TextView fromD1, fromD2, fromD3, fromD4, fromD5;
    TextView toD1, toD2, toD3, toD4, toD5, toD6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        volleyGetPlaces();
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
        textFieldsClass.setNames(header, context, 4, navigationView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day < 7 && day > 1) {
            dayOfWeek = (TextView) findViewById(getResources().getIdentifier("openedTimeTableDay" + String.valueOf(day - 1), "id", getPackageName()));
            dayOfWeek.setTextColor(getResources().getColor(R.color.mainRed));
        }
        name = (TextView) findViewById(R.id.placesTxtName);
        address = (TextView) findViewById(R.id.placesTxtAddress);
        mail = (TextView) findViewById(R.id.placesTxtEmail);
        webLink = (TextView) findViewById(R.id.placesTxtWebSite);
        fromD1 = (TextView) findViewById(R.id.openedTimeFrom1);
        fromD2 = (TextView) findViewById(R.id.openedTimeFrom2);
        fromD3 = (TextView) findViewById(R.id.openedTimeFrom3);
        fromD4 = (TextView) findViewById(R.id.openedTimeFrom4);
        fromD5 = (TextView) findViewById(R.id.openedTimeFrom5);
        toD1 = (TextView) findViewById(R.id.openedTimeTo1);
        toD2 = (TextView) findViewById(R.id.openedTimeTo2);
        toD3 = (TextView) findViewById(R.id.openedTimeTo3);
        toD4 = (TextView) findViewById(R.id.openedTimeTo4);
        toD5 = (TextView) findViewById(R.id.openedTimeTo5);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    //Color.parseColor("#bdbdbd");

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService((LOCATION_SERVICE));
        System.out.println("loc");
        mMap.setOnMarkerClickListener(this);

        ArrayList<LatLng> markers = new ArrayList<>();
        // Add a marker in ba and move the camera

       /* LatLng ba = new LatLng(48.167364, 17.091355);

        LatLng tt = new LatLng(48.367908, 17.590564);
        markers.add(tt);
        LatLng bb = new LatLng(48.744545, 19.116564);
        markers.add(bb);
        LatLng nz = new LatLng(47.988541, 18.155937);
        markers.add(nz);
        LatLng ma = new LatLng(49.057437, 18.918192);
        markers.add(ma);
        LatLng nr = new LatLng(48.300996, 18.08516);
        markers.add(nr);
        LatLng za = new LatLng(49.21874, 18.74573);
        markers.add(za);
        LatLng ke = new LatLng(48.722368, 21.237848);
        markers.add(ke);
        for (LatLng marker : markersArrayList) {
            mMap.addMarker(new MarkerOptions().position(marker));
        }
        */
        // mMap.addMarker(new MarkerOptions().position(ba).title("Marker in Bratislava"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.744545, 19.116564), 7.0f));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast toast = Toast.makeText(getApplicationContext(), "Perm", Toast.LENGTH_SHORT);
            toast.show();
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 123);
            }

            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    double latitude = location.getLatitude();

                    double longitude = location.getLongitude();

                    LatLng latlng = new LatLng(latitude, longitude);
                    //instatnce geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        System.out.println(str);
                        mMap.addMarker(new MarkerOptions().position(latlng).title(str));
                        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,11f));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {
           /* Toast toast = Toast.makeText(getApplicationContext(), "GPS2", Toast.LENGTH_SHORT);
            toast.show();*/

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    double latitude = location.getLatitude();

                    double longitude = location.getLongitude();

                    LatLng latlng = new LatLng(latitude, longitude);

                    //Geocoder geocoder = new Geocoder(getApplicationContext());

                    //  try {
                       /* List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latlng).title(str));*/
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,11f));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    // System.out.println(str);
                    //    } catch (IOException e) {
                    //        e.printStackTrace();
                    //    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });

        }
        mMap.setMyLocationEnabled(true);
    }

    public void volleyGetPlaces() {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/places";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jObject = new JSONObject(response);
                            jsonArray = jObject.getJSONArray("places");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject oneObject = jsonArray.getJSONObject(i);
                                    // Pulling items from the array
                                    Double latitudeObj = oneObject.getDouble("latitude");
                                    Double longtitudeObj = oneObject.getDouble("longitude");
                                    markersArrayList.add(new LatLng(latitudeObj, longtitudeObj));

                                } catch (JSONException e) {
                                    // Oops
                                }
                            }
                            for (LatLng marker : markersArrayList) {
                                mMap.addMarker(new MarkerOptions().position(marker));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123)
            for (int res : grantResults) {

            }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast toast = Toast.makeText(getApplicationContext(), marker.getId(), Toast.LENGTH_SHORT);
        toast.show();
        int position = Integer.valueOf(marker.getId().substring(1, marker.getId().length()));
        try {
            tappedMarker = jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        populatePlace(tappedMarker);

        return true;
    }


    public void populatePlace(JSONObject jsonObject) {
        try {
            JSONArray openingH = jsonObject.getJSONArray("opening_hours");
            name.setText(jsonObject.getString("name"));
            address.setText(jsonObject.getString("address"));
            mail.setText(jsonObject.getString("web"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude")), 13.0f));
            JSONObject day = openingH.getJSONObject(0);
            fromD1.setText(day.getString("open_time"));
            toD1.setText(day.getString("close_time"));
            day = openingH.getJSONObject(1);
            fromD2.setText(day.getString("open_time"));
            toD2.setText(day.getString("close_time"));
            day = openingH.getJSONObject(2);
            fromD3.setText(day.getString("open_time"));
            toD3.setText(day.getString("close_time"));
            day = openingH.getJSONObject(3);
            fromD4.setText(day.getString("open_time"));
            toD4.setText(day.getString("close_time"));
            day = openingH.getJSONObject(4);
            fromD5.setText(day.getString("open_time"));
            toD5.setText(day.getString("close_time"));

            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.167364, 17.091355), 8.0f));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.settings, menu);
        return true;
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
            Intent myintent = new Intent(this, DonationsActivity.class);
            startActivity(myintent);

        } else if (id == R.id.nav_settings) {
            Intent myintent = new Intent(this,
                    SettingsActivity.class);
            startActivity(myintent);

        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            Intent logOut = new Intent(PlacesActivity.this,LoginActivity.class);
            logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logOut);
            finish();
        } else if (id == R.id.nav_info) {
            Intent myIntent = new Intent(this,
                    InfoActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_places) {
            System.out.println("places");
            Intent myintent = new Intent(this, PlacesActivity.class);
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
