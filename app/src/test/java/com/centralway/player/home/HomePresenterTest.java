package com.centralway.player.home;

import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.utils.ImmediateSchedulerProvider;
import com.centralway.player.utils.SchedulerProvider;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class HomePresenterTest {

    private static final String VIDEO_ID_1 = "1";
    private static final String VIDEO_ID_2 = "2";
    private static final String VIDEO_ID_3 = "3";
    private static final String VIDEO_ID_4 = "4";

    private static List<Video> VIDEOS = Lists.newArrayList(
            new Video(VIDEO_ID_1, "videoTest1", "uri1", 0, 200),
            new Video(VIDEO_ID_2, "videoTest2", "uri2", 0, 200),
            new Video(VIDEO_ID_3, "videoTest3", "uri3", 0, 200),
            new Video(VIDEO_ID_4, "videoTest4", "uri4", 0, 200));
    @Mock
    private PlaylistRepository mVideosRepository;

    @Mock
    private HomeContract.View mHomeView;

    private HomePresenter mHomePresenter;

    private ImmediateSchedulerProvider mSchedulerProvider;

    @Before
    public void setupHomePresenter() {

        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mHomePresenter = new HomePresenter(mVideosRepository, mHomeView, mSchedulerProvider);

    }
    @Test
    public void loadAllVideosFromRepositoryAndLoadIntoView() {

        when(mVideosRepository.getVideos()).thenReturn(Observable.just(VIDEOS));

        mHomePresenter.loadVideos(true);

        verify(mHomeView).showLoading();
        verify(mHomeView).hideLoading();
    }
    @Test
    public void errorLoadingVideos_ShowsError() {

        when(mVideosRepository.getVideos()).thenReturn(Observable.<List<Video>>error(new Exception()));

        mHomePresenter.loadVideos(true);

        verify(mHomeView).showErrorLoading();
    }

}
