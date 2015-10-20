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
import android.widget.AdapterView;
import android.widget.ListView;

import com.perecullera.aptapp.data.AptContract;


public class Neigh_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private int NEIGH_LOADER = 0;

    private static final String LOG_TAG = Neigh_Fragment.class.getSimpleName();

    Context context;
    private AptAdapter mAptAdapter;

    public Neigh_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(NEIGH_LOADER, null, this);

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
                    int col_index = cursor.getColumnIndex(
                            AptContract.ApartmentEntry.COLUMN_NEIGHBORHOOD);
                    String neigh = cursor.getString(col_index);
                    /*Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("neigh", neigh);
                    startActivity(intent);*/
                }
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        getLoaderManager().initLoader(NEIGH_LOADER, null, this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri AptUri = Uri.parse(AptContract.BASE_CONTENT_URI+"/neighborhood");
        Log.d(LOG_TAG, "Loader created with uri " + AptUri);

        String [] projection = {AptContract.ApartmentEntry.COLUMN_NEIGHBORHOOD};

        return new CursorLoader(context,
                AptUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAptAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAptAdapter.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}