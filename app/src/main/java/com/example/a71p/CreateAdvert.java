package com.example.a71p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateAdvert extends AppCompatActivity {

    //Creates Edit text values to be used
    private RadioGroup lostFoundRadioGroup;
    private EditText nameEditText;
    private EditText numbersEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private EditText locationEditText;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        //Sets up use of database
        dbHelper = new MyDatabaseHelper(this);

        //Connects user clickables to variable names
        lostFoundRadioGroup = findViewById(R.id.lost_found_radio_group);
        nameEditText = findViewById(R.id.name_edit_text);
        numbersEditText = findViewById(R.id.numbers_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);

        // Set a click listener for the submit button
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            String lostOrFound;
            //Checks if one of the radiobuttons has been pressed if not returns an empty string
            try {
                int selectedId = lostFoundRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                lostOrFound = radioButton.getText().toString();
            } catch (Exception e) {
                lostOrFound = "";
            }


            //Returns all values of all inputs into strings
            String name = nameEditText.getText().toString().trim();
            String number = numbersEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();

            //Checks if any of the values are empty if so does not continue
            if (lostOrFound.isEmpty()|| name.isEmpty() || number.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                Toast.makeText(CreateAdvert.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {

                //Adds new values to the database
                dbHelper.addNewItem(lostOrFound, name, number, description, date, location);

                //Notifies the user that the values have been added
                Toast.makeText(CreateAdvert.this, "Item reported", Toast.LENGTH_SHORT).show();

                //Ends the activity
                finish();
            }
        });
    }
}
