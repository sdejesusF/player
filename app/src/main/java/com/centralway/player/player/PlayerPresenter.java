package com.centralway.player.player;

import android.support.annotation.NonNull;
import android.util.Log;

import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.utils.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlayerPresenter implements PlayerContract.Presenter {

    @NonNull
    private PlayerContract.View mView;

    @NonNull
    private PlaylistRepository mPlaylistRepository;

    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    private List<Video> mList = new ArrayList<>();

    @NonNull
    private String mVideoUri;
    private String mPlaylistId;
    private int mCurrentIndexVideo;

    public PlayerPresenter(
            @NonNull String videoId,
            String playlistId,
            @NonNull PlayerContract.View view,
            @NonNull PlaylistRepository playlistRepository,
            @NonNull BaseSchedulerProvider schedulerProvider){
        mSubscriptions = new CompositeSubscription();
        mVideoUri = checkNotNull(videoId, "VideoId cannot be null");
        mSchedulerProvider  = checkNotNull(schedulerProvider, "Scheduler cannot be null");
        mPlaylistId = playlistId;
        mView = checkNotNull(view, "View cannot be null");
        mPlaylistRepository = checkNotNull(playlistRepository, "Repository cannot be null");
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if(mPlaylistId == null) loadVideos();
        else loadVideosFromPlaylist();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void goNextVideo() {
        if(mCurrentIndexVideo < mList.size() - 1){
            mCurrentIndexVideo = mCurrentIndexVideo + 1;
        }else{
            mCurrentIndexVideo = 0;
        }
        mView.loadVideo(mList.get(mCurrentIndexVideo).getUri(), mPlaylistId);
    }

    @Override
    public void goBackVideo() {
        if(mCurrentIndexVideo > 1){
            mCurrentIndexVideo = mCurrentIndexVideo - 1;
        }else{
            mCurrentIndexVideo = mList.size() - 1;
        }
        mView.loadVideo(mList.get(mCurrentIndexVideo).getUri(), mPlaylistId);

    }

    @Override
    public void deleteNotFoundVideo(String videoUri) {
        mPlaylistRepository.deleteVideoFromUri(videoUri);
    }

    private void loadVideos(){
        mSubscriptions.clear();
        Subscription subscription = mPlaylistRepository
                .getVideos()
                .flatMap(videos -> Observable.from(videos))
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::saveVideos,
                        error -> mView.showErrorLoading(),
                        () -> {}
                );
        mSubscriptions.add(subscription);
    }
    private void loadVideosFromPlaylist(){
        mSubscriptions.clear();
        Subscription subscription = mPlaylistRepository
                .getVideosPlaylist(mPlaylistId)
                .flatMap(videos -> Observable.from(videos))
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::saveVideos,
                        error -> mView.showErrorLoading(),
                        () -> {}
                );
        mSubscriptions.add(subscription);
    }
    private void saveVideos(List<Video> videos){
        mList = videos;
        mCurrentIndexVideo = getCurrentVideoPosition();
    }
    private int getCurrentVideoPosition(){
        for(int i = 0; i < mList.size(); i++){
            if(mList.get(i).getUri().equalsIgnoreCase(mVideoUri)){
                return i;
            }
        }
        return -1;
    }
}
