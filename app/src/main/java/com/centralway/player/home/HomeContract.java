package com.centralway.player.home;

import com.centralway.player.BaseReactivePresenter;
import com.centralway.player.BaseView;
import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;

import java.util.List;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class HomeContract {

    interface View extends BaseView<Presenter> {

        void showVideos(List<Video> videos);

        void showLoading();

        void hideLoading();

        void showErrorLoading();

        void showNoVideos();

        void showPlaylist();

        void launchPlayer(Video video);

        void dismissActionMode();

        void showActionMode();
    }

    interface Presenter extends BaseReactivePresenter {

        void loadVideos(boolean forceUpdate);

        void playRequired(Video video);

        void addPlaylistRequired();

        void endActionMode();

        void startActionMode();

        void itemSelected(Video video);

        void itemUnselected(Video video);

        void addToPlaylist(String playlistId);

    }
}
