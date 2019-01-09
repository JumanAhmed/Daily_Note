package com.capsulestudio.dailynote.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Juman on 12/16/2017.
 */

public class CheckPermission {
    Context context;
    public static final int RequestPermissionCode = 1;
    public CheckPermission(Context context) {
        this.context = context;

    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission()
    {
        ActivityCompat.requestPermissions((Activity) context, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE

                }, RequestPermissionCode);
    }

}
