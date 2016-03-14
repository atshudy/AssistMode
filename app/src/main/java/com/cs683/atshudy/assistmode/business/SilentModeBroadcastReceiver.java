package com.cs683.atshudy.assistmode.business;

/**
 * Created by ATshudy on 4/19/2015.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class SilentModeBroadcastReceiver extends BroadcastReceiver{

    final static public String TAG = "com.cs683.atshudy.assistmode.business.SilentModeBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        int iMode = intent.getExtras().getInt("ALARM_MODE");
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(iMode);
        Log.i(TAG, "onReceive: Alarm has fired om "+sdf.format(System.currentTimeMillis())+" mode is "+iMode);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i(TAG, "Silent mode");
                Toast.makeText(context, "onReceive: setting device to Silent mode", Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i(TAG,"Vibrate mode");
                Toast.makeText(context, "onReceive: setting device to Vibrate mode", Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.i(TAG,"Normal mode");
                Toast.makeText(context, "onReceive: setting device back to Normal mode", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     *
     * @param context
     * @param iDay
     * @param sAlarmTime
     */
    public void SetTimeAlarm(Context context, int iDay, String sAlarmTime, int iAlarmMode) {
        Random rn = new Random();
        Log.i(TAG, "setting an alarm for "+sAlarmTime+" mode is "+iAlarmMode);

        Intent serviceIntent = new Intent("com.cs683.atshudy.assistmode.businessSilentModeBroadcastReceiver.add");
        serviceIntent.putExtra("ALARM_MODE", iAlarmMode);

        PendingIntent pendingService = PendingIntent.getBroadcast(context, rn.nextInt(), serviceIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        String[] atime = sAlarmTime.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK,iDay);
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(atime[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(atime[1]));
        calendar.set(Calendar.SECOND,Integer.parseInt(atime[2]));

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingService);
    }

}