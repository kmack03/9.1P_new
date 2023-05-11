package com.example.a71p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity {

    //Create variables to use to manipulate the activity
    private ListView listView;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        //Connect variable to database
        dbHelper = new MyDatabaseHelper(this);

        listView = findViewById(R.id.listView);

        // Get all items from the database
        ArrayList<String> itemList = new ArrayList<>();
        ArrayList<Long> itemIdList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            //Print the values that show the item the best
            long itemId = cursor.getLong(cursor.getColumnIndex("_id"));
            String lostOrFound = cursor.getString(cursor.getColumnIndex("lostOrFound"));
            String name = cursor.getString(cursor.getColumnIndex("name"));

            String item = "Type: " + lostOrFound + "\nItem name: " + name;

            itemList.add(item);
            //Create list of all ids that will match with the position of the item
            itemIdList.add(itemId);
        }
        cursor.close();

        // Set up the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        //When item clicked go to another activity that shows the item in more detail
        listView.setOnItemClickListener((parent, view, position, id) -> {
            long itemId = itemIdList.get(position);
            Intent intent = new Intent(ShowList.this, ItemsDetailsActivity.class);
            intent.putExtra("itemId", itemId);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
