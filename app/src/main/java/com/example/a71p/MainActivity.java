package com.example.a71p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates a button that takes you to the create post activity
        Button createAdvert = findViewById(R.id.createButton);
        createAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAdvert.class);
            startActivity(intent);
        });

        //Creates a button that takes you to the show post activity
        Button showList = findViewById(R.id.showButton);
        showList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShowList.class);
            startActivity(intent);
        });

        //Creates a button that takes you to show map activity
        Button showMap = findViewById(R.id.showMap);
        showMap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });

    }


}