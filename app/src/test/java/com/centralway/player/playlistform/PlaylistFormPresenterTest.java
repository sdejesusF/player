package com.centralway.player.playlistform;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.playlist.PlaylistContract;
import com.centralway.player.playlist.PlaylistPresenter;
import static org.mockito.Matchers.any;
import com.centralway.player.utils.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistFormPresenterTest {

    @Mock
    private PlaylistRepository mVideosRepository;

    @Mock
    private PlaylistFormContract.View mPlaylistView;

    private PlaylistFormPresenter mPlaylistPresenter;

    private ImmediateSchedulerProvider mSchedulerProvider;

    @Before
    public void setupHomePresenter() {

        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();

    }
    @Test
    public void saveNewPlaylistToRepository_showsSuccessMessageUi() {

        mPlaylistPresenter = new PlaylistFormPresenter(
                null, mPlaylistView, mVideosRepository, mSchedulerProvider);

        mPlaylistPresenter.savePlaylist("new playlist");
        verify(mVideosRepository).addEditPlaylist(any(Playlist.class));
        verify(mPlaylistView).showAndExitSaved();
    }

    @Test
    public void saveEditPlaylistToRepository_showsSuccessMessageUi() {

        mPlaylistPresenter = new PlaylistFormPresenter(
                "1", mPlaylistView, mVideosRepository, mSchedulerProvider);

        mPlaylistPresenter.savePlaylist("new playlist");

        verify(mVideosRepository).addEditPlaylist(any(Playlist.class));
        verify(mPlaylistView).showAndExitSaved(); // shown in the UI
    }
}