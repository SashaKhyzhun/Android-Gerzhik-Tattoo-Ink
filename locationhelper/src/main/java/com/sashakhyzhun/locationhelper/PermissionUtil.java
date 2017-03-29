package com.sashakhyzhun.locationhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * @autor SashaKhyzhun
 * Created on 3/29/17.
 */

public class PermissionUtil {
    /*
    * Check if version is marshmallow and above.
    * Used in deciding to ask runtime permission
    * */
    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    @SuppressLint("NewApi")
    public static void checkPermission(Activity activity, String permission, PermissionAskListener listener) {
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


}