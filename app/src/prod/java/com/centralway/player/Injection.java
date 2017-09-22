/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centralway.player;

import android.content.Context;
import android.support.annotation.NonNull;

import com.centralway.player.data.source.PlaylistRepository;
import com.centralway.player.data.source.SettingRepository;
import com.centralway.player.data.source.local.PlaylistLocalDataSource;
import com.centralway.player.data.source.local.SettingLocalDataSource;
import com.centralway.player.data.source.phone.PlaylistPhoneDataSource;
import com.centralway.player.utils.BaseSchedulerProvider;
import com.centralway.player.utils.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {
    public static SettingRepository provideSettingRepository(@NonNull Context context) {
        checkNotNull(context);
        return SettingRepository.getInstance(
                SettingLocalDataSource.getInstance(context));
    }
    public static PlaylistRepository providePlaylistRepository(@NonNull Context context) {
        checkNotNull(context);
        return PlaylistRepository.getInstance(
                PlaylistLocalDataSource.getInstance(context),
                PlaylistPhoneDataSource.getInstance(context));
    }
    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
