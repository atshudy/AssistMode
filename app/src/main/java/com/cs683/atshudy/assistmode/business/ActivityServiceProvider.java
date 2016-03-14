package com.cs683.atshudy.assistmode.business;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;

/**
 * Created by ATshudy on 4/21/2015.
 *
 * copy code from the following location. I was not able to test it using the emulator.
 * NOTE: This will require a real device to continue.
 *
 * http://stackoverflow.com/questions/27196078/how-to-use-com-google-android-gms-common-api-googleapiclient-not-the-activityre
 *
 */

public class ActivityServiceProvider {
    private Context context;
    private GoogleApiClient googleApiClient;
    private PendingIntent mActivityRecognitionPendingIntent;
    private static final String TAG = "ActivityServiceProvider";

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;


    public ActivityServiceProvider(Context context) {
        this.context = context;
        createGoogleLocationServiceClient();
    }

    private void createGoogleLocationServiceClient() {
        googleApiClient = new GoogleApiClient.Builder(context).addApi(ActivityRecognition.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

                    @Override
                    public void onConnectionSuspended(int arg0) {
                        Log.d(TAG, "GoogleApiClient Suspended");
                    }

                    @Override
                    public void onConnected(Bundle arg0) {
                        Log.d(TAG, "GoogleApiClient Connected Now");
                        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, DETECTION_INTERVAL_MILLISECONDS, getPendingIntent())
                                .setResultCallback(
                                new ResultCallback<Status>() {

                                    @Override
                                    public void onResult(Status arg0) {
                                        if (arg0.isSuccess()) {
                                            Log.d(TAG, "Updates Requested Successfully");
                                        } else {
                                            Log.d(TAG, "Updates could not be requested");
                                        }
                                    }
                                });



                    }
                }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult arg0) {
                        Log.d(TAG, "GoogleApiClient Connection Failed");
                    }
                }).build();
    }

    public PendingIntent getRequestPendingIntent() {
        return mActivityRecognitionPendingIntent;
    }

    public void setRequestPendingIntent(PendingIntent intent) {
        mActivityRecognitionPendingIntent = intent;
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, DrivingModeService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, (int)Math.random(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        setRequestPendingIntent(pendingIntent);
        return pendingIntent;
    }

    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(TAG, "Google Play services is available");
            return true;
        } else {
            Log.d(TAG, "Google Play services is unavailable");
            return false;
        }
    }

    public void connect() {
        if (servicesConnected() && !googleApiClient.isConnected()) {
            Log.d(TAG, "GoogleApiClient Connection Initiated: connect() Called");
            googleApiClient.connect();
        } else {
            Log.d(TAG, "GoogleApiClient already connected or is unavailable");
        }
    }

    public void disconnect() {
        if (servicesConnected() && googleApiClient.isConnected()) {
            Log.d(TAG, "GoogleApiClient disconnection kicked");
            if (mActivityRecognitionPendingIntent != null && googleApiClient != null) {
                ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(googleApiClient,
                        mActivityRecognitionPendingIntent);
            }
            googleApiClient.disconnect();
        } else {
            Log.d(TAG, "GoogleApiClient already disconnected or is unavailable");
        }
    }
}
