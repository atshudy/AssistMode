package com.cs683.atshudy.assistmode.business;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class DrivingModeService extends IntentService {

    private static final String TAG = "DrivingModeService";
    public DrivingModeService() {
        super("DrivingModeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();
            int confidence = mostProbableActivity.getConfidence();
            int activityType = mostProbableActivity.getType();
            String activityName = getNameFromType(activityType);

            Log.d(TAG, "Activity Detected:" + activityName);

            Intent activityUpdateIntent = new Intent("ACTIVITY_UPDATE_INTENT");
            activityUpdateIntent.putExtra("ACTIVITY_NAME", activityName);
            activityUpdateIntent.putExtra("ACTIVITY_TYPE", activityType);
            activityUpdateIntent.putExtra("ACTIVITY_CONFEDENCE", confidence);

            getApplicationContext().sendBroadcast(activityUpdateIntent);
        }
    }

    private String getNameFromType(int activityType){
        String type;
        if (activityType == DetectedActivity.IN_VEHICLE) {
            type = "In Car";
        } else if (activityType == DetectedActivity.ON_FOOT) {
            type = "On Foot";
        } else if (activityType == DetectedActivity.ON_BICYCLE) {
            type = "By Bicycle";
        } else if (activityType == DetectedActivity.STILL) {
                type = "Your standing still";
        } else {
            type = "Unknown";
        }
        return type;
    }


    private boolean isDriving(int type) {
        switch (type) {
            case DetectedActivity.IN_VEHICLE:
                return true;
            default:
                return false;
        }
    }
}


