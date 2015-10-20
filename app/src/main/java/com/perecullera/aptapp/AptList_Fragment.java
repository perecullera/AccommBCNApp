package com.perecullera.aptapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
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


public class AptList_Fragment extends Fragment implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = AptList_Fragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private AptAdapter mAptAdapter;
    private int APARTMENT_LOADER = 0;
    private int COL_APT_ID = 0; //todo put aptcontract column name
    Context context;

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date
        Uri AptUri = AptContract.ApartmentEntry.CONTENT_URI;
        Log.d(LOG_TAG, "Loader created with uri " + AptUri);

        return new CursorLoader(context,
                AptUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {

        mAptAdapter.swapCursor(cursor);
        Log.d(LOG_TAG, "Loader finished " + cursor + "count: "+  cursor.getCount());

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mAptAdapter.swapCursor(null);
    }


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }


    public AptList_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                    int apt_id = cursor.getInt(COL_APT_ID);
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("id", apt_id);
                    startActivity(intent);
                }
            }
        });
        return rootView;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(APARTMENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
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
        mListener = null;
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
