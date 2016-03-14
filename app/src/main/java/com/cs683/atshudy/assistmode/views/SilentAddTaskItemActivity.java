package com.cs683.atshudy.assistmode.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.business.SilentModeService;
import com.cs683.atshudy.assistmode.model.SilentModeDAO;
import com.cs683.atshudy.assistmode.model.SilentModeDAOImpl;

import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

/**
 *
 *
 * referenced code for the timepicker in the following link
 * http://androidexample.com/Time_Picker_With_AM_PM_Values_-_Android_Example/index.php?view=article_discription&aid=86&aaid=109
 */

public class SilentAddTaskItemActivity extends ActionBarActivity {

    private final String TAG = "com.cs683.atshudy.assistmode.views.SilentAddTaskItemActivity";

    private EditText taskName;
    private Switch taskEnabled;
    private TextView startTime;
    private TextView endTime;
    private Button startTimeBtn;
    private Button endTimeBtn;

    private int iHour;
    private int iMinute;
    private CheckBox ckMon;
    private CheckBox ckTue;
    private CheckBox ckWed;
    private CheckBox ckThu;
    private CheckBox ckFri;
    private CheckBox ckSat;
    private CheckBox ckSun;

    // This integer will uniquely define the dialog to be used for displaying time picker.
    static final int START_TIME_DIALOG_ID = 0;
    static final int END_TIME_DIALOG_ID = 1;

    private static final int JOB_ID = 101;

    JobScheduler mJobScheduler;

    // Callback received when the user "picks" a time in the dialog
    private TimePickerDialog.OnTimeSetListener mStartTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    iHour = hourOfDay;
                    iMinute = minute;
                    updateDisplay(startTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mEndTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    iHour = hourOfDay;
                    iMinute = minute;
                    updateDisplay(endTime);
                }
            };

    // Updates the time in the TextView
    private void updateDisplay(TextView timeText) {
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, iHour);
        datetime.set(Calendar.MINUTE, iMinute);

        SimpleDateFormat frmTime = new SimpleDateFormat("H:mm a");
        String strTime = frmTime.format(datetime.getTime());
        timeText.setText(strTime);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silent_add_task_item);

        if (mJobScheduler != null){
            mJobScheduler.cancel(JOB_ID);
        }
        setTitle("Add Task");

        Button ok = (Button)findViewById(R.id.ok_btn);
        ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveDB();
                final JobInfo job = new JobInfo.Builder(JOB_ID, new ComponentName(getApplicationContext(), SilentModeService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .setPeriodic(10 * 1000)
                        .setPersisted(true)
                        .build();
                mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                mJobScheduler.schedule(job);
            }
        });

        Button cancel = (Button)findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        // Capture our View elements
        taskName = (EditText) findViewById(R.id.taskname_edt);
        taskEnabled = (Switch) findViewById(R.id.enable_sc);
        startTime = (TextView) findViewById(R.id.start_time_txt);
        startTimeBtn = (Button) findViewById(R.id.starttime_btn);
        endTime = (TextView) findViewById(R.id.end_time_text);
        endTimeBtn = (Button) findViewById(R.id.endtime_btn);
        ckMon = (CheckBox) findViewById(R.id.mon_chkbox);
        ckTue = (CheckBox) findViewById(R.id.tue_chkbox);
        ckWed = (CheckBox) findViewById(R.id.wed_chkbox);
        ckThu = (CheckBox) findViewById(R.id.thu_chkbox);
        ckFri = (CheckBox) findViewById(R.id.fri_chkbox);
        ckSat = (CheckBox) findViewById(R.id.sat_chkbox);
        ckSun = (CheckBox) findViewById(R.id.sun_chkbox);

        // Get the current time
        final Calendar cal = Calendar.getInstance();
        iHour = cal.get(Calendar.HOUR_OF_DAY);
        iMinute = cal.get(Calendar.MINUTE);

        // Listener for click events
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_TIME_DIALOG_ID);
            }
        });
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_TIME_DIALOG_ID);
            }
        });
    }

    public void exit(){
        Log.i(TAG, " calling exit()");
        super.finish();
    }

    /**
     * values.put(DBHelper.COLUMN_SILENT_ENABLE_CAL_SYNC, iEnableCalSync+"");
     values.put(DBHelper.COLUMN_SILENT_ENABLE_FIXED_SCH, iEnableFixedSch+"");
     values.put(DBHelper.COLUMN_SILENT_SUNDAY, iSun*1+"");
     values.put(DBHelper.COLUMN_SILENT_MONDAY, iMon*2+"");
     values.put(DBHelper.COLUMN_SILENT_TUESDAY, iTue*3+"");
     values.put(DBHelper.COLUMN_SILENT_WEDNESDAY, iWed*4+"");
     values.put(DBHelper.COLUMN_SILENT_THURSDAY, iThu*5+"");
     values.put(DBHelper.COLUMN_SILENT_FRIDAY, iFri*6+"");
     values.put(DBHelper.COLUMN_SILENT_SATURDAY, iSat*7+"");
     values.put(DBHelper.COLUMN_SILENT_HOURS_START_TIME, iHoursStartTime+"");
     values.put(DBHelper.COLUMN_SILENT_MINUTES_START_TIME, iMinutesStartTime+"");
     values.put(DBHelper.COLUMN_SILENT_HOURS_END_TIME, iHoursEndTime+"");
     values.put(DBHelper.COLUMN_SILENT_MINUTES_END_TIME, iMinutesEndTime+"");
     values.put(DBHelper.COLUMN_SILENT_ENABLE_SILENT_MODE, iEnableSilentMode+"");
     values.put(DBHelper.COLUMN_SILENT_ENABLE_VIBRATE_MODE, iEnableVibrate+"");
     values.put(DBHelper.COLUMN_SILENT_VOLUME_LEVEL, iVolumeLevel+"");
     */
    public void onSaveDB(){
        Log.i(TAG, " Saving task to db");
        String sTaskName = taskName.getText().toString();
        int iTaskEnable = (taskEnabled.isChecked()) ? 1 : 0;
        int iEnableCalSync = (((RadioButton)findViewById(R.id.EnableCal)).isChecked()) ? 1 : 0;
        int iEnableFixedSch = (((RadioButton)findViewById(R.id.EnableFixedSched)).isChecked()) ? 1 : 0;
        int iSun = (ckSun.isChecked()) ? 1*1 : 0;
        int iMon = (ckMon.isChecked()) ? 1*2 : 0;
        int iTue = (ckTue.isChecked()) ? 1*3 : 0;
        int iWed = (ckWed.isChecked()) ? 1*4 : 0;
        int iThu = (ckThu.isChecked()) ? 1*5 : 0;
        int iFri = (ckFri.isChecked()) ? 1*6 : 0;
        int iSat = (ckSat.isChecked()) ? 1*7 : 0;

        int iStartHours = 0;
        int iStartMinutes = 0;
        int iEndHours = 0;
        int iEndMinutes = 0;
        Time tStartTime = null;
        Time tEndTime = null;
        if (iEnableFixedSch == 1) {
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            String sTime = startTime.getText().toString();
            try {
                tStartTime = new Time(formatter.parse(sTime).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            sTime = endTime.getText().toString();
            try {
                tEndTime = new Time(formatter.parse(sTime).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        int iEnableSilentMode = (((RadioButton)findViewById(R.id.EnableSilentMode)).isChecked()) ? 1 : 0;
        int iEnableVibrate =    (((RadioButton)findViewById(R.id.vibrateMode)).isChecked()) ? 1 : 0;
        int iVolumeLevel = ((SeekBar)findViewById(R.id.seekBar)).getProgress();

        try {
            SilentModeDAO data;
            data = new SilentModeDAOImpl(getBaseContext());
            data.createSilentModeTask(sTaskName, iTaskEnable, iEnableCalSync,
                    iEnableFixedSch, iSun, iMon, iTue, iWed,
                    iThu, iFri, iSat, tStartTime, tEndTime,
                    iEnableSilentMode, iEnableVibrate, iVolumeLevel);
            data.close();
            exit();
        }
        catch (SQLException e)
        {
            Log.e(TAG, e.getErrorCode()+":"+e.getMessage());
            e.printStackTrace();
        }
    }

    /** Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mStartTimeSetListener, iHour, iMinute, false);
            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mEndTimeSetListener, iHour, iMinute, false);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnScheduleRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.EnableCal:
                if (checked)
                    // disable the controls for the Fixed Schedule
                    UpdateDayOfWeekCheckboxes(false);
                break;
            case R.id.EnableFixedSched:
                if (checked)
                    // enable the controls for the Fixed schedule
                    UpdateDayOfWeekCheckboxes(true);
                break;
        }

    }

    public void OnModeRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        SeekBar volumeControl = (SeekBar)findViewById(R.id.seekBar);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.adjustVolume:
                if (checked)
                    // enable the controls for the Fixed schedule
                    volumeControl.setEnabled(true);
                break;
            default:
                if (checked)
                    // enable the controls for the Fixed schedule
                    volumeControl.setEnabled(false);
                break;
        }

    }

    private void UpdateDayOfWeekCheckboxes(boolean checked) {
        ckMon.setEnabled(checked);
        ckTue.setEnabled(checked);
        ckWed.setEnabled(checked);
        ckThu.setEnabled(checked);
        ckFri.setEnabled(checked);
        ckSat.setEnabled(checked);
        ckSun.setEnabled(checked);
        startTime.setEnabled(checked);
        endTime.setEnabled(checked);
        startTimeBtn.setEnabled(checked);
        endTimeBtn.setEnabled(checked);
        findViewById(R.id.toLabelLabel).setEnabled(checked);
    }
}
