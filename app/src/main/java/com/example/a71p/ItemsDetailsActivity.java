package com.example.a71p;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemsDetailsActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private TextView textViewLostOrFound;
    private TextView textViewName;
    private TextView textViewNumber;
    private TextView textViewDescription;
    private TextView textViewDate;
    private TextView textViewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        dbHelper = new MyDatabaseHelper(this);

        // Get the item id from the Intent
        Intent intent = getIntent();
        long itemId = intent.getLongExtra("itemId", -1);

        // Query the database for the item with the given id
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
               "lostOrFound",
                "name",
                "number",
                "description",
                "date",
                "location"
        };
        String selection = "_id" + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            //Get the value of the all elements from the id
            String lostOrFound = cursor.getString(cursor.getColumnIndexOrThrow("lostOrFound"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));

            // Display the relevant data in the activity
            textViewLostOrFound = findViewById(R.id.type);
            textViewName = findViewById(R.id.name);
            textViewNumber = findViewById(R.id.number);
            textViewDescription = findViewById(R.id.description);
            textViewDate = findViewById(R.id.date);
            textViewLocation = findViewById(R.id.location);

            textViewLostOrFound.setText("Type: " + lostOrFound);
            textViewName.setText("Name: " + name);
            textViewNumber.setText("Number: " + number);
            textViewDescription.setText("Description: " + description);
            textViewDate.setText("Date: " + date);
            textViewLocation.setText("Location: " + location);
        } else {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

        //Create button to delete item
        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the item from the database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int deletedRows = db.delete(MyDatabaseHelper.TABLE_NAME, selection, selectionArgs);


                Toast.makeText(ItemsDetailsActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();


                // Return to the previous activity
                Intent intent = new Intent(ItemsDetailsActivity.this, ShowList.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}

