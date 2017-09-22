package com.centralway.player.playlistform;

import com.centralway.player.BaseReactivePresenter;
import com.centralway.player.BaseView;
import com.centralway.player.data.Video;

import java.util.List;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistFormContract {

    interface View extends BaseView<Presenter>{

        void showName(String name);

        void showVideos(List<Video> videos);

        void showAndExitSaved();

        void showNoVideos();

        void showErrorName(boolean value);

    }

    interface Presenter extends BaseReactivePresenter{

        void savePlaylist(String playListName);

        void deleteVideo(Video video);
    }
}
