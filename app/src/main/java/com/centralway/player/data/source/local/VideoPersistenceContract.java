package com.centralway.player.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class VideoPersistenceContract {
    private VideoPersistenceContract() {
    }

    public static abstract class VideoPersistenceEntry implements BaseColumns {
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_URI = "uri";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LENGTH = "length";
    }
}
