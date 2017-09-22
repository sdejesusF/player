package com.centralway.player.permission;

import com.centralway.player.BasePresenter;
import com.centralway.player.BaseView;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PermissionContract {

    interface View extends BaseView<Presenter> {

        void showPermission();

    }

    interface Presenter extends BasePresenter {

        void enablePermissionRequired();

    }
}
