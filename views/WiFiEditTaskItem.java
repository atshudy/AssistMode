package com.cs683.atshudy.assistmode.views;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.model.WiFiModeDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WiFiEditTaskItem extends ActionBarActivity {

    private final String TAG = "com.cs683.atshudy.assistmode.views.WiFiEditTaskItem";

    Switch taskEnabled;
    Switch wifi_lowBat_sc;
    Switch wifi_location_sc;
    Switch wifi_timeout_sc;
    TextView taskName;

    String[] mlsConnectionTimeouts = {"1 minute","2 minutes","3 minutes","4 minutes","5 minutes"};
    Spinner mTimeoutSpinner;
    TextView mTimeoutValue;

    Spinner mWifiLocationSpinner;
    TextView mWifiLocationValue;
    WifiManager mWifiManager;
    ArrayList<String> mlWifiConnections = new ArrayList<String>();

    String wifiModeTaskName;
    WiFiModeDAO data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_edit_task_item);

        // gets the information passed in from the act
        Bundle data = getIntent().getExtras();
        wifiModeTaskName = data.getString("taskName");

        setTitle("Edit Task");

        // call SQL statement to get the task to edit
        WiFiModeDAO taskData;
        Map<String, Object> map = new HashMap<String, Object>();

        taskData = new WiFiModeDAO(getBaseContext());
        map = taskData.getWiFiModeTaskByName(wifiModeTaskName);

        // get all view controls
        taskName = (TextView) findViewById(R.id.taskname_txt);
        taskEnabled = (Switch) findViewById(R.id.enabled_sc);
        wifi_timeout_sc = (Switch) findViewById(R.id.disable_timeout_sc);
        mTimeoutSpinner = (Spinner) findViewById(R.id.spinnerConnTimeout);
        wifi_location_sc = (Switch) findViewById(R.id.Location_enabled_sc);
        mWifiLocationSpinner = (Spinner) findViewById(R.id.spinnerWifiLocation);
        wifi_lowBat_sc = (Switch)findViewById(R.id.disable_wifi_lowBat_sc);

        // Add the task name to the text view
        taskName.setText(wifiModeTaskName);
        boolean bEnabled = (1 == (int)map.get("ENABLE_TASK"));
        taskEnabled.setChecked(bEnabled);
        bEnabled = (1 == (int)map.get("ENABLE_CONN_TIMEOUT"));
        wifi_timeout_sc.setChecked(bEnabled);
        updateConnectionTimeOutSpinner(bEnabled, (int)map.get("CONN_TIMEOUT") );

        bEnabled = (1 == (int)map.get("ENABLE_LOCATION"));
        wifi_location_sc.setChecked(bEnabled);

        updateLocationSpinner(bEnabled, (String)map.get("LOCATION"));
        bEnabled = (1 == (int)map.get("ENABLE_BATTERY_USEAGE"));
        wifi_lowBat_sc.setChecked(bEnabled);



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

        setConnectionTimeOut();
        setWiFiLocation();
        setBatteryUsage();
    }

    private void setBatteryUsage() {
        wifi_lowBat_sc = (Switch)findViewById(R.id.disable_wifi_lowBat_sc);
        //Get value from database
        boolean enabled = false;
        wifi_lowBat_sc.setChecked(enabled);
        wifi_lowBat_sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });
    }

    private void setWiFiLocation() {
        wifi_location_sc = (Switch) findViewById(R.id.Location_enabled_sc);
        //Get value from database

        wifi_location_sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                updateLocationSpinner(isChecked, null);
            }
        });
    }

    private void updateLocationSpinner(boolean enabled, String location) {
        mWifiLocationSpinner = (Spinner) findViewById(R.id.spinnerWifiLocation);
        mWifiLocationSpinner.setEnabled(enabled);
        if (enabled) {
            mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> configureHotSpots = mWifiManager.getConfiguredNetworks();
            if (configureHotSpots == null) {
                mlWifiConnections.add("Networks are not available");
            } else {
                for (WifiConfiguration wifiConfig : configureHotSpots) {
                    mlWifiConnections.add(wifiConfig.SSID);
                }
            }

            // if the location name exists in the database
            // add it to the end of list and set it as the selected item
            //****
            if (location != null){
                mWifiLocationSpinner.setSelection(mlWifiConnections.indexOf(location));
            }

            /*****/
            ArrayAdapter<String> wifi_adapter = new ArrayAdapter<String>(WiFiEditTaskItem.this,
                    android.R.layout.simple_spinner_item, mlWifiConnections);

            mWifiLocationSpinner.setAdapter(wifi_adapter);
            mWifiLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mWifiLocationValue = (TextView) view;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mWifiLocationValue.setText("");
                }
            });
        }
    }

    private void setConnectionTimeOut() {
        wifi_timeout_sc = (Switch) findViewById(R.id.disable_timeout_sc);
        mTimeoutSpinner = (Spinner) findViewById(R.id.spinnerConnTimeout);

        wifi_timeout_sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                updateConnectionTimeOutSpinner(isChecked, -1);
            }
        });
    }

    private void updateConnectionTimeOutSpinner(boolean enabled, int timeout ){
        mTimeoutSpinner.setEnabled(enabled);
        if (enabled) {
            //Get value from database

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(WiFiEditTaskItem.this,
                    android.R.layout.simple_spinner_item, mlsConnectionTimeouts);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTimeoutSpinner.setAdapter(adapter);
            if (timeout != -1) {
                mTimeoutSpinner.setSelection(timeout-1);
            }
            mTimeoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mTimeoutValue = (TextView) view;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mTimeoutValue.setText("");
                }
            });
        }
    }

    private void exit(){
        Log.i(TAG, " calling exit()");
        this.finish();
    }

    private void onSaveDB() {
        Log.i(TAG, " Saving task to db");

        data = new WiFiModeDAO(getBaseContext());

        String sTaskName = taskName.getText().toString();
        int iEnableTask = (taskEnabled.isChecked()) ? 1 : 0;
        int iEnableConnectionTimeOut = (wifi_timeout_sc.isChecked()) ? 1 : 0;
        int iConnectionTimout = mTimeoutSpinner.getSelectedItemPosition() + 1;
        int iEnableLocation = (wifi_location_sc.isChecked()) ? 1 : 0;
        String sLocationName = mWifiLocationSpinner.getItemAtPosition(mWifiLocationSpinner.getSelectedItemPosition()).toString();
        int iEnableBatteryUsage = (wifi_lowBat_sc.isChecked()) ? 1 : 0;
        int iMinBatteryUsageLevel = (wifi_lowBat_sc.isChecked()) ? 10 : 0;

        try {
            data.updateWiFiModeTaskById(sTaskName, iEnableTask, iEnableConnectionTimeOut, iConnectionTimout, iEnableLocation,
                    sLocationName, iEnableBatteryUsage,iMinBatteryUsageLevel) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        exit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wi_fi_add_task_item, menu);
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
