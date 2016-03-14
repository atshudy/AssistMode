package com.cs683.atshudy.assistmode.business;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.views.MainActivity;

/**
 *
 * This library is the back port of the Job Scheduler from API 21
 * which enables the Job scheduler to work at a lower API level.  Works with API level 10 and above.
 *
 * NOTE: Once you add the Gradle dependency.
 * Goto Tools-->Android-->Sync Project with Gradle Files.
 * This will automatically download the library from github and link it with the project.
 * When extending JobService select the package from me.tatarka.support.job.JobService
 *
 * for documentation refer to the following link
 * https://github.com/evant/JobSchedulerCompat
 *
 *
 * The service needs to read the silent_mode table and get the list of enabled tasks
 *      1] Fixed schedule: If the task is a scheduled task set the job to run at the start time and end at the end time.
 *      2] Calendar Schedule: Every 24 hrs read the calendar and set jobs based on meeting that are scheduled.
 *
 *      3] set the device in one of the three modes
 *              Silent:  Set the device in silent mode
 *              Vibrate:  Set the device in vibrate mode
 *              Volume:  Decrease the volume level to 25%
 */
public class SilentModeService extends BaseService {

    private static final String TAG = "com.cs683.atshudy.assistmode.business.SilentModeService";


    public SilentModeService() {
        super();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Toast.makeText(this, "SilentModeService Job Starts", Toast.LENGTH_SHORT).show();
        startNotification();
        new JobTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        killNotification();
        Toast.makeText(this, "SilentModeService Job Stopped: criteria not met", Toast.LENGTH_SHORT).show();

        return false;
    }

    private static class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;

        public JobTask(JobService jobService) {
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            SystemClock.sleep(5000);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobService.jobFinished(jobParameters, false);
            Toast.makeText(jobService, "SilentModeService Task Finished", Toast.LENGTH_SHORT).show();
        }
    }
}