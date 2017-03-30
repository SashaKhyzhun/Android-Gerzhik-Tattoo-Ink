package com.sashakhyzhun.locationhelper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @autor SashaKhyzhun
 * Created on 3/28/17.
 */

public class LocationUtil extends Activity {

    private Activity activity;

    public LocationUtil(Activity activity) {
        this.activity = activity;
    }


    /**
     * Method to get permission if it's not granted. For Android 23 and above.
     */
    public void invokeLocationPermission() {
        if (!getLocationPermissionGrantedValue()) {
        ActivityCompat.requestPermissions(activity, new String[]{
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
        }, 1);
        return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("onRequestPermissionsResult method");
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    //permission denied, boo! Disable the functionality that depends on this permission.
                    invokeLocationPermission();
                }
            }
        }
    }

    /**
     * Method which returns boolean value of granted permission. To work with location we need to
     * have this permission and we had to check it in runtime
     * @return value of granted permission
     */
    public boolean getLocationPermissionGrantedValue() {
        int isGranted = activity.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION);
        return (isGranted == PackageManager.PERMISSION_GRANTED);
    }




}

