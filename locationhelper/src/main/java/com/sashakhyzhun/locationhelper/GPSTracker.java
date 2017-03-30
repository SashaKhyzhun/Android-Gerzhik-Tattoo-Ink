package com.sashakhyzhun.locationhelper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ServiceCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * @autor SashaKhyzhun
 * Created on 3/30/17.
 */

public final class GPSTracker implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    //private Activity activity;
    private Context context;
    private boolean isGPSEnabled = false;         // flag for GPS status
    private boolean isNetworkEnabled = false;     // flag for network status
    private boolean canGetLocation = false;       // flag for GPS status
    private Location location;                    // location
    private double latitude;                      // latitude
    private double longitude;                     // longitude
    private double speed;                         // speed

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(/*Activity activity, */Context context) {
        /*this.activity = activity;*/
        this.context = context;
        getLocation();
    }

    /**
     * Function to get the user's current location
     * @return
     */
    private Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v("isGPSEnabled", "=" + isGPSEnabled);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(null, new String[] {
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                if (isNetworkEnabled) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to get Speed
     */
    public double getSpeed() {
        if (location != null) {
            speed = location.getSpeed();
        }
        return speed;
    }
    public boolean hasSpeed() {
        boolean hasSpeed = false;
        if (location != null) {
            hasSpeed = location.hasSpeed();
        }
        return hasSpeed;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     */
    public boolean canGetLocation() {
        // return bool value
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * launch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    /**
     * Method to set daily location checker. Create AlarmManager for daily ring, convert
     * time and sending extras for CustomAlarmReceiver. Next we generate cards for MainActivity.
     * @param requestCode - values from time picker: minutes and hour. To start we get this,
     *                      convert to normal view (means from 1:8 to 01:08) and put inside
     *                      alarmManager like ID.
     * @param hour - this is value from last method-provider, this value is equals to time when
     *               we need fire our alarm manager.
     * @param min  - this is value from last method-provider, this value is equals to time when
     *               we need fire our alarm manager.
     */
    public void setDailyLocationChecker(int hour, int min, int requestCode) {
        LocationChecker locationChecker = new LocationChecker(context);
        locationChecker.enableDailyLocationCheck(hour, min, requestCode);
    }

    /**
     * Method to disable daily location checker. Create AlarmManager for daily ring, convert
     * time and sending extras for CustomAlarmReceiver. Next we generate cards for MainActivity.
     * @param requestCode - values from time picker: minutes and hour. To start we get this,
     *                      convert to normal view (means from 1:8 to 01:08) and put inside
     *                      alarmManager like ID.
     */
    public void disableDailyLocationChecker(int requestCode) {
        LocationChecker locationChecker = new LocationChecker(context);
        locationChecker.disableDailyLocationCheck(requestCode);
    }





    @Override
    public void onLocationChanged(Location location) {
        speed = location.getSpeed();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}