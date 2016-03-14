package com.cs683.atshudy.assistmode.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.model.DrivingModeDAO;
import com.cs683.atshudy.assistmode.model.DrivingModeDAOImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DrivingEditTaskItem extends ActionBarActivity {

    private String DrivingModeTaskName;
    private final String TAG = "com.cs683.atshudy.assistmode.views.WiFiEditTaskItem";

    TextView taskName;
    Switch taskEnabled;
    RadioButton sendTxt_rad;
    RadioButton handsFree_rad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_edit_task_item);
        // gets the information passed in from the act
        Bundle data = getIntent().getExtras();
        DrivingModeTaskName = data.getString("taskName");

        setTitle("Edit Task");

        // call SQL statement to get the task to edit
        DrivingModeDAO taskData;
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            taskData = new DrivingModeDAOImpl(getBaseContext());
            map = taskData.getDrivingModeTaskByName(DrivingModeTaskName);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Get the from controls
        taskName = (TextView) findViewById(R.id.taskname_txt);
        taskEnabled = (Switch)findViewById(R.id.enable_sc);
        sendTxt_rad = (RadioButton)findViewById(R.id.sendTxt_rad);
        handsFree_rad = (RadioButton)findViewById(R.id.handsFree_rad);

        // set the text in the text view
        taskName.setText(DrivingModeTaskName);
        boolean bEnabled = (1 == (int)map.get("ENABLE_TASK"));
        taskEnabled.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("ENABLE_SEND_TEXT"));
        sendTxt_rad.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("ENABLE_HANDS_FREE_MODE"));
        handsFree_rad.setChecked(bEnabled);

        Button ok = (Button)findViewById(R.id.ok_btn);
        ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveDB();
            }
        });

        Button cancel = (Button)findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    public void exit(){
        Log.i(TAG, " calling exit()");
        this.finish();
    }

    public void onSaveDB() {
        Log.i(TAG, " Saving task to db");

        DrivingModeDAO data;


        String sTaskName = taskName.getText().toString();
        int iEnableTask = (taskEnabled.isChecked()) ? 1 : 0;
        int iEnableTextMsg = (sendTxt_rad.isChecked()) ? 1 : 0;
        int iEnableHandsFreeMode = (handsFree_rad.isChecked()) ? 1 : 0;

        try {
            data = new DrivingModeDAOImpl(getBaseContext());
            data.updateDrivingModeTaskById(sTaskName, iEnableTask, iEnableTextMsg, iEnableHandsFreeMode) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        exit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driving_edit_task_item, menu);
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
}
