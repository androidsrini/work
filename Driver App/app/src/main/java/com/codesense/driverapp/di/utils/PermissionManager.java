package com.codesense.driverapp.di.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.ui.helper.CrashlyticsHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PermissionManager {

    public static int PERMISSIONS_REQUEST = 0x00300;
    private int grandPermissionCount = 0;
    @Inject
    AppSharedPreference appSharedPreference;

    @Inject
    public PermissionManager(){

    }

    public  boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private  boolean shouldAskPermission(Context context, String permission){
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private PermissionStatus checkPermission(Context context, String permission, PermissionAskListener listener){
        if (!shouldAskPermission(context, permission)){
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity)context,permission)) {
                listener.onPermissionPreviouslyDenied(permission);
                return PermissionStatus.PREVIOUSLY_DENIED;
            } else {
                listener.onNeedPermission();
                return PermissionStatus.NEED_PERMISSION;
                /*if (appSharedPreference.isFirstTime(permission)) {
                    appSharedPreference.savePermission(permission, false);
                  listener.onNeedPermission();
                    return PermissionStatus.NEED_PERMISSION;
                } else {
                    listener.onPermissionPreviouslyDeniedWithNeverAskAgain(permission);
                    return PermissionStatus.DENIED_PERMANENTLY;
                }*/
            }
        } else {
            grandPermissionCount++;
            listener.onPermissionGranted();
            return PermissionStatus.GRANTED;
        }
    }

    /**
     * This method to check app enabled permission or not.
     * If permission not enabled it will triggered permission dialog and return false.
     * All permission enabled it will return true.
     * @param activity
     * @param permission
     * @return
     */
    public boolean initPermissionDialog(Activity activity, String[] permission) {
        List<String> requestPermission = new ArrayList<>();
        for (String s: permission) {
            if (shouldAskPermission(activity, s)) {
                requestPermission.add(s);
            }
        }
        CrashlyticsHelper.d("Init permission request permission list size: ", requestPermission.size());
        if (!requestPermission.isEmpty()) {
            ActivityCompat.requestPermissions(activity, requestPermission.toArray(new String[0]),
                    PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

    /**
     * This method should call on onRequestPermissionsResult.
     * This method to check app enabled permission or not.
     * If permission not enabled it will triggered permission dialog.
     * @param activity
     * @param permission
     * @param permissionAskListener
     */
    public void showRequestPermissionDialog(Activity activity, String[] permission, PermissionAskListener permissionAskListener) {
        grandPermissionCount = 0;
        List<String> requestPermission = new ArrayList<>();
        for (String s: permission) {
            if (PermissionStatus.NEED_PERMISSION == checkPermission(activity, s, permissionAskListener)) {
                requestPermission.add(s);
            }
        }
        CrashlyticsHelper.d("Show permission request permission list size: ", requestPermission.size());
        if (!requestPermission.isEmpty()) {
            ActivityCompat.requestPermissions(activity, requestPermission.toArray(new String[0]),
                    PERMISSIONS_REQUEST);
        }
    }

    public void showRequestPermissionDialog(Activity activity, String permission, PermissionAskListener permissionAskListener) {
        String[] permissionString = new String[1];
        permissionString[0] = permission;
        showRequestPermissionDialog(activity, permissionString, permissionAskListener);
    }

    public void showPermissionNeededDialog(Context context, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        builder.setMessage(msg);
        builder.setPositiveButton(android.R.string.ok, onClickListener);
        alertDialog.show();
    }

    public String[] getStorageReadAndWrightPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    public int getGrandPermissionCount() {
        return grandPermissionCount;
    }

    public interface PermissionAskListener {
        void onNeedPermission();
        void onPermissionPreviouslyDenied(String permission);
        void onPermissionGranted();
    }

    public enum PermissionStatus {
        /**
         * Show permission dialog
         */
        NEED_PERMISSION,
        /**
         * Permission Success
         */
        GRANTED,
        /**
         * Need to show permission need dialog
         */
        PREVIOUSLY_DENIED,
    }
}
