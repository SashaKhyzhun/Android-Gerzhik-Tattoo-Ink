package com.sashakhyzhun.gerzhiktattooink.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.sashakhyzhun.gerzhiktattooink.receiver.MyReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * @autor SashaKhyzhun
 * Created on 3/30/17.
 */

public class DailyLocationChecker {

    private Context context;

    public DailyLocationChecker(Context context) { this.context = context; }


    public void enableDailyNotificationReminder(int hour, int min, int requestCode) {
        Calendar myCalender = Calendar.getInstance();
        myCalender.set(Calendar.HOUR_OF_DAY, hour);
        myCalender.set(Calendar.MINUTE, min);
        myCalender.set(Calendar.SECOND, 0);

        if (System.currentTimeMillis() > myCalender.getTimeInMillis()) {
            myCalender.add(Calendar.DAY_OF_YEAR, 1);
        }


        Date date = new Date(myCalender.getTimeInMillis());
        Date now = new Date(System.currentTimeMillis());
        System.out.println("Alarm | Current time on device : " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds());
        System.out.println("Alarm | Enable Daily Wake Up on: " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());

        Intent notifyIntent = new Intent(context, MyReceiver.class);
        notifyIntent.putExtra("hour", hour);
        notifyIntent.putExtra("min", min);
        notifyIntent.putExtra("requestCode", requestCode);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pi);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        }

    }

    public void disableDailyNotificationReminder(int requestCode) {
        System.out.println("Notification | Disable Daily Reminder");
        Intent alarmIntent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


}
