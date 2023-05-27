package com.example.a71p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvert extends AppCompatActivity implements LocationListener, PlaceSelectionListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 000001;
    private LocationManager locationManager;

    // Creates EditText values to be used
    private RadioGroup lostFoundRadioGroup;
    private EditText nameEditText;
    private EditText numbersEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private AutocompleteSupportFragment locationAutocompleteFragment;
    private String selectedLocation;
    private Double lat;
    private Double lon;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        PlacesClient placesClient = Places.createClient(this);

        //Looks at location permissions
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permissions are not granted, request them
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        // Sets up use of database
        dbHelper = new MyDatabaseHelper(this);

        // Connects user clickable to variable names
        lostFoundRadioGroup = findViewById(R.id.lost_found_radio_group);
        nameEditText = findViewById(R.id.name_edit_text);
        numbersEditText = findViewById(R.id.numbers_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);

        //Sets up autocomplete fragment for use
        locationAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.location_auto_complete_fragment);

        locationAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        locationAutocompleteFragment.setOnPlaceSelectedListener(this);
        //Sets on place selected listener for when user selects a location
        locationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Get all important information from location
                selectedLocation = place.getName();
                LatLng LatLng = place.getLatLng();
                lat = LatLng.latitude;
                lon = LatLng.longitude;

            }

            @Override
            public void onError(Status status) {
                Log.i("CreateAdvert", "An error occurred: " + status);
            }
        });

        //Set up get current location button
        Button getLocationButton = findViewById(R.id.get_location_button);
        getLocationButton.setOnClickListener(v -> {
            //Gets last known location from gps
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Gets lat and long from said location
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                //Uses lat and long to get the name of that location
                String locationName = getLocationName(lat, lon);
                //If a name is found set that to be the name shown and held in variables
                if (locationName != null) {
                    locationAutocompleteFragment.setText(locationName);
                    selectedLocation = locationName;

                } else {
                    //Show error message
                    Toast.makeText(CreateAdvert.this, "Unable to get location name", Toast.LENGTH_SHORT).show();
                }
            } else {
                //Show error message
                Toast.makeText(CreateAdvert.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for the submit button
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            String lostOrFound;
            // Checks if one of the radio buttons has been pressed, if not returns an empty string
            try {
                int selectedId = lostFoundRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                lostOrFound = radioButton.getText().toString();
            } catch (Exception e) {
                lostOrFound = "";
            }

            // Returns all values of all inputs into strings
            String name = nameEditText.getText().toString().trim();
            String number = numbersEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            String location = selectedLocation;
            Double latitude = lat;
            Double longitude = lon;

            // Checks if any of the values are empty, if so does not continue
            if (lostOrFound.isEmpty() || name.isEmpty() || number.isEmpty() || description.isEmpty() || date.isEmpty() || selectedLocation.isEmpty()) {
                Toast.makeText(CreateAdvert.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {

                // Adds new values to the database
                dbHelper.addNewItem(lostOrFound, name, number, description, date, location, latitude, longitude);

                // Notifies the user that the values have been added
                Toast.makeText(CreateAdvert.this, "Item reported", Toast.LENGTH_SHORT).show();

                //Resets lat and lon
                lat = null;
                lon = null;

                // Ends the activity
                finish();
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        // Handle location updates
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changes
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled
    }

    //Is used to get a location name from a latitude and longitude
    private String getLocationName(double latitude, double longitude) {
        //Sets up a geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            //Uses geocoder to get a list of addresses from the lat and long, list will only have a length of 1 max
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            //If an address can be found return that address and the locality
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onError(@NonNull Status status) {

    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {

    }
}

