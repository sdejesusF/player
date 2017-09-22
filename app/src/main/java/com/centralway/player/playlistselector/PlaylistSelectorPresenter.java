package com.centralway.player.playlistselector;

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
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistSelectorPresenter implements PlaylistSelectorContract.Presenter{

    @NonNull
    private PlaylistRepository mPlaylistRepository;

    @NonNull
    private PlaylistSelectorContract.View mView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;


    public PlaylistSelectorPresenter(
            @NonNull PlaylistSelectorContract.View view,
            @NonNull PlaylistRepository playlistRepository,
            @NonNull BaseSchedulerProvider schedulerProvider
            ){
        mPlaylistRepository = checkNotNull(playlistRepository, "Repository cannot be null");
        mView = checkNotNull(view, "View cannot be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "Scheduler cannot be null");
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
    public void playlistSelected(Playlist playlist) {
        String id = playlist == null ? null : playlist.getId();
        mView.exit(id);
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
                        error -> {},
                        () -> {}
                );
        mSubscriptions.add(subscription);
    }
    private void processPlaylist(List<Playlist> playlist){
        if(playlist.isEmpty()){
            mView.showEmpty();
        }else{
            mView.showList(playlist);
        }
    }
}
