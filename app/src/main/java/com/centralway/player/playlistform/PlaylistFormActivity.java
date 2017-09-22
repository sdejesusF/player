package com.centralway.player.playlistform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.centralway.player.BaseActivity;
import com.centralway.player.Injection;
import com.centralway.player.R;
import com.centralway.player.adapter.VideoAdapter;
import com.centralway.player.data.Video;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public class PlaylistFormActivity extends BaseActivity implements PlaylistFormContract.View, VideoAdapter.VideoItemListener {

    private static final String EXTRA_PLAYLIST_ID = "EXTRA_PLAYLIST_ID";
    private PlaylistFormContract.Presenter mPresenter;
    private AppCompatEditText mName;
    private VideoAdapter mAdapter;
    private RecyclerView mList;
    private TextView mEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_form);
        String playlistID = getIntent().getStringExtra(EXTRA_PLAYLIST_ID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mName = (AppCompatEditText) findViewById(R.id.activity_playlist_form_name);
        mEmpty = (TextView) findViewById(R.id.activity_playlist_form_no_videos);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.bottom_navigation_menu_playlist));
        initList();
        new PlaylistFormPresenter(
                playlistID,
                this,
                Injection.providePlaylistRepository(getApplicationContext()),
                Injection.provideSchedulerProvider());
    }

    private void initList() {
        mAdapter = new VideoAdapter(new ArrayList<Video>(), this, getApplicationContext());
        GridLayoutManager layoutGrid = new GridLayoutManager(this, 2);
        mList = (RecyclerView) findViewById(R.id.activity_playlist_form_list);
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
    public void setPresenter(PlaylistFormContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showName(String name) {
        mName.setText(name);
    }

    @Override
    public void showVideos(List<Video> videos) {
        displayNotificationWrapper(false);
        mAdapter.replaceData(videos);
    }

    @Override
    public void showAndExitSaved() {
        Toast.makeText(getApplicationContext(), getString(R.string.notification_saved), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showNoVideos() {
        displayNotificationWrapper(true);
    }

    @Override
    public void showErrorName(boolean value) {
        if(value){
            mName.setError(getString(R.string.error_required));
        }else{
            mName.setError(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playlist_form_menu, menu);
        return true;
    }


    public static Intent getIntentPlaylistForm(Context context, String id){
        Intent i = new Intent(context, PlaylistFormActivity.class);
        i.putExtra(EXTRA_PLAYLIST_ID, id);
        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.playlist_form_menu_save:
                mPresenter.savePlaylist(mName.getText().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVideoClick(Video clickedVideo) {
        Toast.makeText(getApplicationContext(), getString(R.string.notification_long_press_remove), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoLongClick(Video longClickedVideo) {
        mPresenter.deleteVideo(longClickedVideo);
    }

    @Override
    public void onItemSelected(Video selectedVideo) {

    }

    @Override
    public void onItemUnSelected(Video selectedVideo) {

    }

    private void displayNotificationWrapper(boolean show) {
        if (show) {
            mEmpty.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
        } else {
            mEmpty.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
        }
    }
}
