package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

/**
 * Created by T on 2016/01/27.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "popularmovies.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE " + DBColumns.TABLE + " ("
                    + DBColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DBColumns.COL_TITLE + " TEXT NOT NULL,"
                    + DBColumns.COL_POSTER_PATH + " TEXT NOT NULL,"
                    + DBColumns.COL_POSTER + " TEXT NOT NULL,"
                    + DBColumns.COL_OVERVIEW + " TEXT NOT NULL,"
                    + DBColumns.COL_RELEASE_DATE + " TEXT NOT NULL,"
                    + DBColumns.COL_VOTE_AVERAGE + " TEXT NOT NULL,"
                    + DBColumns.COL_REVIEW + " TEXT NOT NULL);"
            );
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBColumns.TABLE);
        onCreate(sqLiteDatabase);
    }
}
