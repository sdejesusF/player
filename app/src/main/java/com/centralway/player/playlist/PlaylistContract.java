package com.centralway.player.playlist;

import com.centralway.player.BaseReactivePresenter;
import com.centralway.player.BaseView;
import com.centralway.player.data.Playlist;

import java.util.List;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public interface PlaylistContract {

    interface View extends BaseView<Presenter>{

        void showAddOrEditPlaylist(String id);

        void showPlaylist(List<Playlist> playlist);

        void showErrorLoading();

        void showEmpty();

        void showNoVideos();

        void showPlayer(String videoId, String playlistId);

        void showDeleted();

    }
    interface Presenter extends BaseReactivePresenter{
        void playlistDetail(Playlist playlist);

        void playlistDelete(Playlist playlist);

        void playlistPlay(Playlist playlist);
    }
}
