package com.centralway.player.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.centralway.player.data.source.SettingDataSource;
import com.centralway.player.utils.Constants;

import static com.centralway.player.utils.Preferences.FIRST_TIME_PERMISSIONS;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class SettingLocalDataSource implements SettingDataSource {

    private static SettingLocalDataSource INSTANCE;

    private SharedPreferences mPreferences;

    private SettingLocalDataSource(Context context) {
        checkNotNull(context);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SettingLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void setNoFirstTimePermission() {
        saveFirstTimePermissionsPreference(false);
    }

    @Override
    public boolean isFirstTimePermission() {
        return getFirstTimePermissionsPreference();
    }

    private boolean getFirstTimePermissionsPreference() {
        return mPreferences.getBoolean(FIRST_TIME_PERMISSIONS, Constants.BASE_FIRST_TIME_PERMISSIONS);
    }

    private void saveFirstTimePermissionsPreference(boolean value) {
        mPreferences.edit().putBoolean(FIRST_TIME_PERMISSIONS,
                value).apply();
    }
}
