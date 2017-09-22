package com.centralway.player.about;

import com.centralway.player.BaseNavigationActivity;
import com.centralway.player.R;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class AboutActivity extends BaseNavigationActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.action_about;
    }

    @Override
    public int getTitleId() {
        return R.string.bottom_navigation_menu_about;
    }
}
