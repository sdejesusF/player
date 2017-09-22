package com.centralway.player.data;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class Playlist {

    @NonNull
    private String mId;

    @NonNull
    private String mName;

    private List<Video> mVideos;

    public Playlist(
            @NonNull String name,
            List<Video> videos
    ) {
        this(UUID.randomUUID().toString(), name, videos);
    }

    public Playlist(
            @NonNull String id,
            @NonNull String name,
            List<Video> videos
    ) {
        mId = id;
        mName = name;
        mVideos = videos;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<Video> getVideos() {
        return mVideos;
    }
}
