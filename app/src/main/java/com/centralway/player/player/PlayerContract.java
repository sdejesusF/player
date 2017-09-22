package com.centralway.player.player;

import com.centralway.player.BaseReactivePresenter;
import com.centralway.player.BaseView;
import com.centralway.player.data.Video;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public interface PlayerContract {

    interface View extends BaseView<Presenter>{

        void loadVideo(String videoId, String playlistId);

        void showErrorLoading();
    }

    interface Presenter extends BaseReactivePresenter{

        void goNextVideo();

        void goBackVideo();

        void deleteNotFoundVideo(String videoUri);

    }
}
