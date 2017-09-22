package com.centralway.player;

import android.content.Context;
import android.support.annotation.NonNull;

import com.centralway.player.data.source.PhoneMockDataSource;
import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.data.source.SettingRepository;
import com.centralway.player.data.source.local.PlaylistLocalDataSource;
import com.centralway.player.data.source.local.SettingLocalDataSource;
import com.centralway.player.data.source.phone.PlaylistPhoneDataSource;
import com.centralway.player.utils.BaseSchedulerProvider;
import com.centralway.player.utils.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class Injection {
    public static SettingRepository provideSettingRepository(@NonNull Context context) {
        checkNotNull(context);
        return SettingRepository.getInstance(
                SettingLocalDataSource.getInstance(context));
    }

    public static PlaylistRepository providePlaylistRepository(@NonNull Context context) {
        checkNotNull(context);
        return PlaylistRepository.getInstance(
                PhoneMockDataSource.getInstance(),
                PlaylistPhoneDataSource.getInstance(context));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
