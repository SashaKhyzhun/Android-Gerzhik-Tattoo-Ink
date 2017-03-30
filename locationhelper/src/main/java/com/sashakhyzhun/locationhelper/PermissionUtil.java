package com.sashakhyzhun.locationhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @autor SashaKhyzhun
 * Created on 3/29/17.
 */

public class PermissionUtil extends ContextCompat {
    /*
    * Check if version is marshmallow and above.
    * Used in deciding to ask runtime permission
    * */
    private static boolean shouldAskPermission() {
        System.out.println("#PermissionUtil | shouldAskPermission()");
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        System.out.println("#PermissionUtil | shouldAskPermission()");
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    public static void invokeLocationPermission(Activity activity) {
        System.out.println("#PermissionUtil | invokeLocationPermission()");
        if (!getLocationPermissionIsGranted(activity)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
            }, 1);
            return;
        }
    }

    /**
     * Method which returns boolean value of granted permission. To work with location we need to
     * have this permission and we had to check it in runtime
     * @return value of granted permission
     */
    public static boolean getLocationPermissionIsGranted(Activity activity) {
        System.out.println("#PermissionUtil | getLocationPermissionIsGranted()");
        int isGranted = activity.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION);
        return (isGranted == PackageManager.PERMISSION_GRANTED);
    }


    @SuppressLint("NewApi")
    public static void checkPermission(Activity activity, String permission, PermissionAskListener listener) {
        System.out.println("#PermissionUtil | checkPermission()");
       /*
        * If permission is not granted
        * */
        if (shouldAskPermission(activity, permission)) {
           /*
            * If permission denied previously
            * */
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                listener.onPermissionPreviouslyDenied();
            } else {
               /*
                * Permission denied or first time requested
                * */
                if (PreferenceUtil.isFirstTimeAskingPermission(activity, permission)) {
                    PreferenceUtil.firstTimeAskingPermission(activity, permission, false);
                    listener.onPermissionAsk();
                } else {
                    /*
                    * Handle the feature without permission or ask user to manually allow permission
                    * */
                    listener.onPermissionDisabled();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED })
    public @interface PermissionStatus {}

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;

    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }


}