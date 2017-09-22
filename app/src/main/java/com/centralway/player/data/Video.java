package com.centralway.player.data;

import android.support.annotation.NonNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class Video {

    @NonNull
    private String mId;

    @NonNull
    private String mName;

    @NonNull
    private String mUri;

    private long mDate;

    private long mLength;

    public Video(
            @NonNull String id,
            @NonNull String name,
            @NonNull String uri,
            long date,
            long length
    ) {
        mId = id;
        mName = name;
        mUri = uri;
        mDate = date;
        mLength = length;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUri() {
        return mUri;
    }

    public long getDate() {
        return mDate;
    }

    public long getLength() {
        return mLength;
    }

    public String toString() {
        return mId + " - " + mUri + " " + mName;
    }
}
