package com.centralway.player.utils;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by sergiodejesus on 6/25/17.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
