package com.centralway.player.playlist;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.home.HomeContract;
import com.centralway.player.home.HomePresenter;
import com.centralway.player.utils.ImmediateSchedulerProvider;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistPresenterTest {

    @Mock
    private PlaylistRepository mVideosRepository;

    @Mock
    private PlaylistContract.View mPlaylistView;

    private PlaylistPresenter mPlaylistPresenter;

    private ImmediateSchedulerProvider mSchedulerProvider;

    @Before
    public void setupHomePresenter() {

        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mPlaylistPresenter = new PlaylistPresenter(mPlaylistView, mVideosRepository, mSchedulerProvider);

    }
    @Test
    public void tryLoadPlaylistWithVideos() {

        Playlist playlist = new Playlist("new playlist", new ArrayList<>());

        mPlaylistPresenter.playlistPlay(playlist);

        verify(mPlaylistView).showNoVideos();
    }

    @Test
    public void clickOnFab_ShowsAddPlaylistUi() {

        mPlaylistPresenter.playlistDetail(null);

        verify(mPlaylistView).showAddOrEditPlaylist(null);
    }

    @Test
    public void clickOnFab_ShowsEditPlaylistUi() {

        Playlist test = new Playlist("test", new ArrayList<>());
        mPlaylistPresenter.playlistDetail(test);

        verify(mPlaylistView).showAddOrEditPlaylist(test.getId());
    }

}
