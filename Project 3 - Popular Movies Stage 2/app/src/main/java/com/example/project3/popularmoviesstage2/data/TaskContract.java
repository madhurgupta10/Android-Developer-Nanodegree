package com.example.project3.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {

    public static final String AUTHORITY = "com.example.project3.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";


    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_MID = "mid";

        public static final String COLUMN_JSON = "json";
    }
}
