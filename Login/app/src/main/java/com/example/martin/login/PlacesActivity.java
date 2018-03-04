package com.example.martin.login;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlacesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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
        ArrayList<LatLng> markers = new ArrayList<>();
        // Add a marker in ba and move the camera

        LatLng ba = new LatLng(48.167364,17.091355);

        LatLng tt = new LatLng(48.367908 ,17.590564);
        markers.add(tt);
        LatLng bb = new LatLng(48.744545 ,19.116564);
        markers.add(bb);
        LatLng nz = new LatLng(47.988541 ,18.155937);
        markers.add(nz);
        LatLng ma = new LatLng(49.057437 ,18.918192);
        markers.add(ma);
        LatLng nr = new LatLng(48.300996,18.08516);
        markers.add(nr);
        LatLng za = new LatLng(49.21874,18.74573);
        markers.add(za);
        LatLng ke = new LatLng(48.722368,21.237848);
        markers.add(ke);
        for (LatLng marker: markers) {
            mMap.addMarker(new MarkerOptions().position(marker));
        }


        mMap.addMarker(new MarkerOptions().position(ba).title("Marker in Bratislava"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ba));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ba,11f));
    }
}
