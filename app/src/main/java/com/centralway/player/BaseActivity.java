package com.centralway.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.centralway.player.data.source.SettingRepository;
import com.centralway.player.permission.PermissionActivity;

import static com.centralway.player.utils.Permission.CODE_PERMISSIONS_ACCEPTED;
import static com.centralway.player.utils.Permission.canWeRuntimePermissions;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class BaseActivity extends AppCompatActivity {

    private SettingRepository mSettingRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingRepository = Injection.provideSettingRepository(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((canWeRuntimePermissions(this, mSettingRepository.isFirstTimePermission()) != CODE_PERMISSIONS_ACCEPTED
                && !PermissionActivity.class.getName().equalsIgnoreCase(this.getClass().getName()))) {
            Intent intent = new Intent(this, PermissionActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public boolean isFirstTimePermission() {
        return mSettingRepository.isFirstTimePermission();
    }

    public void noFirstTime() {
        mSettingRepository.setNoFirstTimePermission();
    }
}
