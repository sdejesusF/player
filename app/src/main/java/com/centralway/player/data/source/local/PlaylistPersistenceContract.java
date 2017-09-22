package com.centralway.player.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlaylistPersistenceContract {

    private PlaylistPersistenceContract() {
    }

    public static abstract class PlaylistEntry implements BaseColumns {
        public static final String TABLE_NAME = "playlist";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_VIDEOS = "videos";
    }
}
