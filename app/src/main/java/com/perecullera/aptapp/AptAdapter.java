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
        int id_index = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_ID);
        TextView tv = (TextView) view;
        if(cursor.isNull(id_index)){
            int i = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_NEIGHBORHOOD);
            tv.setText(cursor.getString(i));
        }else {
            int i = cursor.getColumnIndex(AptContract.ApartmentEntry.COLUMN_NAME);
            tv.setText(cursor.getString(i));
        }
    }
}
