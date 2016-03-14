package com.cs683.atshudy.assistmode.business;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

public class DrivingModeService extends BaseService {
    private static final String TAG = "com.cs683.atshudy.assistmode.business.DrivingModeService";
    public DrivingModeService() {
        super();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Toast.makeText(this, "DrivingModeService Job Starts", Toast.LENGTH_SHORT).show();
        startNotification();
        new JobTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        killNotification();
        Toast.makeText(this, "DrivingModeService Job Stopped: criteria not met", Toast.LENGTH_SHORT).show();

        return false;
    }

    private static class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;

        public JobTask(JobService jobService) {
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            SystemClock.sleep(10000);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobService.jobFinished(jobParameters, false);
            Toast.makeText(jobService, "DrivingModeService Task Finished", Toast.LENGTH_SHORT).show();
        }
    }
}
