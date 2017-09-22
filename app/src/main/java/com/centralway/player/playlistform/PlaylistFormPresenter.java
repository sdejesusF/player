package com.centralway.player.playlistform;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.centralway.player.data.Playlist;
import com.centralway.player.data.Video;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.utils.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistFormPresenter implements PlaylistFormContract.Presenter {

    @NonNull
    private PlaylistRepository mPlaylistRepository;

    @NonNull
    private PlaylistFormContract.View mView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    private String mPlaylistId;

    private List<Video> mList = new ArrayList<>();

    public PlaylistFormPresenter(
            @Nullable String playlistId,
            @NonNull PlaylistFormContract.View view,
            @NonNull PlaylistRepository playlistRepository,
            @NonNull BaseSchedulerProvider schedulerProvider){
        mView = checkNotNull(view, "View cannot be null!");
        mPlaylistRepository = checkNotNull(playlistRepository, "Repository cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "Scheduler cannot be null!");
        mSubscriptions = new CompositeSubscription();
        mPlaylistId = playlistId;
        mView.setPresenter(this);
    }
    @Override
    public void subscribe() {
        if(!isNew()){
            loadPlaylist();
        }else{
            mView.showNoVideos();
        }
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void savePlaylist(String playListName) {
        if(playListName.isEmpty()){
            mView.showErrorName(true);
            return;
        }
        mView.showErrorName(false);
        if(isNew()){
            createNewPlaylist(playListName, mList);
        }else{
            editPlaylist(playListName, mList);
        }
    }

    @Override
    public void deleteVideo(Video video) {
        mList.remove(video);
        mView.showVideos(mList);
        displayVideos();
    }

    private void populatePlaylist(Playlist playlist){
        mView.showName(playlist.getName());
        mList = playlist.getVideos();
        displayVideos();
    }

    private void displayVideos(){
        if(!mList.isEmpty()){
            mView.showVideos(mList);
        }else{
            mView.showNoVideos();
        }
    }
    private boolean isNew(){
        return mPlaylistId == null;
    }

    private void createNewPlaylist(String name, List<Video> videos){
        Playlist newPlaylist = new Playlist(name, videos);
        mPlaylistRepository.addEditPlaylist(newPlaylist);
        mView.showAndExitSaved();
    }
    private void editPlaylist(String name, List<Video> videos){
        Playlist editPlaylist = new Playlist(mPlaylistId, name, videos);
        mPlaylistRepository.addEditPlaylist(editPlaylist);
        mView.showAndExitSaved();
    }
    private void loadPlaylist(){
        Subscription subscription = mPlaylistRepository
                .getPlaylist(mPlaylistId)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::populatePlaylist,
                        error -> {},
                        () -> {}
                );
        mSubscriptions.add(subscription);
    }
    private int getIndex(String id){
        for (int i = 0; i < mList.size(); i++){
            if(mList.get(i).getId().equalsIgnoreCase(id)){
                return i;
            }
        }
        return -1;
    }
}
