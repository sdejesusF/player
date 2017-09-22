package com.centralway.player.data.source;

import android.content.Context;
import android.util.Log;

import com.centralway.player.data.Video;
import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistRepositoryTest {

    private static final String VIDEO_ID_1 = "1";
    private static final String VIDEO_ID_2 = "2";
    private static final String VIDEO_ID_3 = "3";
    private static final String VIDEO_ID_4 = "4";

    private static List<Video> VIDEOS = Lists.newArrayList(
            new Video(VIDEO_ID_1, "videoTest1", "uri1", 0, 200),
            new Video(VIDEO_ID_2, "videoTest1", "uri2", 0, 200),
            new Video(VIDEO_ID_3, "videoTest1", "uri3", 0, 200),
            new Video(VIDEO_ID_4, "videoTest1", "uri4", 0, 200));

    private PlaylistRepository mPlaylistRepository;

    private TestSubscriber<List<Video>> mVideoTestSubscriber;

    @Mock
    private PlaylistDataSource mPhoneDataSource;

    @Mock
    private PlaylistDataSource mLocalDataSource;

    @Before
    public void setupVideosRepository() {

        MockitoAnnotations.initMocks(this);

        mPlaylistRepository = PlaylistRepository.getInstance(
                mLocalDataSource, mPhoneDataSource);

        mVideoTestSubscriber = new TestSubscriber<>();
    }
    @After
    public void destroyRepositoryInstance() {
        PlaylistRepository.destroyInstance();
    }
    @Test
    public void getVideos_repositoryCachesAfterFirstSubscription_whenVideosAvailableInLocalStorage() {

        setVideosAvailable(mLocalDataSource, VIDEOS);

        setVideoNotAvailable(mPhoneDataSource);

        TestSubscriber<List<Video>> testSubscriber1 = new TestSubscriber<>();
        mPlaylistRepository.getVideos().subscribe(testSubscriber1);

        TestSubscriber<List<Video>> testSubscriber2 = new TestSubscriber<>();
        mPlaylistRepository.getVideos().subscribe(testSubscriber2);

        assertTrue(mPlaylistRepository.mIsCacheDirty);
        testSubscriber1.assertValue(VIDEOS);
        testSubscriber2.assertValue(VIDEOS);
    }
    @Test
    public void getVideos_requestsAllVideosFromLocalDataSource() {

        setVideosAvailable(mLocalDataSource, VIDEOS);

        setVideosNotAvailable(mPhoneDataSource);

        mPlaylistRepository.getVideos().subscribe(mVideoTestSubscriber);

        verify(mLocalDataSource).getVideos();
        mVideoTestSubscriber.assertValue(VIDEOS);
    }

    @Test
    public void getVideo_requestLocalVideoById() {

        setVideosAvailable(mLocalDataSource, VIDEOS);

        setVideosNotAvailable(mPhoneDataSource);

        TestSubscriber<List<Video>> testSubscriber = new TestSubscriber<>();

        mPlaylistRepository
                .getVideos()
                .subscribe(testSubscriber);

        testSubscriber.assertValue(VIDEOS);
    }

    @Test
    public void getVideoWithBothDataSourcesUnavailable_firesNoError() {

        final String videoId = "999";

        setVideoNotAvailable(mLocalDataSource, videoId);

        setVideoNotAvailable(mPhoneDataSource, videoId);

        TestSubscriber<Video> testSubscriber = new TestSubscriber<>();
        mPlaylistRepository.getVideo(videoId).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    @Test
    public void getVideoWithBothDataSourcesAvailable_firesNoError() {

        final String videoId = "999";

        setVideosAvailable(mLocalDataSource, VIDEOS);

        setVideosAvailable(mPhoneDataSource, VIDEOS);

        TestSubscriber<Video> testSubscriber = new TestSubscriber<Video>();
        mPlaylistRepository.getVideo(videoId).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    private void setVideosNotAvailable(PlaylistDataSource dataSource) {
        when(dataSource.getVideos()).thenReturn(Observable.just(Collections.<Video>emptyList()));
    }
    private void setVideoNotAvailable(PlaylistDataSource dataSource) {
        when(dataSource.getVideos()).thenReturn(Observable.just(Collections.<Video>emptyList()));
    }
    private void setVideosAvailable(PlaylistDataSource dataSource, List<Video> videos) {
        // don't allow the data sources to complete.
        when(dataSource.getVideos()).thenReturn(Observable.just(videos).concatWith(Observable.<List<Video>>never()));
    }

    private void setVideoNotAvailable(PlaylistDataSource dataSource, String videoId) {
        when(dataSource.getVideo(eq(videoId))).thenReturn(Observable.<Video>just(null).concatWith(Observable.<Video>never()));
    }

    private void setVideoAvailable(PlaylistDataSource dataSource, Video video) {
        when(dataSource.getVideo(eq(video.getId()))).thenReturn(Observable.just(video).concatWith(Observable.<Video>never()));
    }


}
