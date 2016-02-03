package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL("CREATE TABLE " + DBColumns.TABLE_MOVIES + " ("
                    + DBColumns.COL_ID + " INTEGER PRIMARY KEY,"
                    + DBColumns.COL_TITLE + " TEXT NOT NULL,"
                    + DBColumns.COL_POSTER_PATH + " TEXT NOT NULL,"
                    + DBColumns.COL_POSTER + " TEXT NOT NULL,"
                    + DBColumns.COL_OVERVIEW + " TEXT NOT NULL,"
                    + DBColumns.COL_RELEASE_DATE + " TEXT NOT NULL,"
                    + DBColumns.COL_VOTE_AVERAGE + " TEXT NOT NULL,"
                    + DBColumns.COL_AUTHOR + " TEXT NOT NULL,"
                    + DBColumns.COL_REVIEW + " TEXT NOT NULL,"
                    + DBColumns.COL_NAME + " TEXT NOT NULL,"
                    + DBColumns.COL_VIDEO + " TEXT NOT NULL);"
            );
//            sqLiteDatabase.execSQL("CREATE TABLE " + DBColumns.TABLE_REVIEWS + " ("
//                    + DBColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + DBColumns.COL_AUTHOR + " TEXT NOT NULL,"
//                    + DBColumns.COL_REVIEW + " TEXT NOT NULL);"
//            );
//            sqLiteDatabase.execSQL("CREATE TABLE " + DBColumns.TABLE_VIDEOS + " ("
//                    + DBColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + DBColumns.COL_NAME + " TEXT NOT NULL,"
//                    + DBColumns.COL_VIDEO + " TEXT NOT NULL);"
//            );
            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE_MOVIES IF EXISTS " + DBColumns.TABLE_MOVIES);
//        sqLiteDatabase.execSQL("DROP TABLE_REVIEWS IF EXISTS " + DBColumns.TABLE_REVIEWS);
//        sqLiteDatabase.execSQL("DROP TABLE_VIDEO IF EXISTS " + DBColumns.TABLE_VIDEOS);
        onCreate(sqLiteDatabase);
    }
}
