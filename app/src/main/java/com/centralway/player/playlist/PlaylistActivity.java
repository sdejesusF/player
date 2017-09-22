package com.centralway.player.playlist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.centralway.player.BaseNavigationActivity;
import com.centralway.player.Injection;
import com.centralway.player.adapter.PlaylistAdapter;
import com.centralway.player.R;
import com.centralway.player.data.Playlist;

import java.util.ArrayList;
import java.util.List;

import static com.centralway.player.player.PlayerActivity.createPlayer;
import static com.centralway.player.playlistform.PlaylistFormActivity.getIntentPlaylistForm;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlaylistActivity extends BaseNavigationActivity implements PlaylistContract.View, View.OnClickListener, PlaylistAdapter.PlaylistItemListener {

    PlaylistContract.Presenter mPresenter;
    private FrameLayout mNotificationWrapper;
    private PlaylistAdapter mAdapter;
    private RecyclerView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.activity_playlist_add_fab).setOnClickListener(this);
        mNotificationWrapper = (FrameLayout) findViewById(R.id.activity_playlist_notification_wrapper);

        new PlaylistPresenter(
                this,
                Injection.providePlaylistRepository(getApplicationContext()),
                Injection.provideSchedulerProvider());

        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_playlist;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.action_playlist;
    }

    @Override
    public int getTitleId() {
        return R.string.bottom_navigation_menu_playlist;
    }

    @Override
    public void setPresenter(PlaylistContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAddOrEditPlaylist(String id) {
        startActivity(getIntentPlaylistForm(getApplicationContext(), id));
    }

    @Override
    public void showPlaylist(List<Playlist> playlist) {
        displayNotificationWrapper(false);
        mAdapter.replaceData(playlist);
    }

    @Override
    public void showErrorLoading() {

    }

    @Override
    public void showEmpty() {
        displayNotificationWrapper(true);
    }

    @Override
    public void showNoVideos() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_no_videos_playlist), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPlayer(String videoId, String playlistId) {
        startActivity(createPlayer(getApplicationContext(), videoId, playlistId));
    }

    @Override
    public void showDeleted() {
        Toast.makeText(getApplicationContext(), getString(R.string.notification_deleted), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        mPresenter.playlistDetail(null);
    }

    private void displayNotificationWrapper(boolean show) {
        if (show) {
            mNotificationWrapper.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
        } else {
            mNotificationWrapper.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
        }
    }
    private void initList() {
        mAdapter = new PlaylistAdapter(new ArrayList<>(), this);
        LinearLayoutManager layoutGrid = new LinearLayoutManager(this);
        layoutGrid.setOrientation(LinearLayoutManager.VERTICAL);
        mList = (RecyclerView) findViewById(R.id.activity_playlist_list);
        mList.setLayoutManager(layoutGrid);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onPlaylistClick(Playlist clickedPlaylist) {
        mPresenter.playlistDetail(clickedPlaylist);
    }

    @Override
    public void onPlaylistPlay(Playlist clickedPlaylist) {
        mPresenter.playlistPlay(clickedPlaylist);
    }


    @Override
    public void onPlaylistDelete(Playlist clickedPlaylist) {
        mPresenter.playlistDelete(clickedPlaylist);
    }
}
