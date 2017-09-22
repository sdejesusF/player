package com.centralway.player.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.centralway.player.BaseNavigationActivity;
import com.centralway.player.Injection;
import com.centralway.player.R;
import com.centralway.player.adapter.VideoAdapter;
import com.centralway.player.data.Video;
import com.centralway.player.playlistselector.PlaylistSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import static com.centralway.player.player.PlayerActivity.createPlayer;
import static com.centralway.player.playlistselector.PlaylistSelectorActivity.getIntentSelectorActivity;

/**
 * Created by sergiodejesus on 6/24/17.
 */
public class HomeActivity extends BaseNavigationActivity implements
        HomeContract.View,
        VideoAdapter.VideoItemListener,
        ActionMode.Callback{

    public static final int REQUEST_CODE = 99;
    private HomeContract.Presenter mPresenter;
    private VideoAdapter mAdapter;
    private RecyclerView mList;
    private SwipeRefreshLayout mSwipe;
    private FrameLayout mNotificationWrapper;
    private Toolbar mToolBar;
    private ActionMode mActionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HomePresenter(
                Injection.providePlaylistRepository(getApplicationContext()),
                this,
                Injection.provideSchedulerProvider());

        mSwipe = (SwipeRefreshLayout) findViewById(R.id.activity_home_loading);
        mNotificationWrapper = (FrameLayout) findViewById(R.id.activity_home_notification_wrapper);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        initList(R.layout.item_video);

        mSwipe.setEnabled(false);
    }

    private void initList(int layout) {
        mAdapter = new VideoAdapter(new ArrayList<>(), this, getApplicationContext(), layout);
        GridLayoutManager layoutGrid = new GridLayoutManager(this, 2);
        mList = (RecyclerView) findViewById(R.id.activity_home_list);
        mList.setLayoutManager(layoutGrid);
        mList.setAdapter(mAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mPresenter.loadVideos(true);
                break;
            case R.id.action_select:
                mPresenter.startActionMode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.action_home;
    }

    @Override
    public int getTitleId() {
        return R.string.bottom_navigation_menu_home;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showVideos(List<Video> videos) {
        displayNotificationWrapper(false);
        mAdapter.replaceData(videos);
    }

    @Override
    public void showLoading() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showErrorLoading() {
        displayNotificationWrapper(true);
        setNotificationMessage(getString(R.string.error_unknown));
    }

    @Override
    public void showNoVideos() {
        displayNotificationWrapper(true);
        setNotificationMessage(getString(R.string.error_no_videos));
    }

    @Override
    public void showPlaylist() {
        startActivityForResult(getIntentSelectorActivity(getApplicationContext()), REQUEST_CODE);
    }

    @Override
    public void launchPlayer(Video video) {
        startActivity(createPlayer(getApplicationContext(), video.getUri(), null));
    }

    @Override
    public void dismissActionMode() {
        initList(R.layout.item_video);
        mActionMode.finish();
    }

    @Override
    public void showActionMode() {
        initList(R.layout.item_video_selectable);
        mActionMode = startSupportActionMode(this);
    }

    @Override
    public void onVideoClick(Video clickedVideo) {
        mPresenter.playRequired(clickedVideo);
    }

    @Override
    public void onVideoLongClick(Video longClickedVideo) {
    }

    @Override
    public void onItemSelected(Video selectedVideo) {
        mPresenter.itemSelected(selectedVideo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String playlistId = data.getStringExtra(PlaylistSelectorActivity.EXTRA_PLAYLIST_SELECTED);
            mPresenter.addToPlaylist(playlistId);
        }
    }

    @Override
    public void onItemUnSelected(Video selectedVideo) {
        mPresenter.itemUnselected(selectedVideo);
    }

    private void displayNotificationWrapper(boolean show) {
        if (show) {
            mNotificationWrapper.setVisibility(View.VISIBLE);
            mSwipe.setVisibility(View.GONE);
        } else {
            mNotificationWrapper.setVisibility(View.GONE);
            mSwipe.setVisibility(View.VISIBLE);
        }
    }

    private void setNotificationMessage(String id) {
        ((TextView) findViewById(R.id.activity_home_notification)).setText(id);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.home_menu_contextual, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_playlist:
                mPresenter.addPlaylistRequired();
                break;

        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mPresenter.endActionMode();
    }
}
