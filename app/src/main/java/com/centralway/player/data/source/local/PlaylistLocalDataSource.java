package com.centralway.player.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistDataSource;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlaylistLocalDataSource implements PlaylistDataSource {

    private static final String VIDEO_SEPARATOR = ",";
    @Nullable
    private static PlaylistLocalDataSource INSTANCE;
    @NonNull
    private final BriteDatabase mDatabaseHelper;
    private String[] VIDEO_PROJECTION = {
            VideoPersistenceContract.VideoPersistenceEntry._ID,
            VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_NAME,
            VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_URI,
            VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_DATE,
            VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_LENGTH
    };

    private String[] PLAYLIST_PROJECTION = {
            PlaylistPersistenceContract.PlaylistEntry._ID,
            PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_NAME,
            PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_VIDEOS
    };

    private PlaylistLocalDataSource(@NonNull Context context) {
        checkNotNull(context, "context cannot be null");
        VideoPlayerDBHelper dbHelper = new VideoPlayerDBHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

    public static PlaylistLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PlaylistLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Video>> getVideos() {
        String sql = String.format(
                "SELECT %s FROM %s",
                TextUtils.join(",", VIDEO_PROJECTION),
                VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME, sql)
                .mapToList(this::getVideo)
                .first();
    }

    @Override
    public Observable<List<Video>> getVideosPlaylist(String playlistId) {
        return getPlaylist(playlistId)
                .flatMap(playlist -> Observable.from(playlist.getVideos()))
                .toList()
                .first();
    }


    @Override
    public Observable<List<Playlist>> getPlaylist() {
        String sql = String.format(
                "SELECT %s FROM %s",
                TextUtils.join(",", PLAYLIST_PROJECTION),
                PlaylistPersistenceContract.PlaylistEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME, sql)
                .mapToList(this::getPlaylist)
                .first();
    }

    @Override
    public Observable<Playlist> getPlaylist(String id) {
        String sql = String.format(
                "SELECT %s FROM %s WHERE %s LIKE '%s'",
                TextUtils.join(",", PLAYLIST_PROJECTION),
                PlaylistPersistenceContract.PlaylistEntry.TABLE_NAME,
                PlaylistPersistenceContract.PlaylistEntry._ID,
                id);
        return mDatabaseHelper.createQuery(PlaylistPersistenceContract.PlaylistEntry.TABLE_NAME, sql)
                .mapToOne(this::getPlaylist)
                .first();
    }

    @Override
    public Observable<Video> getVideo(String id) {
        String sql = String.format(
                "SELECT %s FROM %s WHERE %s LIKE '%s'",
                TextUtils.join(",", VIDEO_PROJECTION),
                VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME,
                VideoPersistenceContract.VideoPersistenceEntry._ID,
                id);
        return mDatabaseHelper.createQuery(VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME, sql)
                .mapToOne(this::getVideo)
                .first();
    }

    @Override
    public void refreshVideos() {

    }

    @Override
    public void addVideo(Video video) {
        saveVideo(video);
    }

    @Override
    public void addVideo(final Video newVideo, String playListID) {
        if (playListID == null) {
            saveVideo(newVideo);
        } else {
            getPlaylist(playListID)
                    .doOnCompleted(() -> {
                    })
                    .subscribe((playlist) -> {
                        addVideo(newVideo, playlist);
                    });
        }
    }

    @Override
    public void addVideo(String videoId, String playListID) {
        Observable.zip(getVideo(videoId), getPlaylist(playListID), (video, playlist) -> {
            addVideo(video, playlist);
            return null;
        }).first();

    }

    @Override
    public void addVideo(String videoId, Playlist playlist) {
        getVideo(videoId)
                .first()
                .subscribe(v -> addVideo(v, playlist));
    }

    @Override
    public void addVideo(Video video, Playlist playlist) {
        playlist.getVideos().add(video);
        addEditPlaylist(playlist);
    }

    @Override
    public void addVideos(List<Video> videos, String playListID) {
        getPlaylist(playListID)
                .first()
                .subscribe(playlist -> {
                    for (Video video : videos) {
                        playlist.getVideos().add(video);
                    }
                    addEditPlaylist(playlist);
                });
    }

    @Override
    public void addEditPlaylist(Playlist playlist) {
        savePlaylist(playlist);
    }

    @Override
    public void deletePlaylist(String playlistID) {
        String selection = PlaylistPersistenceContract.PlaylistEntry._ID + " LIKE ?";
        String[] selectionArgs = {playlistID};
        mDatabaseHelper
                .delete(PlaylistPersistenceContract.PlaylistEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void deleteVideo(String videoId) {
        String selection = VideoPersistenceContract.VideoPersistenceEntry._ID + " LIKE ?";
        String[] selectionArgs = {videoId};
        mDatabaseHelper
                .delete(VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void deleteVideoFromUri(String videoUri) {
        String selection = VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_URI + " LIKE ?";
        String[] selectionArgs = {videoUri};
        mDatabaseHelper
                .delete(VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void saveVideo(final Video video) {
        checkNotNull(video);
        ContentValues values = new ContentValues();
        values.put(VideoPersistenceContract.VideoPersistenceEntry._ID, video.getId());
        values.put(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_NAME, video.getName());
        values.put(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_DATE, video.getDate());
        values.put(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_LENGTH, video.getLength());
        values.put(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_URI, video.getUri());
        mDatabaseHelper
                .insert(VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void savePlaylist(Playlist playlist) {
        checkNotNull(playlist);
        ContentValues values = new ContentValues();
        values.put(PlaylistPersistenceContract.PlaylistEntry._ID, playlist.getId());
        values.put(PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_NAME, playlist.getName());
        values.put(PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_VIDEOS, getVideos(playlist.getVideos()));
        mDatabaseHelper
                .insert(PlaylistPersistenceContract.PlaylistEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @NonNull
    private Video getVideo(@NonNull Cursor c) {
        String itemId = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.VideoPersistenceEntry._ID));
        String name = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_NAME));
        long date =
                c.getLong(c.getColumnIndexOrThrow(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_DATE));
        long length =
                c.getLong(c.getColumnIndexOrThrow(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_LENGTH));
        String uri =
                c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.VideoPersistenceEntry.COLUMN_NAME_URI));

        return new Video(itemId, name, uri, date, length);
    }

    @NonNull
    private Playlist getPlaylist(@NonNull Cursor c) {
        String itemId = c.getString(c.getColumnIndexOrThrow(PlaylistPersistenceContract.PlaylistEntry._ID));
        String name = c.getString(c.getColumnIndexOrThrow(PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_NAME));
        String videosString = c.getString(c.getColumnIndexOrThrow(PlaylistPersistenceContract.PlaylistEntry.COLUMN_NAME_VIDEOS));
        List<Video> videos = getVideos(videosString);
        return new Playlist(itemId, name, videos);
    }

    private List<Video> getVideos(String selectionArgs) {
        String sql = String.format(
                "SELECT %s FROM %s WHERE %s IN (%s)",
                TextUtils.join(",", VIDEO_PROJECTION),
                VideoPersistenceContract.VideoPersistenceEntry.TABLE_NAME,
                VideoPersistenceContract.VideoPersistenceEntry._ID,
                selectionArgs);
        List<Video> result = new ArrayList<>();
        Cursor c = mDatabaseHelper.query(sql);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                result.add(getVideo(c));
            }
        }
        if (c != null) {
            c.close();
        }
        return result;
    }

    private String getVideos(List<Video> videos) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < videos.size(); i++) {
            Video video = videos.get(i);
            builder.append(video.getId());
            if (i != videos.size() - 1)
                builder.append(VIDEO_SEPARATOR);
        }
        return builder.toString();
    }
}
