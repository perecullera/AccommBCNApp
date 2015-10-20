package com.perecullera.aptapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.perecullera.aptapp.data.AptContract;

/**
 * Created by perecullera on 20/10/15.
 */
public class District_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private int DISTRICT_LOADER = 0;
    private int DISTRICT_LOADER_DETAIL = 1;

    private static final String LOG_TAG = District_Fragment.class.getSimpleName();

    Context context;
    private AptAdapter mAptAdapter;

    Loader loader;

    public District_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(DISTRICT_LOADER, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_apt_list, container, false);
        mAptAdapter = new AptAdapter(getActivity(), null, 0);

        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_apt);
        listView.setAdapter(mAptAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    if(loader.getId() == DISTRICT_LOADER){
                        int col_index = cursor.getColumnIndex(
                                AptContract.ApartmentEntry.COLUMN_DISTRICT);
                        String district = cursor.getString(col_index);
                        Bundle bundle = new Bundle();
                        bundle.putString("district", district);
                        getLoaderManager().initLoader(DISTRICT_LOADER_DETAIL, bundle, District_Fragment.this);
                    }else if (loader.getId() == DISTRICT_LOADER_DETAIL) {
                        int col_index = cursor.getColumnIndex(
                                AptContract.ApartmentEntry.COLUMN__ID);
                        int apt_id = cursor.getInt(col_index);
                        Intent intent = new Intent(getActivity(), DetailActivity.class)
                                .putExtra("id", apt_id);
                        startActivity(intent);
                    }
                }
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        getLoaderManager().initLoader(DISTRICT_LOADER, null, this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri AptUri = null;
        String [] projection = null;
        String selection = null;
        String [] selectionArgs = null;
        if (id == DISTRICT_LOADER) {
            AptUri = Uri.parse(AptContract.BASE_CONTENT_URI + "/district");
            projection = new String[]{AptContract.ApartmentEntry.COLUMN_DISTRICT};

        } else if (id == DISTRICT_LOADER_DETAIL) {
            String district = args.getString("district");
            AptUri = Uri.parse(AptContract.BASE_CONTENT_URI + "/district"+"/" + district);
            selection = AptContract.ApartmentEntry.COLUMN_DISTRICT + "=?";
            selectionArgs = new String[]{district};
        }

        Log.d(LOG_TAG, "Loader created with uri " + AptUri);

        return new CursorLoader(context,
                AptUri,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAptAdapter.changeCursor(data);
        this.loader = loader;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAptAdapter.swapCursor(null);
    }

}
