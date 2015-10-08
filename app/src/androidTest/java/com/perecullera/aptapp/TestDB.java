package com.perecullera.aptapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.perecullera.aptapp.data.DBHelper;

import java.util.Map;
import java.util.Set;

import static com.perecullera.aptapp.data.AptContract.ApartmentEntry;

/**
 * Created by perecullera on 8/10/15.
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();
    static final String TEST_LOCATION = "99705";
    static final String TEST_DATE = "20141205";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
        SQLiteDatabase db = new DBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
    // If there's an error in those massive SQL table creation Strings,
    // errors will be thrown here when you try to get a writable database.
    public void testInsertReadDb(){
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createDummieValues();

        long locationRowId;
        locationRowId = db.insert(ApartmentEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        Cursor cursor = db.query(
                ApartmentEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, testValues);
        dbHelper.close();
    }


    private ContentValues createDummieValues() {

        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ApartmentEntry.COLUMN_NAME, "Apt1");
        testValues.put(ApartmentEntry.COLUMN_ADDRESS, "North Pole");
        testValues.put(ApartmentEntry.COLUMN_NEIGHBORHOOD, "Eixample dreta");
        testValues.put(ApartmentEntry.COLUMN_DISTRICT,"Eixample");
        testValues.put(ApartmentEntry.COLUMN_POSTAL_CODE,"08002");
        testValues.put(ApartmentEntry.COLUMN_CATS,"Hotels");
        testValues.put(ApartmentEntry.COLUMN_LATITUDE,64.7488);
        testValues.put(ApartmentEntry.COLUMN_LONGITUDE,-147.353);
        testValues.put(ApartmentEntry.COLUMN_CREATED,"today ");

        return testValues;

    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }


}
