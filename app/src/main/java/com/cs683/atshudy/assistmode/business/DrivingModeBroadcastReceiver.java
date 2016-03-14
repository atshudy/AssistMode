package com.cs683.atshudy.assistmode.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ATshudy on 4/22/2015.
 */
public class DrivingModeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int iActiveType = intent.getExtras().getInt("ACTIVITY_TYPE");
        String sActiveType = intent.getExtras().getString("ACTIVITY_TYPE");
        // if you are are driving read database record to set the device to send a text message or
        // change the device to hands free.
        Toast.makeText(context, "Your activily "+sActiveType,Toast.LENGTH_SHORT).show();
    }
}
