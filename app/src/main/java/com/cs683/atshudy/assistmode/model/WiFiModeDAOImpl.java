package com.cs683.atshudy.assistmode.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ATshudy on 4/29/2015.
 */
public class WiFiModeDAOImpl implements WiFiModeDAO {
    public static final String TAG = "WiFiModeDAOImpl";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = {
            DBHelper.COLUMN_WIFI_TASK_ID,
            DBHelper.COLUMN_WIFI_TASK_NAME,
            DBHelper.COLUMN_WIFI_ENABLE_TASK,
            DBHelper.COLUMN_WIFI_ENABLE_CONN_TIMEOUT,
            DBHelper.COLUMN_WIFI_CONN_TIMEOUT,
            DBHelper.COLUMN_WIFI_ENABLE_LOCATION,
            DBHelper.COLUMN_WIFI_LOCATION,
            DBHelper.COLUMN_WIFI_ENABLE_BATTERY_USEAGE,
            DBHelper.COLUMN_WIFI_MIN_BATTERY_USEAGE
    };

    public WiFiModeDAOImpl(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public WiFiModeTask createWiFiModeTask(String name, int iEnableTask, int iEnableConnectionTimeOut,
                                           int iConnectionTimout, int iEnableLocation, String sLocationName, int iEnableBatteryUsage,
                                           int iMinBatteryUsageLevel) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_WIFI_TASK_NAME, name.toUpperCase());
        values.put(DBHelper.COLUMN_WIFI_ENABLE_TASK, iEnableTask);
        values.put(DBHelper.COLUMN_WIFI_ENABLE_CONN_TIMEOUT, iEnableConnectionTimeOut);
        values.put(DBHelper.COLUMN_WIFI_CONN_TIMEOUT, iConnectionTimout);
        values.put(DBHelper.COLUMN_WIFI_ENABLE_LOCATION, iEnableLocation);
        values.put(DBHelper.COLUMN_WIFI_LOCATION, sLocationName);
        values.put(DBHelper.COLUMN_WIFI_ENABLE_BATTERY_USEAGE, iEnableBatteryUsage);
        values.put(DBHelper.COLUMN_WIFI_MIN_BATTERY_USEAGE, iMinBatteryUsageLevel);

        long insertId = mDatabase.insert(DBHelper.TABLE_WIFI_MODE, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_WIFI_MODE,
                mAllColumns, DBHelper.COLUMN_WIFI_TASK_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        WiFiModeTask newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
    }

    public Map<String, Object> getWiFiModeTaskByName(String taskName) {
        Map<String, Object> map = new HashMap<String, Object>();
        Cursor cursor = mDatabase.query(DBHelper.TABLE_WIFI_MODE,
                mAllColumns, DBHelper.COLUMN_WIFI_TASK_NAME + " = '" + taskName+"'", null, null, null, null);
        cursor.moveToFirst();
        map.put("TASK_ID", cursor.getLong(0));
        map.put("TASK_NAME", cursor.getString(1));
        map.put("ENABLE_TASK", cursor.getInt(2));
        map.put("ENABLE_CONN_TIMEOUT", cursor.getInt(3));
        map.put("CONN_TIMEOUT", cursor.getInt(4));
        map.put("ENABLE_LOCATION", cursor.getInt(5));
        map.put("LOCATION", cursor.getString(6));
        map.put("ENABLE_BATTERY_USEAGE", cursor.getInt(7));
        map.put("MIN_BATTERY_USEAGE", cursor.getInt(8));

        cursor.close();
        return map;
    }

    public ArrayList<Map<String, Object>> getWiFimodeTaskJobs(){
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList listOfTasks = new ArrayList<Map<String, Object>>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_WIFI_MODE,
                mAllColumns, DBHelper.COLUMN_WIFI_TASK_NAME + " != " +DBHelper.COMPLETE , null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            map.put("TASK_ID", cursor.getLong(0));
            map.put("TASK_NAME", cursor.getString(1));
            map.put("ENABLE_TASK", cursor.getInt(2));
            map.put("ENABLE_CONN_TIMEOUT", cursor.getInt(3));
            map.put("CONN_TIMEOUT", cursor.getInt(4));
            map.put("ENABLE_LOCATION", cursor.getInt(5));
            map.put("LOCATION", cursor.getString(6));
            map.put("ENABLE_BATTERY_USEAGE", cursor.getInt(7));
            map.put("MIN_BATTERY_USEAGE", cursor.getInt(8));
            listOfTasks.add(map);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listOfTasks;
    }

    public void setCompleteState(){
        ContentValues values = new ContentValues();
        values.put( DBHelper.COLUMN_WIFI_TASK_STATE, DBHelper.COMPLETE);
        mDatabase.update(DBHelper.TABLE_WIFI_MODE, values, null, null);
    }

    public void updateWiFiModeTaskById(String name, int iEnableTask, int iEnableConnectionTimeOut,
                                       int iConnectionTimout, int iEnableLocation, String sLocationName, int iEnableBatteryUsage,
                                       int iMinBatteryUsageLevel) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_WIFI_TASK_NAME, name.toUpperCase());
        values.put(DBHelper.COLUMN_WIFI_ENABLE_TASK, iEnableTask);
        values.put(DBHelper.COLUMN_WIFI_ENABLE_CONN_TIMEOUT, iEnableConnectionTimeOut);
        values.put(DBHelper.COLUMN_WIFI_CONN_TIMEOUT, iConnectionTimout);
        values.put(DBHelper.COLUMN_WIFI_ENABLE_LOCATION, iEnableLocation);
        values.put(DBHelper.COLUMN_WIFI_LOCATION, sLocationName);
        values.put(DBHelper.COLUMN_WIFI_ENABLE_BATTERY_USEAGE, iEnableBatteryUsage);
        values.put(DBHelper.COLUMN_WIFI_MIN_BATTERY_USEAGE, iMinBatteryUsageLevel);
        mDatabase.update(DBHelper.TABLE_WIFI_MODE, values, DBHelper.COLUMN_WIFI_TASK_NAME+"='"+name+"'", null);
    }
    public void deleteWiFiModeTask(WiFiModeTask task) throws SQLException {
        String name = task.getName().toUpperCase();
        Log.i(TAG, "the deleted task has the id: " + name);
        mDatabase.delete(DBHelper.TABLE_WIFI_MODE, DBHelper.COLUMN_WIFI_TASK_NAME + " = '" + name+"'", null);
    }

    public ArrayList<WiFiModeTask> getAllWiFiModeTaskModeTasks() {
        ArrayList<WiFiModeTask> listTasks = new ArrayList<WiFiModeTask>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_WIFI_MODE,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WiFiModeTask task = cursorToTask(cursor);
            listTasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listTasks;
    }

    private WiFiModeTask cursorToTask(Cursor cursor) {
        WiFiModeTask task = new WiFiModeTask();
        task.setId(cursor.getLong(0));
        task.setName(cursor.getString(1));
        return task;
    }

}
