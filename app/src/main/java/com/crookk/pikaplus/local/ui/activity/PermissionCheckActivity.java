package com.crookk.pikaplus.local.ui.activity;

import android.support.annotation.NonNull;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.ui.activity.BaseActivity;
import com.crookk.pikaplus.local.bean.FileManager;
import com.crookk.pikaplus.module.map.ui.activity.MapActivity_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;

@EActivity(R.layout.activity_permission_check)
public class PermissionCheckActivity extends BaseActivity {

    @Bean
    FileManager mFileManager;

    @Override
    protected void afterViews() {
        super.afterViews();
        checkPermission();
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    private void checkPermission() {

        PermissionManager.with(this)
                .permission(
                        PermissionEnum.ACCESS_COARSE_LOCATION, PermissionEnum.ACCESS_FINE_LOCATION,
                        PermissionEnum.WRITE_EXTERNAL_STORAGE, PermissionEnum.READ_EXTERNAL_STORAGE)
                .askagain(true)
                .askagainCallback(new AskagainCallback() {
                    @Override
                    public void showRequestPermission(UserResponse response) {
                        finish();
                    }
                })
                .callback(new FullCallback() {
                    @Override
                    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied,
                                       ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {

                        if(permissionsGranted.contains(PermissionEnum.WRITE_EXTERNAL_STORAGE)) {
                            mFileManager.init();
                        }

                        MapActivity_.intent(PermissionCheckActivity.this).start();
                        finish();
                    }
                })
                .ask();
    }
}
