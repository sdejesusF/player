package com.centralway.player.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class Permission {

    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int CODE_PERMISSIONS_ACCEPTED = 0;
    public static final int CODE_ANY_PERMISSION_DENIED = 1;
    public static final int CODE_ANY_PERMISSION_DENIED_FOREVER = 2;
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String TAG = Permission.class.getSimpleName();

    public static int canWeRuntimePermissions(Activity activity, boolean isFirsTime) {
        int result = CODE_PERMISSIONS_ACCEPTED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                            || isFirsTime) {
                        result = (result == CODE_PERMISSIONS_ACCEPTED) ? CODE_ANY_PERMISSION_DENIED : result;
                    } else {
                        Log.i(TAG, "Permission forever denied: " + permission);
                        result = CODE_ANY_PERMISSION_DENIED_FOREVER;
                    }
                }
            }
        }
        return result;
    }
}
