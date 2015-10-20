package com.perecullera.aptapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perecullera.aptapp.data.AptContract;

public class DetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int APARTMENT_LOADER = 0;
    private static final String COL_APT_NAME = AptContract.ApartmentEntry.COLUMN_NAME;
    private static final String COL_APT_ADD = AptContract.ApartmentEntry.COLUMN_ADDRESS;
    private static final String COL_APT_CODE = AptContract.ApartmentEntry.COLUMN_POSTAL_CODE;
    private static final String COL_APT_DIST = AptContract.ApartmentEntry.COLUMN_DISTRICT;
    private static final String COL_APT_NEIGH = AptContract.ApartmentEntry.COLUMN_NEIGHBORHOOD;
    private static final String COL_APT_LAT = AptContract.ApartmentEntry.COLUMN_LATITUDE;
    private static final String COL_APT_LONG = AptContract.ApartmentEntry.COLUMN_LONGITUDE;


    int apt_id;

    TextView nameTv;
    TextView addressTv;
    TextView p_codeTv;
    TextView neighTV;
    TextView districtTV;

    public final String LOG_TAG = DetailActivity.class.getSimpleName();
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTv = (TextView) findViewById(R.id.detail_text_view);
        addressTv = (TextView) findViewById(R.id.adress_textview);
        p_codeTv = (TextView) findViewById(R.id.p_code_textview);
        neighTV = (TextView) findViewById(R.id.neigh_textview);
        districtTV = (TextView) findViewById(R.id.district_textview);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map))
                .getMap();


        apt_id = getIntent().getIntExtra("id", 0);
        Log.d(LOG_TAG, "Extra id =  " + apt_id );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getLoaderManager().initLoader(APARTMENT_LOADER, null, this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_neigh) {
            // Handle the camera action
        } else if (id == R.id.nav_district) {

        } else if (id == R.id.nav_search) {

        } else if (id == R.id.nav_cats) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri AptUri = AptContract.ApartmentEntry.CONTENT_URI;
        Log.d(LOG_TAG, "Loader created with uri " + AptUri);
        String selection = AptContract.ApartmentEntry._ID + "= ? ";
        return new CursorLoader(this,
                AptUri,
                null,
                selection,
                new String[]{ Integer.toString(apt_id)},
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Loader returns cursor: " + data.getCount());
        if (data != null && data.moveToFirst()) {

            String name = data.getString(data.getColumnIndex(COL_APT_NAME));
            String address = data.getString(data.getColumnIndex(COL_APT_ADD));
            String p_code = data.getString(data.getColumnIndex(COL_APT_CODE));
            String neigh = data.getString(data.getColumnIndex(COL_APT_NEIGH));
            String district = data.getString(data.getColumnIndex(COL_APT_DIST));
            Float latitude = Float.parseFloat(data.getString(data.getColumnIndex(COL_APT_LAT)));
            Float longitude = Float.parseFloat(data.getString(data.getColumnIndex(COL_APT_LONG)));

            Log.d(LOG_TAG, "Loader set text to " + name);
            nameTv.setText(name);
            addressTv.setText(address);
            p_codeTv.setText(p_code);
            neighTV.setText(neigh);
            districtTV.setText(district);

            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            LatLng latLong = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions()
                .title(name)
                .position(latLong));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
