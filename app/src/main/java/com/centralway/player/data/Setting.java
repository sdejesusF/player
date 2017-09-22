package com.centralway.player.data;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class Setting {

    private final boolean mFirstTimePermissions;

    public Setting(boolean isFirstTimePermissions) {
        mFirstTimePermissions = isFirstTimePermissions;
    }

    public boolean isFirstTimePermissions() {
        return mFirstTimePermissions;
    }
}
