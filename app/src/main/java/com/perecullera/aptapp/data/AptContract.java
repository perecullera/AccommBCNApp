package com.perecullera.aptapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by perecullera on 8/10/15.
 */
public class AptContract {

    public static final String CONTENT_AUTHORITY = "com.perecullera.aptapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_AP = "apartment";
    public static final String PATH_CAT = "category";

    /* Inner class that defines the table contents of the apartment table */
    public static final class ApartmentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AP).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_AP;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_AP;

        // Table name
        public static final String TABLE_NAME = "apartments";

        //columns
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NEIGHBORHOOD = "neighborhood";
        public static final String COLUMN_DISTRICT = "district";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_CATS = "cats";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_POSTAL_CODE = "postal_code";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        public static Uri buildAptUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the cats table */
    public static final class CatsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AP).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_AP;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_AP;

        public static final String TABLE_NAME = "categories";

        //columns
        public static final String COLUMN_NAME = "name";

        public static Uri buildAptUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }






    }
