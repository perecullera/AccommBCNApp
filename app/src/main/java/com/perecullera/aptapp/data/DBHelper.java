package com.perecullera.aptapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.perecullera.aptapp.data.AptContract.ApartmentEntry;

/**
 * Created by perecullera on 8/10/15.
 */
public class DBHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "apt.db";

    public final String LOG_TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_APARTMENT_TABLE = "CREATE TABLE " + ApartmentEntry.TABLE_NAME + " (" +
                ApartmentEntry._ID + " INTEGER PRIMARY KEY," +
                ApartmentEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ApartmentEntry.COLUMN_ADDRESS + " TEXT , " +
                ApartmentEntry.COLUMN_NEIGHBORHOOD + " TEXT, " +
                ApartmentEntry.COLUMN_DISTRICT + " TEXT, "+
                ApartmentEntry.COLUMN_POSTAL_CODE + " TEXT, " +
                ApartmentEntry.COLUMN_CATS + " TEXT, " +
                ApartmentEntry.COLUMN_LATITUDE + " TEXT," +
                ApartmentEntry.COLUMN_LONGITUDE + " TEXT," +
                ApartmentEntry.COLUMN_CREATED + " TEXT " +
                ");";

        db.execSQL(SQL_CREATE_APARTMENT_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ApartmentEntry.TABLE_NAME);
        Log.d(LOG_TAG, " Dropped Table");
        onCreate(db);
    }
}
