package com.perecullera.aptapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.perecullera.aptapp.data.AptContract;

/**
 * Created by perecullera on 9/10/15.
 */
public class AptAdapter extends CursorAdapter {

    public AptAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View view = LayoutInflater.from(context).inflate(R.layout.list_item_apartment, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int name_index = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_NAME);
        int neigh_index = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_NEIGHBORHOOD);
        int district_index = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_DISTRICT);
        TextView tv = (TextView) view;
        //if cursor brings the name is for apt list
        if(!cursor.isNull(name_index)){
            tv.setText(cursor.getString(name_index));

        //otherwise, is for district list
        }else if (!cursor.isNull(district_index)){
            tv.setText(cursor.getString(district_index));

        //or neighborhood list
        }else if (!cursor.isNull(neigh_index)){
            tv.setText(cursor.getString(neigh_index));
        }else {
            int i = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_NAME);
            tv.setText(cursor.getString(i));
        }
    }
}
