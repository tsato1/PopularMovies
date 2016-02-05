package com.udacity.tsato.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*** Got from http://qiita.com/kojionilk/items/9bb1a28d04dc87d301b9 ***/
public class DBContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.udacity.tsato.provider";

    private static final String SQLITE_FILENAME = "tsato.sqlite";

    public enum Movie {
        TABLE_MOVIES (BaseColumns._ID, DBColumns.COL_TITLE,
                DBColumns.COL_POSTER_PATH,
                DBColumns.COL_OVERVIEW,
                DBColumns.COL_RELEASE_DATE,
                DBColumns.COL_VOTE_AVERAGE,
                DBColumns.COL_AUTHOR,
                DBColumns.COL_REVIEW,
                DBColumns.COL_NAME,
                DBColumns.COL_VIDEO);
//        TABLE_REVIEWS (BaseColumns._ID, DBColumns.COL_AUTHOR, DBColumns.COL_REVIEW),
//        TABLE_VIDEOS (BaseColumns._ID, DBColumns.COL_NAME, DBColumns.COL_VIDEO);

        Movie(final String... columns) {
            this.columns = Collections.unmodifiableList(Arrays.asList(columns));
        }

        private final String tableName = name().toLowerCase();
        private final int allCode = ordinal() * 10;
        private final int byIdCode = ordinal() * 10 + 1;

        public final List<String> columns;
        public final String mimeTypeForAll = "vnd.android.cursor.dir/vnd.udacity.tsato." + tableName;
        public final String mimeTypeForone = "vnd.android.cursor.item/vnd.udacity.tsato." + tableName;
        public final Uri contentUri = Uri.parse("content://" + AUTHORITY + "/" +tableName);
    }

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, Movie.TABLE_MOVIES.tableName, Movie.TABLE_MOVIES.allCode);
        sUriMatcher.addURI(AUTHORITY, Movie.TABLE_MOVIES.tableName + "/#", Movie.TABLE_MOVIES.byIdCode);
//        sUriMatcher.addURI(AUTHORITY, Movie.TABLE_REVIEWS.tableName, Movie.TABLE_REVIEWS.allCode);
//        sUriMatcher.addURI(AUTHORITY, Movie.TABLE_REVIEWS + "/#", Movie.TABLE_REVIEWS.byIdCode);
//        sUriMatcher.addURI(AUTHORITY, Movie.TABLE_VIDEOS.tableName, Movie.TABLE_VIDEOS.allCode);
//        sUriMatcher.addURI(AUTHORITY, Movie.TABLE_VIDEOS + "/#", Movie.TABLE_VIDEOS.byIdCode);
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        final int version;
        try {
            version = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mDBHelper = new DBHelper(getContext(), SQLITE_FILENAME, null, version);
        return true;
    }

    @Override
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        checkUri(uri);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(uri.getPathSegments().get(0), projection, appendSelection(uri, selection), appendSelectionArgs(uri, selectionArgs), null, null, sortOrder);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        checkUri(uri);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final long rowId = db.insertOrThrow(uri.getPathSegments().get(0), null, values);
        Uri returnUri = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        checkUri(uri);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int count = db.update(uri.getPathSegments().get(0), values, appendSelection(uri, selection), appendSelectionArgs(uri, selectionArgs));
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        checkUri(uri);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int count = db.delete(uri.getPathSegments().get(0), appendSelection(uri, selection), appendSelectionArgs(uri, selectionArgs));
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        final int code = sUriMatcher.match(uri);
        for (final Movie movie : Movie.values()) {
            if (code == movie.allCode) return movie.mimeTypeForAll;
            else if (code == movie.byIdCode) return movie.mimeTypeForone;
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    private void checkUri(Uri uri) {
        final int code = sUriMatcher.match(uri);
        for (final Movie movie : Movie.values()) {
            if (code == movie.allCode) {
                return;
            } else if (code == movie.byIdCode) {
                return;
            }
        }
        throw new IllegalArgumentException("unknown uri : " + uri);
    }

    private String appendSelection(Uri uri, String selection) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 1) {
            return selection;
        }
        return BaseColumns._ID + " = ?" + (selection == null ? "" : " AND (" + selection + ")");
    }

    private String[] appendSelectionArgs(Uri uri, String[] selectionArgs) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 1) {
            return selectionArgs;
        }
        if (selectionArgs == null || selectionArgs.length == 0) {
            return new String[] {pathSegments.get(1)};
        }
        String[] returnArgs = new String[selectionArgs.length + 1];
        returnArgs[0] = pathSegments.get(1);
        System.arraycopy(selectionArgs, 0, returnArgs, 1, selectionArgs.length);
        return returnArgs;
    }
}
