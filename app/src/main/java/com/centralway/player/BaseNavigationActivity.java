package com.centralway.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.centralway.player.home.HomeActivity;
import com.centralway.player.playlist.PlaylistActivity;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public abstract class BaseNavigationActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private BottomNavigationView mNavigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mNavigation.setOnNavigationItemSelectedListener(this);

        setSupportActionBar(mToolbar);
        setTitle(getTitleId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = mNavigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent activityAboutToStart = null;
        switch (item.getItemId()) {
            case R.id.action_home:
                activityAboutToStart = new Intent(getApplicationContext(), HomeActivity.class);
                break;
            case R.id.action_playlist:
                activityAboutToStart = new Intent(getApplicationContext(), PlaylistActivity.class);
                break;
        }
        if (activityAboutToStart != null && item.getItemId() != getNavigationMenuItemId()) {
            startActivity(activityAboutToStart);
            overridePendingTransition(0, 0);
        }
        return true;
    }

    public abstract int getContentViewId();

    public abstract int getNavigationMenuItemId();

    public abstract int getTitleId();
}