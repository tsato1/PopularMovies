package com.udacity.tsato.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by T on 2016/01/27.
 */
public class DBContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.udacity.tsato";

    public static final int FAVORITES = 1;
    public static final int FAVORITE_ID = 2;

    public static UriMatcher sUriMatcher ;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DBColumns.PATH, FAVORITES);
        sUriMatcher.addURI(AUTHORITY, DBColumns.PATH + "/#", FAVORITE_ID);
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
            case FAVORITE_ID:
                queryBuilder.setTables(DBColumns.TABLE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String insertTable;
        Uri contentUri;
        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                insertTable = DBColumns.TABLE;
                contentUri = DBColumns.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        long rowId = db.insert(insertTable, null, values);
        if (rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        } else {
            throw new IllegalArgumentException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        int count;
        String id = uri.getPathSegments().get(1);
        count = db.update(DBColumns.TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
            case FAVORITE_ID:
                count = db.delete(DBColumns.TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case FAVORITES:
                return DBColumns.CONTENT_TYPE;
            case FAVORITE_ID:
                return DBColumns.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
