package com.cs683.atshudy.assistmode.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.model.SilentModeDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.OnClickListener;

/**
 *
 *
 * referenced code for the timepicker in the following link
 * http://androidexample.com/Time_Picker_With_AM_PM_Values_-_Android_Example/index.php?view=article_discription&aid=86&aaid=109
 */
public class SilentEditTaskItemActivity extends ActionBarActivity {

    private String silentModeTaskName;
    private final String TAG = "com.cs683.atshudy.assistmode.views.SilentEditTaskItemActivity";

    private TextView taskName;
    private Switch taskEnabled;
    private RadioButton enableCalSync;
    private RadioButton enableFixedSch;
    private TextView startTime;
    private TextView endTime;
    private Button startTimeBtn;
    private Button endTimeBtn;
    private RadioButton enableSilentMode;
    private RadioButton vibrateMode;

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

        SimpleDateFormat frmTime = new SimpleDateFormat("h:mm a");
        String strTime = frmTime.format(datetime.getTime());
        timeText.setText(strTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silent_edit_task_item);

        // gets the information passed in from the act
        Bundle data = getIntent().getExtras();
        silentModeTaskName = data.getString("taskName");

        setTitle("Edit Task");

        // Create the text view
        taskName = (TextView) findViewById(R.id.taskname_txt);
        taskName.setText(silentModeTaskName);

        // call SQL statement to get the task to edit
        SilentModeDAO taskData;
        Map<String, Object> map = new HashMap<String, Object>();

        taskData = new SilentModeDAO(getBaseContext());
        map = taskData.getSilentModeTaskByName(silentModeTaskName);
        taskData.close();

        Button ok = (Button)findViewById(R.id.ok_btn);
        ok.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveDB();
            }
        });

        Button cancel = (Button)findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        // Capture our View elements
        taskEnabled = (Switch) findViewById(R.id.enable_sc);
        enableCalSync = (RadioButton)findViewById(R.id.EnableCal);
        enableFixedSch = (RadioButton)findViewById(R.id.EnableFixedSched);

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
        enableSilentMode = (RadioButton)findViewById(R.id.EnableSilentMode);
        vibrateMode = ((RadioButton)findViewById(R.id.vibrateMode));

        boolean bEnabled = (1 == (int)map.get("ENABLE_TASK"));
        taskEnabled.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("ENABLE_CAL_SYNC"));
        enableCalSync.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("ENABLE_FIXED_SCH"));
        UpdateDayOfWeekCheckboxes(bEnabled);
        if (bEnabled) {
            bEnabled = (1 <= (int)map.get("MONDAY"));
            ckMon.setChecked(bEnabled);
            bEnabled = (1 <= (int)map.get("TUESDAY"));
            ckTue.setChecked(bEnabled);
            bEnabled = (1 <= (int)map.get("WEDNESDAY"));
            ckWed.setChecked(bEnabled);
            bEnabled = (1 <= (int)map.get("THURSDAY"));
            ckThu.setChecked(bEnabled);
            bEnabled = (1 <= (int)map.get("FRIDAY"));
            ckFri.setChecked(bEnabled);
            bEnabled = (1 <= (int)map.get("SATURDAY"));
            ckSat.setChecked(bEnabled);
            bEnabled = (1 <= (int)map.get("SUNDAY"));
            ckSun.setChecked(bEnabled);

            startTime.setText((String)map.get("START_TIME"));
            endTime.setText((String)map.get("END_TIME"));
        }
        bEnabled = (1 == (int)map.get("ENABLE_SILENT_MODE"));
        enableSilentMode.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("ENABLE_VIBRATE_MODE"));
        vibrateMode.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("VOLUME_LEVEL"));


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

    public void onSaveDB(){
        Log.i(TAG, " Saving task to db");

        String sTaskName = taskName.getText().toString();
        int iTaskEnable = (taskEnabled.isChecked()) ? 1 : 0;
        int iEnableCalSync = (enableCalSync.isChecked()) ? 1 : 0;
        int iEnableFixedSch = (enableFixedSch.isChecked()) ? 1 : 0;
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
            DateFormat formatter = new SimpleDateFormat("hh:mm");
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
      /*      iStartHours = Integer.parseInt(sStartTime.substring(0, sStartTime.indexOf(":")));
            iStartMinutes = Integer.parseInt(sStartTime.substring(sStartTime.indexOf(":") + 1, sStartTime.length() - 3));
            iEndHours = Integer.parseInt(sEndTime.substring(0, sEndTime.indexOf(":")));
            iEndMinutes = Integer.parseInt(sEndTime.substring(sEndTime.indexOf(":") + 1, sEndTime.length() - 3));
        */
        }


        int iEnableSilentMode = (enableSilentMode.isChecked()) ? 1 : 0;
        int iEnableVibrate =    (vibrateMode.isChecked()) ? 1 : 0;
        int iVolumeLevel = ((SeekBar)findViewById(R.id.seekBar)).getProgress();

        try {
            SilentModeDAO data = new SilentModeDAO(getBaseContext());
            data.updateSilentModeTaskById(sTaskName, iTaskEnable, iEnableCalSync,
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

        exit();
    }

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
        getMenuInflater().inflate(R.menu.menu_edit_task_item, menu);
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

    private void UpdateDayOfWeekCheckboxes(boolean bEnabled) {
        ckMon.setEnabled(bEnabled);
        ckTue.setEnabled(bEnabled);
        ckWed.setEnabled(bEnabled);
        ckThu.setEnabled(bEnabled);
        ckFri.setEnabled(bEnabled);
        ckSat.setEnabled(bEnabled);
        ckSun.setEnabled(bEnabled);
        startTime.setEnabled(bEnabled);
        endTime.setEnabled(bEnabled);
        startTimeBtn.setEnabled(bEnabled);
        endTimeBtn.setEnabled(bEnabled);
        findViewById(R.id.toLabelLabel).setEnabled(bEnabled);
    }

}
