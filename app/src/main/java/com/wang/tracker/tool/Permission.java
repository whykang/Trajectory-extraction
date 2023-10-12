package com.wang.tracker.tool;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wang.tracker.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    //需要申请权限的数组
    private final String[] permissions = {
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //保存真正需要去申请的权限
    private final List<String> permissionList = new ArrayList<>();

    public static int RequestCode = 100;

    public void checkPermissions(Activity activity) {
        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(permission);
//            }
            permissionList.add(permission);
        }

        requestPermission(activity);

        //有需要去动态申请的权限
//        if (permissionList.size() > 0) {
//            requestPermission(activity);
//            Toast.makeText(activity, "请授权", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(activity, "已授权", Toast.LENGTH_SHORT).show();
//        }
    }
    //去申请的权限
    private void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,permissionList.toArray(new String[permissionList.size()]),RequestCode);
    }

}