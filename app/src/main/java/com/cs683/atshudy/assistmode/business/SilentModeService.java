package com.cs683.atshudy.assistmode.business;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.cs683.atshudy.assistmode.model.SilentModeDAO;
import com.cs683.atshudy.assistmode.model.SilentModeDAOImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The service needs to read the silent_mode table and get the list of enabled tasks
 *      1] Fixed schedule: If the task is a scheduled task set the job to run at the start time and end at the end time.
 *      2] Calendar Schedule: Every 24 hrs read the calendar and set jobs based on meeting that are scheduled.
 *
 *      3] set the device in one of the three modes
 *              Silent:  Set the device in silent mode
 *              Vibrate:  Set the device in vibrate mode
 *              Volume:  Decrease the volume level to 25%
 */


public class SilentModeService extends JobService {
    private static final String TAG = "SilentModeService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Toast.makeText(this, "Job Starts", Toast.LENGTH_SHORT).show();
        new JobTask(this).execute(jobParameters);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(this, "Job Stopped: criteria not met", Toast.LENGTH_SHORT).show();
        return false;
    }

    private static class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;
        private volatile ArrayList<Map<String, Object>> mjobList;

        public JobTask(JobService jobService) {
            this.jobService = jobService;
        }


        @Override
        protected void onPreExecute() {
            //Toast.makeText(jobService, "open the database and run the query for a list of tasks to run", Toast.LENGTH_SHORT).show();
            SilentModeDAO silentmodeDAO = new SilentModeDAOImpl(this.jobService.getApplicationContext());
            mjobList = new ArrayList<Map<String, Object>>();
            mjobList = silentmodeDAO.getSilentmodeTaskJobs();
            silentmodeDAO.close();
            if (mjobList.isEmpty())
                Toast.makeText(jobService, "There is nothing to do", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(jobService, "Found something to do", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {

            // set the tasks as Alarms
            for (Map<String, Object> job : mjobList) {
                if (job.get("ENABLE_TASK") == 1) {
                    if (job.get("ENABLE_FIXED_SCH") == 1) {
                        createFixedSchedule(job);
                    } else {
                        createCalendarSchedule(job);
                    }
                }
            }
            return params[0];

        }


        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            // set all the tasks as pending state
            SilentModeDAO silentmodeDAO = new SilentModeDAOImpl(this.jobService.getApplicationContext());
            silentmodeDAO.setCompleteState();
            // close database connection
            silentmodeDAO.close();
            jobService.jobFinished(jobParameters, false);
            Toast.makeText(jobService, "Task Finished", Toast.LENGTH_SHORT).show();
        }

        /**
         * create two alarms one for the start and one for the end for each day.
         *
         * *    1|TEST 1|1|0|1|0|2|0|0|0|6|0|22:12:00|06:12:00|1|0|0
         *
         *   map.put("TASK_ID", 1);
             map.put("TASK_NAME", TEST 1);
             map.put("ENABLE_TASK", 1);
             map.put("ENABLE_CAL_SYNC", 0);
             map.put("ENABLE_FIXED_SCH", 1);
             map.put("SUNDAY", 0);
             map.put("MONDAY", 2);
             map.put("TUESDAY", 0);
             map.put("WEDNESDAY", 0);
             map.put("THURSDAY", 0);
             map.put("FRIDAY", 6);
             map.put("SATURDAY", 0);
             map.put("START_TIME", 22:12:00);
             map.put("END_TIME", 06:12:00);
             map.put("ENABLE_SILENT_MODE", 1);
             map.put("ENABLE_VIBRATE_MODE", 0);
             map.put("VOLUME_LEVEL", 0);
         * @param job
         */
        void createFixedSchedule(Map<String, Object> job){
            Context context = this.jobService.getApplicationContext();
            SilentModeBroadcastReceiver silentModeBroadcastReceiver = new SilentModeBroadcastReceiver();

            int iAlarmMode = 2;
            if (job.get("ENABLE_SILENT_MODE") == 1) {
                iAlarmMode = 0;
            }
            else if (job.get("ENABLE_VIBRATE_MODE") == 1) {
                iAlarmMode = 1;
            }


            if (job.get("SUNDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for SUNDAY");
               // SilentModeBroadcastReceiver silentModeBroadcastReceiver = new SilentModeBroadcastReceiver();
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("SUNDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver = new SilentModeBroadcastReceiver();
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("SUNDAY"), (String) job.get("END_TIME"), 2);
            }
            if (job.get("MONDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for MONDAY");
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("MONDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("MONDAY"), (String) job.get("END_TIME"), 2 );
            }

            if (job.get("TUESDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for TUESDAY");
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("TUESDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("TUESDAY"), (String) job.get("END_TIME"), 2 );
            }
            if (job.get("WEDNESDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for WEDNESDAY");
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("WEDNESDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("WEDNESDAY"), (String) job.get("END_TIME"), 2 );
            }
            if (job.get("THURSDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for THURSDAY");
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("THURSDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("THURSDAY"), (String) job.get("END_TIME"), 2 );
            }
            if (job.get("FRIDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for FRIDAY");
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("FRIDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("FRIDAY"), (String) job.get("END_TIME"), 2 );
            }
            if (job.get("SATURDAY") != 0) {
                Log.i(TAG, "Setting a start and end alarm for SATURDAY");
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("SATURDAY"), (String) job.get("START_TIME"), iAlarmMode);
                silentModeBroadcastReceiver.SetTimeAlarm(context, (int) job.get("SATURDAY"), (String) job.get("END_TIME"), 2 );
            }

        }

        void createCalendarSchedule(Map<String, Object> job){
            // TODO: Implement reading the calendar to create Alarms
        }
    }


    /**
     *  below is code that I found on query you calendar/
     *  https://github.com/david-laundav/Android-CalendarService/blob/master/src/dk/CalendarService/CalendarService.java
     *
     */

    // TODO: research the code below to see if it will do what is needed to get a list of meetings
    public void queryCalendar() {
        // Run query
        Cursor cur = null;
        ContentResolver cr = this.getApplicationContext().getContentResolver();

        // Create a cursor and read from the calendar (for Android API below 4.0)
        final Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"),
                (new String[]{"_id", "displayName", "selected"}), null, null, null);


            /*
            * Use the cursor below for Android API 4.0+ (Thanks to SLEEPLisNight)
            *
            * Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"),
            * new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" },
            * null, null, null);
            *
            */

        // Create a set containing all of the calendar IDs available on the phone
        HashSet<String> calendarIds = getCalenderIds(cursor);

        // Create a hash map of calendar ids and the events of each id
        HashMap<String, List<CalendarEvent>> eventMap = new HashMap<String, List<CalendarEvent>>();

        // Loop over all of the calendars
        for (String id : calendarIds) {

            // Create a builder to define the time span
            Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
            long now = new Date().getTime();


            // create the time span based on the inputs
            ContentUris.appendId(builder, now - (DateUtils.DAY_IN_MILLIS * 0) - (DateUtils.HOUR_IN_MILLIS * 12));
            ContentUris.appendId(builder, now + (DateUtils.DAY_IN_MILLIS * 0) + (DateUtils.HOUR_IN_MILLIS * 12));

            // Create an event cursor to find all events in the calendar
            Cursor eventCursor = cr.query(builder.build(),
                    new String[]{"title", "begin", "end", "allDay"}, "Calendars._id=" + id,
                    null, "startDay ASC, startMinute ASC");

            System.out.println("eventCursor count=" + eventCursor.getCount());

            // If there are actual events in the current calendar, the count will exceed zero
            if (eventCursor.getCount() > 0) {

                // Create a list of calendar events for the specific calendar
                List<CalendarEvent> eventList = new ArrayList<CalendarEvent>();

                // Move to the first object
                eventCursor.moveToFirst();

                // Create an object of CalendarEvent which contains the title, when the event begins and ends,
                // and if it is a full day event or not
                CalendarEvent ce = loadEvent(eventCursor);

                // Adds the first object to the list of events
                eventList.add(ce);


                System.out.println(ce.toString());


                // While there are more events in the current calendar, move to the next instance
                while (eventCursor.moveToNext()) {

                    // Adds the object to the list of events
                    ce = loadEvent(eventCursor);
                    eventList.add(ce);

                    System.out.println(ce.toString());

                }

                Collections.sort(eventList);
                eventMap.put(id, eventList);

                System.out.println(eventMap.keySet().size() + " " + eventMap.values());

            }
        }
    }

    // Returns a new instance of the calendar object
    private static CalendarEvent loadEvent(Cursor csr) {
        return new CalendarEvent(csr.getString(0),
                new Date(csr.getLong(1)),
                new Date(csr.getLong(2)),
                !csr.getString(3).equals("0"));
    }

    // Creates the list of calendar ids and returns it in a set
    private static HashSet<String> getCalenderIds(Cursor cursor) {

        HashSet<String> calendarIds = new HashSet<String>();

        try
        {

            // If there are more than 0 calendars, continue
            if(cursor.getCount() > 0) {

                // Loop to set the id for all of the calendars
                while (cursor.moveToNext()) {

                    String _id = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    Boolean selected = !cursor.getString(2).equals("0");

                    System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
                    calendarIds.add(_id);

                }
            }

        }
        catch(AssertionError ex)
        {
            ex.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return calendarIds;

    }
}