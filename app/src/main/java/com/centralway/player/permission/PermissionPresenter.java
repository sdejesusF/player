package com.centralway.player.permission;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PermissionPresenter implements PermissionContract.Presenter {

    private PermissionContract.View mView;

    public PermissionPresenter(PermissionContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void enablePermissionRequired() {
        mView.showPermission();
    }

}
