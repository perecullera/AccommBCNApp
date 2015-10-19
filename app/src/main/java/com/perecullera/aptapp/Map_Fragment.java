package com.perecullera.aptapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perecullera.aptapp.data.AptContract;



public class Map_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int MAP_LOADER = 0;

    private GoogleMap map;

    private static final String LOG_TAG = Map_Fragment.class.getSimpleName();

    Context context;

    public Map_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(MAP_LOADER, null, this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_, null, false);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        getLoaderManager().initLoader(MAP_LOADER, null, this);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date
        Uri AptUri = AptContract.ApartmentEntry.CONTENT_URI;
        Log.d(LOG_TAG, "Loader created with uri " + AptUri);

        String [] projection = {AptContract.ApartmentEntry.COLUMN_NAME,
        AptContract.ApartmentEntry.COLUMN_LATITUDE,
        AptContract.ApartmentEntry.COLUMN_LONGITUDE};

        return new CursorLoader(context,
                AptUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //TODO
        int col_name = data.getColumnIndex(AptContract.ApartmentEntry.COLUMN_NAME);
        int col_lat = data.getColumnIndex(AptContract.ApartmentEntry.COLUMN_LATITUDE);
        int col_long = data.getColumnIndex(AptContract.ApartmentEntry.COLUMN_LONGITUDE);
        while (data.moveToNext()) {
            String name = data.getString(col_name);
            Float lat = Float.parseFloat(data.getString(col_lat));
            Float lon = Float.parseFloat(data.getString(col_long));
            Log.d(LOG_TAG, "Marker Name " + name
            + "lat " + lat + "lon " + lon);
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(name));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
