package com.centralway.player.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.centralway.player.BaseActivity;
import com.centralway.player.Injection;
import com.centralway.player.R;
import com.centralway.player.views.VideoControllerView;

import java.io.IOException;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PlayerActivity extends BaseActivity implements
        PlayerContract.View,
        SurfaceHolder.Callback,
        View.OnTouchListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        VideoControllerView.MediaPlayerControl, MediaPlayer.OnCompletionListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private MediaPlayer mMediaPlayer = null;
    private VideoControllerView mController;
    private SurfaceView mVideoSurface;
    private FrameLayout mSurfaceContainer;
    private String mVideoId;
    private String mPlayListId;
    private AudioManager mAudioManager;
    private PlayerContract.Presenter mPresenter;

    private static final String EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID";
    private static final String EXTRA_PLAYLIST_ID = "EXTRA_PLAYLIST_ID";

    public static Intent createPlayer(
            Context context,
            String videoId,
            String playListId){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_PLAYLIST_ID, playListId);
        return intent;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        mPlayListId = getIntent().getStringExtra(EXTRA_PLAYLIST_ID);
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        setGraphics();

        new PlayerPresenter(
                mVideoId,
                mPlayListId,
                this,
                Injection.providePlaylistRepository(getApplicationContext()),
                Injection.provideSchedulerProvider());
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

    private void setGraphics(){
        mVideoSurface = (SurfaceView) findViewById(R.id.activity_player_surface);
        SurfaceHolder videoHolder = mVideoSurface.getHolder();
        videoHolder.addCallback(this);
        mSurfaceContainer = (FrameLayout) findViewById(R.id.activity_player_container_surface);
        mSurfaceContainer.setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mController = new VideoControllerView(getApplicationContext());
            mController.setPrevNextListeners(v -> nextVideo(),v -> prevVideo());
            mController.setVolumeListeners(v -> volumeUp(), v -> volumeDown());
            try {
                mMediaPlayer.setDataSource(mVideoId);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.i(TAG, mVideoId + " " + e.toString());
                mPresenter.deleteNotFoundVideo(mVideoId);
                mPresenter.goNextVideo();
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            if (mController != null) {
                mController.setVisibility(View.GONE);
                mController.setMediaPlayer(null);
                mController = null;
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mMediaPlayer != null) {
            mController.show();
        }
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mController.setMediaPlayer(this);
        mController.setAnchorView(mSurfaceContainer);
        mMediaPlayer.start();
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    private void nextVideo(){
        mPresenter.goNextVideo();
    }

    private void prevVideo(){
        mPresenter.goBackVideo();
    }

    private void volumeUp(){
        mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
    }

    private void volumeDown(){
        mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    public void setPresenter(PlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadVideo(String videoUri, String playlistId) {
        Log.i(TAG, videoUri);
        Intent i = createPlayer(getApplicationContext(), videoUri, playlistId);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void showErrorLoading() {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPresenter.goNextVideo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
