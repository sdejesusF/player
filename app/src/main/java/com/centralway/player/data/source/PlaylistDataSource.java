package com.centralway.player.data.source;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;

import java.util.List;

import rx.Observable;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public interface PlaylistDataSource {

    Observable<List<Video>> getVideos();

    Observable<List<Video>> getVideosPlaylist(String playList);

    Observable<List<Playlist>> getPlaylist();

    Observable<Playlist> getPlaylist(String id);

    Observable<Video> getVideo(String id);

    void refreshVideos();

    void addVideo(Video video);

    void addVideo(Video video, String playListID);

    void addVideo(String videoId, String playListID);

    void addVideo(String videoId, Playlist playListID);

    void addVideo(Video videoId, Playlist playListID);

    void addVideos(List<Video> videos, String playListID);

    void addEditPlaylist(Playlist playlist);

    void deletePlaylist(String playlistID);

    void deleteVideo(String videoId);

    void deleteVideoFromUri(String videoUri);

}
