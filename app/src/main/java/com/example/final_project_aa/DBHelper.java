package com.example.final_project_aa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "game.db";
    public static final String GAMER_ID = "game";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String SCORE = "score";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public DBHelper(@Nullable Context context) {

        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + GAMER_ID + " (" + NAME + " TEXT PRIMARY KEY , " + PASSWORD + " TEXT, " + SCORE + " TEXT, " + LATITUDE + " TEXT, " + LONGITUDE + " TEXT )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE " + GAMER_ID;
        db.execSQL(query);
    }

    public boolean insert(String name, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PASSWORD, password);
        return db.insert(GAMER_ID, null, contentValues) != -1;
    }

    public Cursor getAll() {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + GAMER_ID, null);
    }

    public int update(String name, String score,String  lat,String  longi) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(SCORE, score);
        contentValues.put(LATITUDE,lat);
        contentValues.put(LONGITUDE,longi);


        return db.update(GAMER_ID, contentValues, "NAME = ?", new String[] {name});
    }
}

