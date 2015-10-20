package com.perecullera.aptapp.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.perecullera.aptapp.R;
import com.perecullera.aptapp.data.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import static com.perecullera.aptapp.data.AptContract.ApartmentEntry;

/**
 * Created by perecullera on 9/10/15.
 */
public class AptSyncAdapter extends AbstractThreadedSyncAdapter {

    private final Context mContext;

    public final String LOG_TAG = AptSyncAdapter.class.getSimpleName();

    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    
    // Interval at which to sync with the weather, in milliseconds.
    // 1000 milliseconds (1 second) * 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 320;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    public AptSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String responseJsonStr = null;

        String format = "json";

        try{
            final String FORECAST_BASE_URL =
                    "https://cryptic-reaches-7088.herokuapp.com/apartments/";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().build();
            URL url = new URL(builtUri.toString());

            // Create the request to APT API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            responseJsonStr = buffer.toString();

            // These are the names of the JSON objects that need to be extracted.
            final String APTname = "name";
            final String APTid = "id_2";
            final String APTaddress = "address";
            final String APTneigh = "neighborhood";
            final String APTdistrict = "district";
            final String APTpostal_code = "postal_code";
            final String APTcats = "cats";
            final String APTlatitude = "latitude";
            final String APTlongitude = "longitude";
            final String APTcreated = "created";

            // Each apt's is an element of the "list" array.
            final String APTList = "list";

            try {
                Log.d(LOG_TAG, "Json String response. " + responseJsonStr);
                JSONArray aptArray = new JSONArray(responseJsonStr);

                Vector<ContentValues> cVVector = new Vector<ContentValues>(aptArray.length());

                for(int i = 0; i < aptArray.length(); i++) {
                    String name;
                    String address;
                    String neighborhood;
                    String id_2;
                    String district;
                    String postal_code;
                    String cats;
                    String latitude;
                    String longitude;
                    String created;

                    JSONObject aptJSON = aptArray.getJSONObject(i);
                    name = aptJSON.getString(APTname);
                    id_2 = aptJSON.getString(APTid);
                    address = aptJSON.getString(APTaddress);
                    neighborhood = aptJSON.getString(APTneigh);
                    district = aptJSON.getString(APTdistrict);
                    postal_code = aptJSON.getString(APTpostal_code);

                    cats = "accommodation"; //TODO fake categoryes
                    latitude = aptJSON.getString(APTlatitude);
                    longitude = aptJSON.getString(APTlongitude);
                    created = aptJSON.getString(APTcreated);

                    Log.d(LOG_TAG, "Latitude saved " + latitude);
                    Log.d(LOG_TAG, "Longitude saved " + longitude);

                    ContentValues aptValues = new ContentValues();
                    aptValues.put(ApartmentEntry.COLUMN_NAME, name);
                    aptValues.put(ApartmentEntry.COLUMN_ID, id_2);
                    aptValues.put(ApartmentEntry.COLUMN_NEIGHBORHOOD, neighborhood);
                    aptValues.put(ApartmentEntry.COLUMN_DISTRICT, district);
                    aptValues.put(ApartmentEntry.COLUMN_POSTAL_CODE, postal_code);
                    aptValues.put(ApartmentEntry.COLUMN_ADDRESS, address);
                    aptValues.put(ApartmentEntry.COLUMN_LATITUDE,latitude);
                    aptValues.put(ApartmentEntry.COLUMN_LONGITUDE, longitude);

                    cVVector.add(aptValues);

                }
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    DBHelper db = new DBHelper(mContext);
                    //we delete old apartment from the database
                    db.onUpgrade(db.getWritableDatabase(), 0, 0);
                    mContext.getContentResolver().bulkInsert(ApartmentEntry.CONTENT_URI, cvArray);
                }
                Log.d(LOG_TAG, "Fetch Apartments Complete. " + cVVector.size() + " Inserted");


            }catch (Exception e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }


        }catch(Exception e){
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return;

        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }
    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);

    }
    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        AptSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
