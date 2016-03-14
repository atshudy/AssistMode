package com.cs683.atshudy.assistmode.business;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.views.MainActivity;

import me.tatarka.support.job.JobService;

public abstract class BaseService extends JobService {
    private static final String TAG = "com.cs683.atshudy.assistmode.business.BaseService";

    Notification mNotification = null;

    public BaseService() {
        super();
    }

    public void startNotification() {

        if (mNotification == null) {
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            // this is it, we'll build the notification!
            // in the addAction method, if you don't want any icon, just set the first param to 0
            Notification mNotification = new Notification.Builder(this)
                    .setContentTitle("AssistMode App")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(101, mNotification);
        }

    }

    public void killNotification(){
        if (this.NOTIFICATION_SERVICE!=null) {
            String ns = this.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancelAll();
            getApplicationContext().canclegetAllJobs();
        }
    }

}
