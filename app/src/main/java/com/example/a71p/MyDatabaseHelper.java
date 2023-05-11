package com.example.a71p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "items";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the items table
        String query =
                "CREATE TABLE " + TABLE_NAME + "("+
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "lostOrFound TEXT NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "number TEXT NOT NULL, " +
                        "description TEXT, " +
                        "date TEXT NOT NULL, " +
                        "location TEXT NOT NULL" +
                        ")";
        db.execSQL(query);
    }


    //Used to add values to the database
    public void addNewItem(String lostOrFound, String name, String number, String description, String date, String location)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //Content values is so all can be added to the database at once
        ContentValues values = new ContentValues();

        values.put("lostOrFound", lostOrFound);
        values.put("name", name);
        values.put("number", number);
        values.put("description", description);
        values.put("date", date);
        values.put("location", location);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);


    }
}