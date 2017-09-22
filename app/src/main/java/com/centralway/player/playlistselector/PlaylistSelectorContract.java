package com.centralway.player.playlistselector;

import com.centralway.player.BaseReactivePresenter;
import com.centralway.player.BaseView;
import com.centralway.player.data.Playlist;

import java.util.List;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public interface PlaylistSelectorContract {

    interface View extends BaseView<Presenter>{

        void showList(List<Playlist> playlist);

        void showEmpty();

        void exit(String id);
    }
    interface Presenter extends BaseReactivePresenter{

        void playlistSelected(Playlist playlist);

    }
}
