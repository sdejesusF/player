package com.centralway.player.data.source;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;

import java.util.List;

import rx.Observable;

/**
 * Created by sergiodejesus on 6/27/17.
 */

public class PhoneMockDataSource implements PlaylistDataSource {

    public static PhoneMockDataSource getInstance(){
        return null;
    }
    @Override
    public Observable<List<Video>> getVideos() {
        return null;
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
}
