package com.cs683.atshudy.assistmode.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.model.DrivingModeDAO;

import java.sql.SQLException;

public class DrivingAddTaskItem extends ActionBarActivity {

    private final String TAG = "com.cs683.atshudy.assistmode.views.DrivingAddTaskItem";
    DrivingModeDAO data;
    EditText taskName;
    Switch taskEnabled;
    RadioButton sendTxt_rad;
    RadioButton handsFree_rad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_add_task_item);
        setTitle("Add Task");

        // Get the from controls
        taskName = (EditText)findViewById(R.id.taskname_edt);
        taskEnabled = (Switch)findViewById(R.id.enable_sc);
        sendTxt_rad = (RadioButton)findViewById(R.id.sendTxt_rad);
        handsFree_rad = (RadioButton)findViewById(R.id.handsFree_rad);

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
        super.finish();
    }

    public void onSaveDB(){
        Log.i(TAG, " Saving task to db");

        data = new DrivingModeDAO(getBaseContext());

        String sTaskName = taskName.getText().toString();
        int iEnableTask = (taskEnabled.isChecked()) ? 1 : 0;
        int iEnableTextMsg = (sendTxt_rad.isChecked()) ? 1 : 0;
        int iEnableHandsFreeMode = (handsFree_rad.isChecked()) ? 1 : 0;

        try {
            data.createDrivingModeTask(sTaskName , iEnableTask, iEnableTextMsg, iEnableHandsFreeMode) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        exit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driving_add_task_item, menu);
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
