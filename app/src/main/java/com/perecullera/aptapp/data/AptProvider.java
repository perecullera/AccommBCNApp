package com.perecullera.aptapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by perecullera on 8/10/15.
 */
public class AptProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DBHelper mDBHelper;

    private static final int APARTMENT = 100;
    private static final int APARTMENT_DETAIL = 101;
    private static final int APARTMENTS_WITH_LOCATION = 102;
    /* private static final int LOCATION = 300;
    private static final int LOCATION_ID = 301;
    */


    private static UriMatcher buildUriMatcher() {
       final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
       final String authority = AptContract.CONTENT_AUTHORITY;

       matcher.addURI(authority, AptContract.PATH_AP, APARTMENT);
       matcher.addURI(authority, AptContract.PATH_AP + "/*", APARTMENT_DETAIL);
       matcher.addURI(authority, AptContract.PATH_AP, APARTMENTS_WITH_LOCATION);

       return matcher;
   }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        retCursor = mDBHelper.getReadableDatabase().query(
                AptContract.ApartmentEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        Log.d("APTProvider", "Loader returns cursor: " + retCursor.getCount());
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
      return AptContract.ApartmentEntry.CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        long _id = db.insert(AptContract.ApartmentEntry.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = AptContract.ApartmentEntry.buildAptUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        rowsDeleted = db.delete(
                AptContract.ApartmentEntry.TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        rowsUpdated = db.update(AptContract.ApartmentEntry.TABLE_NAME, values, selection,
                selectionArgs);
        return rowsUpdated;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Cursor retCursor;
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case APARTMENT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(AptContract.ApartmentEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
