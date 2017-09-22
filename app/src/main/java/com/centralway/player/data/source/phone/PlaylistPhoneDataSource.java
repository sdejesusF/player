package com.centralway.player.data.source.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistDataSource;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlaylistPhoneDataSource implements PlaylistDataSource {

    private static PlaylistPhoneDataSource INSTANCE;

    private final ContentResolver mContentResolver;

    private final Uri URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private final SqlBrite mSqlBrite;
    private String[] PROJECTION = {
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DATE_TAKEN
    };

    private PlaylistPhoneDataSource(@NonNull Context context) {
        mContentResolver = context.getContentResolver();
        mSqlBrite = SqlBrite.create();
    }

    public static PlaylistPhoneDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PlaylistPhoneDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Video>> getVideos() {
        BriteContentResolver resolver = mSqlBrite.wrapContentProvider(mContentResolver, Schedulers.io());
        return
                resolver.createQuery(
                        URI,
                        PROJECTION,
                        null,
                        null,
                        null,
                        false)
                        .mapToList(this::cursorToList)
                        .first();
    }

    @Override
    public Observable<List<Video>> getVideosPlaylist(String playList) {
        return null;
    }

    @Override
    public Observable<List<Playlist>> getPlaylist() {
        return null;
    }

    @Override
    public Observable<Playlist> getPlaylist(String id) {
        return null;
    }

    @Override
    public Observable<Video> getVideo(String id) {
        return null;
    }

    @Override
    public void refreshVideos() {

    }

    @Override
    public void addVideo(Video video) {

    }

    @Override
    public void addVideo(Video video, String playListID) {

    }

    @Override
    public void addVideo(String videoId, String playListID) {

    }

    @Override
    public void addVideo(String videoId, Playlist playListID) {

    }

    @Override
    public void addVideo(Video videoId, Playlist playListID) {

    }

    @Override
    public void addVideos(List<Video> videos, String playListID) {

    }

    @Override
    public void addEditPlaylist(Playlist playlist) {

    }

    @Override
    public void deletePlaylist(String playlistID) {

    }

    @Override
    public void deleteVideo(String videoId) {

    }

    @Override
    public void deleteVideoFromUri(String videoUri) {

    }


    public Video cursorToList(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME));
        String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
        int date = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN));
        return new Video(id, name, uri, date, length);
    }
}
