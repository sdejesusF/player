package com.centralway.player.playlist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.utils.BaseSchedulerProvider;

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

public class PlaylistPresenter implements PlaylistContract.Presenter{

    @NonNull
    PlaylistContract.View mView;

    @NonNull
    PlaylistRepository mPlaylistRepository;

    @NonNull
    CompositeSubscription mSubscriptions;

    @NonNull
    BaseSchedulerProvider mSchedulerProvider;


    public PlaylistPresenter(
            PlaylistContract.View view,
            PlaylistRepository playlistRepository,
            BaseSchedulerProvider schedulerProvider){
        mView = checkNotNull(view, "View cannot be null");
        mPlaylistRepository = checkNotNull(playlistRepository, "Repository cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "Scheduler cannot be null!");

        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }
    @Override
    public void subscribe() {
        loadPlaylist();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void playlistDetail(Playlist playlist) {
        if(playlist == null) mView.showAddOrEditPlaylist(null);
        else mView.showAddOrEditPlaylist(playlist.getId());
    }

    @Override
    public void playlistDelete(Playlist playlist) {
        mPlaylistRepository.deletePlaylist(playlist.getId());
        mView.showDeleted();
        loadPlaylist();
    }

    @Override
    public void playlistPlay(Playlist playlist) {
        if(playlist.getVideos().isEmpty()){
            mView.showNoVideos();
        }else{
            mView.showPlayer(playlist.getVideos().get(0).getUri(), playlist.getId());
        }
    }

    private void loadPlaylist(){
        mSubscriptions.clear();
        Subscription subscription = mPlaylistRepository
                .getPlaylist()
                .flatMap(playlist -> Observable.from(playlist))
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::processPlaylist,
                        error -> mView.showErrorLoading(),
                        () -> {}
                );
        mSubscriptions.add(subscription);
    }
    private void processPlaylist(List<Playlist> playlist){
        if(playlist.isEmpty()){
            mView.showEmpty();
        }else{
            mView.showPlaylist(playlist);
        }
    }
}
