package com.example.a71p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //Initilises buttons and database to use
    private Button createAdvert;
    private Button showList;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Accesses the database
        dbHelper = new MyDatabaseHelper(this);

        //Creates a button that takes you to the create post activity
        createAdvert = findViewById(R.id.createButton);
        createAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAdvert.class);
            startActivity(intent);
        });

        //Creates a button that takes you to the show post activity
        showList = findViewById(R.id.showButton);
        showList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShowList.class);
            startActivity(intent);
        });


    }


}