package com.centralway.player.permission;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;

import com.centralway.player.BaseActivity;
import com.centralway.player.R;
import com.centralway.player.home.HomeActivity;

import static com.centralway.player.utils.Permission.CODE_ANY_PERMISSION_DENIED;
import static com.centralway.player.utils.Permission.CODE_PERMISSIONS_ACCEPTED;
import static com.centralway.player.utils.Permission.PERMISSIONS;
import static com.centralway.player.utils.Permission.PERMISSIONS_MULTIPLE_REQUEST;
import static com.centralway.player.utils.Permission.canWeRuntimePermissions;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class PermissionActivity extends BaseActivity implements PermissionContract.View, View.OnClickListener {

    PermissionContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        findViewById(R.id.screen_permission_permission_read).setOnClickListener(this);

        new PermissionPresenter(this);

    }

    @Override
    public void setPresenter(PermissionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int canWeNormalPermissions = canWeRuntimePermissions(this, isFirstTimePermission());
        if (canWeNormalPermissions == CODE_PERMISSIONS_ACCEPTED) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showPermission() {
        int canWeNormalPermissions = canWeRuntimePermissions(this, isFirstTimePermission());
        if (canWeNormalPermissions != CODE_PERMISSIONS_ACCEPTED) {
            if (canWeNormalPermissions == CODE_ANY_PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    noFirstTime();
                    requestPermissions(PERMISSIONS, PERMISSIONS_MULTIPLE_REQUEST);
                }
            } else {
                final Uri uri = Uri.fromParts("package", getPackageName(), null);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        mPresenter.enablePermissionRequired();
    }

}
