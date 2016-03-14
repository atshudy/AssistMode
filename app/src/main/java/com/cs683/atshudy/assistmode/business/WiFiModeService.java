package com.cs683.atshudy.assistmode.business;

import android.app.job.JobService;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cs683.atshudy.assistmode.model.SilentModeDAO;
import com.cs683.atshudy.assistmode.model.WiFiModeDAO;
import com.cs683.atshudy.assistmode.model.WiFiModeDAOImpl;

import java.util.ArrayList;
import java.util.Map;


public class WiFiModeService extends JobService {
    private static final String TAG = "com.cs683.atshudy.assistmode.business.WiFiModeService";

    public WiFiModeService() {
        super();
    }

    @Override
    public boolean onStartJob(android.app.job.JobParameters jobParameters) {
        Toast.makeText(this, "Job Starts", Toast.LENGTH_SHORT).show();
        new JobTask(this).execute(jobParameters);
        return true;
    }


    @Override
    public boolean onStopJob(android.app.job.JobParameters params) {
        Toast.makeText(this, "Job Stopped: criteria not met", Toast.LENGTH_SHORT).show();
        return false;
    }

    private static class JobTask extends AsyncTask<android.app.job.JobParameters, Void, android.app.job.JobParameters> {
        private final android.app.job.JobService jobService;
        private volatile ArrayList<Map<String, Object>> mjobList;

        public JobTask(android.app.job.JobService jobService) {
            this.jobService = jobService;
        }


        @Override
        protected void onPreExecute() {
            //Toast.makeText(jobService, "open the database and run the query for a list of tasks to run", Toast.LENGTH_SHORT).show();
            WiFiModeDAO wiFiModeDAO = new WiFiModeDAOImpl(this.jobService.getApplicationContext());
            mjobList = new ArrayList<Map<String, Object>>();
            mjobList = wiFiModeDAO.getWiFimodeTaskJobs();
            wiFiModeDAO.close();
            if (mjobList.isEmpty())
                Toast.makeText(jobService, "There is nothing to do", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected android.app.job.JobParameters doInBackground(android.app.job.JobParameters... params) {

            // set the tasks as Alarms
            for (Map<String, Object> job : mjobList) {
                if (job.get("ENABLE_TASK") == 1) {
                    if (job.get("ENABLE_FIXED_SCH") == 1) {
                        // createFixedSchedule(job);
                    } else {
                        // createCalendarSchedule(job);
                    }
                }
            }
            return params[0];

        }


        @Override
        protected void onPostExecute(android.app.job.JobParameters jobParameters) {
            // set all the tasks as pending state
            WiFiModeDAO wiFiModeDAO = new WiFiModeDAOImpl(this.jobService.getApplicationContext());
            wiFiModeDAO.setCompleteState();
            // close database connection
            wiFiModeDAO.close();
            jobService.jobFinished(jobParameters, false);
            Toast.makeText(jobService, "Task Finished", Toast.LENGTH_SHORT).show();
        }

    }

}
