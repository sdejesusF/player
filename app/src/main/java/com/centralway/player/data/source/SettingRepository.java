package com.centralway.player.data.source;

import android.support.annotation.NonNull;

import com.centralway.player.data.source.local.SettingLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class SettingRepository implements SettingDataSource {

    private static SettingRepository INSTANCE;
    private SettingLocalDataSource mLocalSettingSource;

    private SettingRepository(@NonNull SettingLocalDataSource localDataSource) {
        mLocalSettingSource = checkNotNull(localDataSource, "Data source cannot be null");
    }

    public static SettingRepository getInstance(SettingLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new SettingRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void setNoFirstTimePermission() {
        mLocalSettingSource.setNoFirstTimePermission();
    }

    @Override
    public boolean isFirstTimePermission() {
        return mLocalSettingSource.isFirstTimePermission();
    }
}
