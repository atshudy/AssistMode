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
public class DrivingModeDAOImpl implements DrivingModeDAO {
    public static final String TAG = "DrivingModeDAOImpl";
    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_DRIVING_TASK_ID,
            DBHelper.COLUMN_DRIVING_TASK_NAME,
            DBHelper.COLUMN_DRIVING_ENABLE_TASK,
            DBHelper.COLUMN_DRIVING_ENABLE_SEND_TEXT,
            DBHelper.COLUMN_DRIVING_ENABLE_HANDS_FREE_MODE
    };

    public DrivingModeDAOImpl(Context context) throws SQLException {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        try {
            open();
        }
        catch(SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Object> getDrivingModeTaskByName(String taskName) {
        Map<String, Object> map = new HashMap<String, Object>();
        Cursor cursor = mDatabase.query(DBHelper.TABLE_DRIVING_MODE,
                mAllColumns, DBHelper.COLUMN_DRIVING_TASK_NAME + " = '" + taskName+"'", null, null, null, null);
        cursor.moveToFirst();
        map.put("TASK_ID", cursor.getLong(0));
        map.put("TASK_NAME", cursor.getString(1));
        map.put("ENABLE_TASK", cursor.getInt(2));
        map.put("ENABLE_SEND_TEXT", cursor.getInt(3));
        map.put("ENABLE_HANDS_FREE_MODE", cursor.getInt(4));
        cursor.close();
        return map;
    }

    public void open() throws SQLException  {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public DrivingModeTask createDrivingModeTask(String name , int iEnableTask, int iEnableTextMsg, int iEnableHandsFreeMode) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DRIVING_TASK_NAME, name.toUpperCase());
        values.put(DBHelper.COLUMN_DRIVING_ENABLE_TASK, iEnableTask);
        values.put(DBHelper.COLUMN_DRIVING_ENABLE_SEND_TEXT, iEnableTextMsg);
        values.put(DBHelper.COLUMN_DRIVING_ENABLE_HANDS_FREE_MODE, iEnableHandsFreeMode);
        long insertId = mDatabase.insert(DBHelper.TABLE_DRIVING_MODE, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_DRIVING_MODE,
                mAllColumns, DBHelper.COLUMN_DRIVING_TASK_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        DrivingModeTask newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
    }

    public void updateDrivingModeTaskById(String name , int iEnableTask, int iEnableTextMsg, int iEnableHandsFreeMode) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DRIVING_TASK_NAME, name);
        values.put(DBHelper.COLUMN_DRIVING_ENABLE_TASK, iEnableTask);
        values.put(DBHelper.COLUMN_DRIVING_ENABLE_SEND_TEXT, iEnableTextMsg);
        values.put(DBHelper.COLUMN_DRIVING_ENABLE_HANDS_FREE_MODE, iEnableHandsFreeMode);
        mDatabase.update(DBHelper.TABLE_DRIVING_MODE, values, DBHelper.COLUMN_DRIVING_TASK_NAME+"='"+name+"'", null);
    }

    public void deleteDrivingModeTask(DrivingModeTask task) throws SQLException{
        String name = task.getName().toUpperCase();
        Log.i(TAG, "the deleted task has the id: " + name);
        mDatabase.delete(DBHelper.TABLE_DRIVING_MODE, DBHelper.COLUMN_DRIVING_TASK_NAME + " = '" + name+"'", null);
    }

    public ArrayList<DrivingModeTask> getAllDrivingModeTasks() {
        ArrayList<DrivingModeTask> listTasks = new ArrayList<DrivingModeTask>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_DRIVING_MODE,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DrivingModeTask task = cursorToTask(cursor);
            listTasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listTasks;
    }

    private DrivingModeTask cursorToTask(Cursor cursor) {
        DrivingModeTask task = new DrivingModeTask();
        task.setId(cursor.getLong(0));
        task.setName(cursor.getString(1));
        return task;
    }

}
