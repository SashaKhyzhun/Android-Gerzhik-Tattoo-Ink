package com.sashakhyzhun.locationhelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.content.Context.MODE_PRIVATE;

/**
 * @autor SashaKhyzhun
 * Created on 3/30/17.
 */

public class PreferenceUtil {

    public static final String PREF_FILE_NAME = "pref_file_name";

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }


    public static boolean isFirstTimeAskingPermission(Context context, String permission) {
        return context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true);
    }


}
