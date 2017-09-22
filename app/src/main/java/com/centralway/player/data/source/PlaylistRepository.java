package com.centralway.player.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.local.PlaylistLocalDataSource;
import com.centralway.player.data.source.phone.PlaylistPhoneDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlaylistRepository implements PlaylistDataSource {

    private static PlaylistRepository INSTANCE;

    @Nullable
    private Map<String, Video> mCachedVideos;

    private PlaylistDataSource mLocalPlaylistDataSource;
    private PlaylistDataSource mPhonePlaylistDataSource;

    @VisibleForTesting
    boolean mIsCacheDirty = true;

    private PlaylistRepository(@NonNull PlaylistDataSource localPlaylistDataSource,
                               @NonNull PlaylistDataSource phonePlaylistDataSource
    ) {
        mLocalPlaylistDataSource = checkNotNull(localPlaylistDataSource, "Local source cannot be null!");
        mPhonePlaylistDataSource = checkNotNull(phonePlaylistDataSource, "Phone source cannot be null!");
    }

    public static PlaylistRepository getInstance(@NonNull PlaylistDataSource localPlaylistDataSource,
                                                 @NonNull PlaylistDataSource phonePlaylistDataSource
    ) {
        if (INSTANCE == null) {
            INSTANCE = new PlaylistRepository(localPlaylistDataSource, phonePlaylistDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Video>> getVideos() {

        if (mCachedVideos != null && !mIsCacheDirty) {
            return Observable.from(mCachedVideos.values()).toList();
        } else if (mCachedVideos == null) {
            mCachedVideos = new LinkedHashMap<>();
        }

        Observable<List<Video>> remoteVideos = getPhoneVideosAndSaveThemInLocal();
        Observable<List<Video>> localVideos = getAndCacheLocalVideos();

        return Observable.concat(localVideos, remoteVideos)
                .filter(videos -> !videos.isEmpty())
                .first();

    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
    @Override
    public Observable<List<Video>> getVideosPlaylist(String playList) {
        return mLocalPlaylistDataSource.getVideosPlaylist(playList);
    }

    @Override
    public Observable<List<Playlist>> getPlaylist() {
        return mLocalPlaylistDataSource.getPlaylist();
    }

    @Override
    public Observable<Playlist> getPlaylist(String id) {
        return mLocalPlaylistDataSource.getPlaylist(id);
    }

    @Override
    public Observable<Video> getVideo(String id) {
        final Video cachedVideo = getVideoWithId(id);
        if (cachedVideo != null) {
            return Observable.just(cachedVideo);
        }
        return mLocalPlaylistDataSource.getVideo(id);
    }

    @Override
    public void refreshVideos() {
        mIsCacheDirty = true;
    }

    @Override
    public void addVideo(Video video) {
        mLocalPlaylistDataSource.addVideo(video);
    }

    @Override
    public void addVideo(Video video, String playListID) {
        mLocalPlaylistDataSource.addVideo(video, playListID);
    }

    @Override
    public void addVideo(String videoId, String playListID) {
        mLocalPlaylistDataSource.addVideo(videoId, playListID);
    }

    @Override
    public void addVideo(String videoId, Playlist playListID) {
        mLocalPlaylistDataSource.addVideo(videoId, playListID);
    }

    @Override
    public void addVideo(Video videoId, Playlist playlist) {
        mLocalPlaylistDataSource.addVideo(videoId, playlist);
    }

    @Override
    public void addVideos(List<Video> videos, String playListID) {
        if(playListID != null)
            mLocalPlaylistDataSource.addVideos(videos, playListID);
        else{
            Playlist newPlayList = new Playlist("Playlist",videos);
            mLocalPlaylistDataSource.addEditPlaylist(newPlayList);
        }
    }

    @Override
    public void addEditPlaylist(Playlist playlist) {
        mLocalPlaylistDataSource.addEditPlaylist(playlist);
    }

    @Override
    public void deletePlaylist(String playlistID) {
        mLocalPlaylistDataSource.deletePlaylist(playlistID);
    }

    @Override
    public void deleteVideo(String videoId) {
        mLocalPlaylistDataSource.deleteVideo(videoId);
    }

    @Override
    public void deleteVideoFromUri(String videoUri) {
        mLocalPlaylistDataSource.deletePlaylist(videoUri);
    }

    private Observable<List<Video>> getAndCacheLocalVideos() {
        return mLocalPlaylistDataSource.getVideos()
                .flatMap((List<Video> videos) ->
                        Observable.from(videos)
                                .doOnNext(video -> mCachedVideos.put(video.getId(), video))
                                .toList()
                );
    }

    private Observable<List<Video>> getPhoneVideosAndSaveThemInLocal() {
        return mPhonePlaylistDataSource
                .getVideos()
                .flatMap(
                        (List<Video> videos) ->
                                Observable.from(videos).doOnNext(video -> {
                                    mLocalPlaylistDataSource.addVideo(video);
                                    mCachedVideos.put(video.getId(), video);
                                }).toList()
                )
                .doOnCompleted(() -> mIsCacheDirty = false);
    }
    @Nullable
    private Video getVideoWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedVideos == null || mCachedVideos.isEmpty()) {
            return null;
        } else {
            return mCachedVideos.get(id);
        }
    }

}
