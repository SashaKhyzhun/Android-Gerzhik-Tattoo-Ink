package com.sashakhyzhun.gerzhiktattooink.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.activity.MainActivity;
import com.sashakhyzhun.locationhelper.LocationDailyChecker;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;
import com.sashakhyzhun.locationhelper.GPSTracker;

import java.util.Date;

/**
 * @autor SashaKhyzhun
 * Created on 3/30/17.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            FacebookSdk.sdkInitialize(context);
        } catch (RuntimeException e) {
            Log.e("CustomNotifyReceiver", "onReceive: I Hate Facebook: " + e);
            e.printStackTrace();
        }


        SessionManager sessionManager = SessionManager.getInstance(context);
        if (sessionManager.isUserLoggedIn()) {

            GPSTracker gpsTracker = new GPSTracker(context);
            boolean isEnabled = gpsTracker.isGPSEnabled();

            sendNotification(context, "Location Permission: " + isEnabled,
                                      "Lat: " + gpsTracker.getLatitude()
                                  + "\nLon: " + gpsTracker.getLongitude());

            LocationDailyChecker dlc = new LocationDailyChecker(context);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int hour = intent.getIntExtra("hour", 0);
                    int min = intent.getIntExtra("min", 0);
                    int requestCode = intent.getIntExtra("requestCode", 0);
                    dlc.enableDailyNotificationReminder(hour, min, requestCode, MyReceiver.class);
                }
            }, 1000);


        }

    }


    private void sendNotification(Context context, String title, String body) {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("Notification: Received at: " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 9999, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setLights(Color.GREEN, 1000, 1500)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true).setWhen(System.currentTimeMillis());

        notificationManager.notify(9999, builder.build());
    }


}