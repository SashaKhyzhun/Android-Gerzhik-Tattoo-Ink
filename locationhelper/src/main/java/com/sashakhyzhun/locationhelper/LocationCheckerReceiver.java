//package com.sashakhyzhun.locationhelper;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Handler;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
///**
// * @autor SashaKhyzhun
// * Created on 3/30/17.
// */
//
//public class LocationCheckerReceiver extends BroadcastReceiver {
//
//    public static final String TAG = "LocationCheckerReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //try {
//        //    FacebookSdk.sdkInitialize(context);
//        //} catch (RuntimeException e) {
//        //    Log.e(TAG, "onReceive: I Hate Facebook: " + e);
//        //    e.printStackTrace();
//        //}
//
//        final int hour = intent.getIntExtra("hour", 0);
//        final int min = intent.getIntExtra("min", 0);
//        final int requestCode = intent.getIntExtra("requestCode", 0);
//        String inProgressID = intent.getStringExtra("inProgressID");
//
//        System.out.println(TAG + " | extras: hour: " + hour + ", min: " + min + " (requestCode: " + requestCode + ")");
//
//        Intent alarmIntent = new Intent();
//        alarmIntent.putExtra("finalRequestCode", requestCode);
//        alarmIntent.putExtra("finalInProgressID", inProgressID);
//
//
//        GPSTracker gpsTracker = new GPSTracker(context);
//        if (gpsTracker.canGetLocation()) {
//            System.out.println("Latitude: " + gpsTracker.getLatitude() + " Longitude: " + gpsTracker.getLongitude());
//        } else {
//            System.out.println("Location is disable...");
//        }
//
//        final LocationChecker lc = new LocationChecker(context);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                lc.enableDailyLocationCheck(hour, min, requestCode);
//            }
//        }, 1000);
//
//        context.startActivity(alarmIntent);
//
//    }
//
//
//
//}
