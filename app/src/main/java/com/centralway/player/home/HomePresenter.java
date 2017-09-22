package com.centralway.player.home;

import android.support.annotation.NonNull;
import android.util.Log;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.utils.BaseSchedulerProvider;
import com.centralway.player.utils.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class HomePresenter implements HomeContract.Presenter {

    @NonNull
    PlaylistRepository mPlaylistRepository;
    @NonNull
    HomeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private List<Video> mVideos = new ArrayList<>();
    private List<Video> mVideosSelected = new ArrayList<>();

    private BaseSchedulerProvider mSchedulerProvider;

    public HomePresenter(@NonNull PlaylistRepository playlistRepository,
                         @NonNull HomeContract.View view,
                         @NonNull BaseSchedulerProvider schedulerProvider) {
        mPlaylistRepository = checkNotNull(playlistRepository, "Repository cannot be null!");
        mView = checkNotNull(view, "View cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "Schedule cannot be null!");

        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);

    }

    @Override
    public void subscribe() {
        loadVideos(false);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadVideos(boolean forceUpdate) {
        loadVideos(forceUpdate, true);
    }

    @Override
    public void playRequired(Video video) {
        mView.launchPlayer(video);
    }

    @Override
    public void addPlaylistRequired() {
        mView.showPlaylist();
    }

    @Override
    public void endActionMode() {
        mView.dismissActionMode();
        mView.showVideos(mVideos);
    }

    @Override
    public void startActionMode() {
        mView.showActionMode();
        mView.showVideos(mVideos);
    }

    @Override
    public void itemSelected(Video video) {
        mVideosSelected.add(video);
    }

    @Override
    public void itemUnselected(Video video) {
        mVideosSelected.remove(video);
    }

    @Override
    public void addToPlaylist(String playlistId) {
        endActionMode();
        mPlaylistRepository.addVideos(mVideosSelected, playlistId);
        mVideosSelected = new ArrayList<>();
    }

    private void loadVideos(boolean forceUpdate, boolean showInUI) {
        if (forceUpdate) {
            mPlaylistRepository.refreshVideos();
        }
        if (showInUI) {
            mView.showLoading();
        }
        mSubscriptions.clear();
        Subscription subscription = mPlaylistRepository
                .getVideos()
                .flatMap(videos -> Observable.from(videos))
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::processVideos,
                        error -> mView.showErrorLoading(),
                        () -> mView.hideLoading()
                );
        mSubscriptions.add(subscription);
    }

    private void processVideos(List<Video> videos) {
        mVideos = videos;
        if (videos.isEmpty()) {
            mView.showNoVideos();
        } else {
            mView.showVideos(videos);
        }

    }
}
