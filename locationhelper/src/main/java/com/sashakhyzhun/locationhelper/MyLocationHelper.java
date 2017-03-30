package com.sashakhyzhun.locationhelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @autor SashaKhyzhun
 * Created on 3/28/17.
 */

public class MyLocationHelper extends Activity {

    private Activity activity;

    public MyLocationHelper(Activity activity) {
        this.activity = activity;
    }

//    public MyLocationHelper(Context context) {
//        this.context = context;
//    }
//    public MyLocationHelper(Activity activity, Context context) {
//        this.activity = activity;
//        this.context = context;
//    }

//    /**
//     * Method to enable tracking
//     */
//    public void enableTrackLocation() {
//        invokeLocationPermission();
//    }


//    public void getLocationPermissionStatus() {
//        System.out.println("asd method");
//        PermissionUtil.checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionAskListener() {
//            @Override
//            public void onPermissionAsk() {
//                System.out.println("onPermissionAsk OMG");
//                invokeLocationPermission();
//            }
//
//            @Override
//            public void onPermissionPreviouslyDenied() {
//                //show a dialog explaining permission and then request permission
//                System.out.println("onPermissionPreviouslyDenied OMG");
//            }
//
//            @Override
//            public void onPermissionDisabled() {
//                System.out.println("onPermissionDisabled OMG");
//                //askLocationPermission();
//
//            }
//
//            @Override
//            public void onPermissionGranted() {
//                System.out.println("onPermissionGranted OMG");
//            }
//        });
//    }



    /**
     * Method to get permission if it's not granted. For Android 23 and above.
     */
    public void invokeLocationPermission() {
//        if (!checkLocationPermission()) {
        ActivityCompat.requestPermissions(activity, new String[]{
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
        }, 1);
        return;
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        System.out.println("onRequestPermissionsResult method!!!!!");
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the contacts-related task you need to do.
//                } else {
//                    //permission denied, boo! Disable the functionality that depends on this permission.
//                    invokeLocationPermission();
//                }
//            }
//        }
//    }

    /**
     * Method which returns boolean value of granted permission. To work with location we need to
     * have this permission and we had to check it in runtime
     * @return value of granted permission
     */
    public boolean getLocationPermissionGranted() {
        int isGranted = activity.checkCallingOrSelfPermission(ACCESS_FINE_LOCATION);
        return (isGranted == PackageManager.PERMISSION_GRANTED);
    }




}

