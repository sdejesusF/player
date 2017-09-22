package com.centralway.player.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class VideoPlayerDBHelper extends SQLiteOpenHelper {
    private static final String TAG = VideoPlayerDBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "VideoPlayer.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String REAL_TYPE = " REAL";

    private static final String TINY_INT_TYPE = " TINYINT";

    private static final String COMMA_SEP = ",";

    private static final String[] ENTRIES = new String[]{

            "CREATE TABLE " + PlaylistPersistenceContract.PlaylistEntry.TABLE_NAME + " (" +
                    PlaylistPersistenceContract.PlaylistEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_VIDEOS + TEXT_TYPE +
                    " );",

            "CREATE TABLE " + VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME + " (" +
                    VideoPersistenceContract.VideoPersistenceEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_LENGTH + INTEGER_TYPE + COMMA_SEP +
                    VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_URI + TEXT_TYPE +

                    " );",

    };

    public VideoPlayerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        for (String entry : ENTRIES) {
            Log.i(TAG, "Executing " + entry);
            db.execSQL(entry);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
