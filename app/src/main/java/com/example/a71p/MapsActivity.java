package com.example.a71p;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.a71p.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Creates variables for use in map
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Is used to get map ready to view
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        dbHelper = new MyDatabaseHelper(this);

        //Gets a list of the latitudes and longitudes from database
        ArrayList<double[]> latLngList = dbHelper.getLatitudeLongitude();
        //Creates a new list to separate the values
        double[] latitudes = new double[latLngList.size()];
        double[] longitudes = new double[latLngList.size()];

        //Separates latitudes and longitudes into their own lists
        for (int i = 0; i < latLngList.size(); i++) {
            latitudes[i] = latLngList.get(i)[0];
            longitudes[i] = latLngList.get(i)[1];
        }

        dbHelper.close();


        // Add markers for each location
        for (int i = 0; i < latitudes.length; i++) {
            LatLng latLng = new LatLng(latitudes[i], longitudes[i]);
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }
}