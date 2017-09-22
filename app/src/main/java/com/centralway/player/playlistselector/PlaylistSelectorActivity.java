package com.centralway.player.playlistselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.centralway.player.Injection;
import com.centralway.player.R;
import com.centralway.player.adapter.PlaylistAdapter;
import com.centralway.player.data.Playlist;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistSelectorActivity extends AppCompatActivity implements
        PlaylistSelectorContract.View,
        PlaylistAdapter.PlaylistItemListener, View.OnClickListener {

    public static final String EXTRA_PLAYLIST_SELECTED = "EXTRA_PLAYLIST_SELECTED";

    private PlaylistAdapter mAdapter;
    private RecyclerView mList;
    private PlaylistSelectorContract.Presenter mPresenter;
    private Button mFromNew;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_selector);
        setTitle(getString(R.string.screen_playlist_selector_title));
        mFromNew = (Button) findViewById(R.id.activity_playlist_selector_new);
        mFromNew.setOnClickListener(this);
        initList();
        new PlaylistSelectorPresenter(
                this,
                Injection.providePlaylistRepository(getApplicationContext()),
                Injection.provideSchedulerProvider());
    }

    private void initList() {
        mAdapter = new PlaylistAdapter(new ArrayList<>(), this, R.layout.item_playlist_selectable);
        LinearLayoutManager layoutGrid = new LinearLayoutManager(this);
        layoutGrid.setOrientation(LinearLayoutManager.VERTICAL);
        mList = (RecyclerView) findViewById(R.id.activity_playlist_selector_list);
        mList.setLayoutManager(layoutGrid);
        mList.setAdapter(mAdapter);
    }

    public static Intent getIntentSelectorActivity(Context context){
        return new Intent(context, PlaylistSelectorActivity.class);
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
    public void setPresenter(PlaylistSelectorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showList(List<Playlist> playlist) {
        mAdapter.replaceData(playlist);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void exit(String id){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PLAYLIST_SELECTED, id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPlaylistClick(Playlist clickedPlaylist) {
        mPresenter.playlistSelected(clickedPlaylist);
    }

    @Override
    public void onPlaylistPlay(Playlist clickedPlaylist) {

    }

    @Override
    public void onPlaylistDelete(Playlist clickedPlaylist) {

    }

    @Override
    public void onClick(View v) {
        mPresenter.playlistSelected(null);
    }
}
