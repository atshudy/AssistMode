package com.cs683.atshudy.assistmode.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ATshudy on 3/11/2015.
 */
public class SilentModeDAO {

    public static final String TAG = "SilentModeDAO";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = {
            DBHelper.COLUMN_SILENT_TASK_ID,
            DBHelper.COLUMN_SILENT_TASK_NAME,
            DBHelper.COLUMN_SILENT_ENABLE_TASK,
            DBHelper.COLUMN_SILENT_ENABLE_CAL_SYNC,
            DBHelper.COLUMN_SILENT_ENABLE_FIXED_SCH,
            DBHelper.COLUMN_SILENT_SUNDAY,
            DBHelper.COLUMN_SILENT_MONDAY,
            DBHelper.COLUMN_SILENT_TUESDAY,
            DBHelper.COLUMN_SILENT_WEDNESDAY,
            DBHelper.COLUMN_SILENT_THURSDAY,
            DBHelper.COLUMN_SILENT_FRIDAY,
            DBHelper.COLUMN_SILENT_SATURDAY,
            DBHelper.COLUMN_SILENT_START_TIME,
            DBHelper.COLUMN_SILENT_END_TIME,
            DBHelper.COLUMN_SILENT_ENABLE_SILENT_MODE,
            DBHelper.COLUMN_SILENT_ENABLE_VIBRATE_MODE,
            DBHelper.COLUMN_SILENT_VOLUME_LEVEL
    };

    public SilentModeDAO(Context context) {
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

    public SilentModeTask createSilentModeTask(String name, int iEnableTask, int iEnableCalSync,
                                               int iEnableFixedSch, int iSun, int iMon, int iTue, int iWed,
                                               int iThu, int iFri, int iSat, Time tStartTime, Time tEndTime,
                                               int iEnableSilentMode, int iEnableVibrate, int iVolumeLevel) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SILENT_TASK_NAME, name.toUpperCase());
        values.put(DBHelper.COLUMN_SILENT_ENABLE_TASK, iEnableTask+"");
        values.put(DBHelper.COLUMN_SILENT_ENABLE_CAL_SYNC, iEnableCalSync+"");
        values.put(DBHelper.COLUMN_SILENT_ENABLE_FIXED_SCH, iEnableFixedSch+"");
        values.put(DBHelper.COLUMN_SILENT_SUNDAY, iSun+"");
        values.put(DBHelper.COLUMN_SILENT_MONDAY, iMon+"");
        values.put(DBHelper.COLUMN_SILENT_TUESDAY, iTue+"");
        values.put(DBHelper.COLUMN_SILENT_WEDNESDAY, iWed+"");
        values.put(DBHelper.COLUMN_SILENT_THURSDAY, iThu+"");
        values.put(DBHelper.COLUMN_SILENT_FRIDAY, iFri+"");
        values.put(DBHelper.COLUMN_SILENT_SATURDAY, iSat+"");
        values.put(DBHelper.COLUMN_SILENT_START_TIME, tStartTime.toString());
        values.put(DBHelper.COLUMN_SILENT_END_TIME, tEndTime.toString());
        values.put(DBHelper.COLUMN_SILENT_ENABLE_SILENT_MODE, iEnableSilentMode+"");
        values.put(DBHelper.COLUMN_SILENT_ENABLE_VIBRATE_MODE, iEnableVibrate+"");
        values.put(DBHelper.COLUMN_SILENT_VOLUME_LEVEL, iVolumeLevel+"");

        long insertId = mDatabase.insert(DBHelper.TABLE_SILENT_MODE, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_SILENT_MODE,
                mAllColumns, DBHelper.COLUMN_SILENT_TASK_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        SilentModeTask newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
    }

    public Map<String, Object> getSilentModeTaskByName(String taskName) {
        Map<String, Object> map = new HashMap<String, Object>();
        Cursor cursor = mDatabase.query(DBHelper.TABLE_SILENT_MODE,
                mAllColumns, DBHelper.COLUMN_SILENT_TASK_NAME + " = '" + taskName + "'", null, null, null, null);
        cursor.moveToFirst();
        map.put("TASK_ID", cursor.getLong(0));
        map.put("TASK_NAME", cursor.getString(1));
        map.put("ENABLE_TASK", cursor.getInt(2));
        map.put("ENABLE_CAL_SYNC", cursor.getInt(3));
        map.put("ENABLE_FIXED_SCH", cursor.getInt(4));
        map.put("SUNDAY", cursor.getInt(5));
        map.put("MONDAY", cursor.getInt(6));
        map.put("TUESDAY", cursor.getInt(7));
        map.put("WEDNESDAY", cursor.getInt(8));
        map.put("THURSDAY", cursor.getInt(9));
        map.put("FRIDAY", cursor.getInt(10));
        map.put("SATURDAY", cursor.getInt(11));
        map.put("START_TIME", cursor.getString(12));
        map.put("END_TIME", cursor.getString(13));
        map.put("ENABLE_SILENT_MODE", cursor.getInt(14));
        map.put("ENABLE_VIBRATE_MODE", cursor.getInt(15));
        map.put("VOLUME_LEVEL", cursor.getInt(16));
        return map;

    }

    public void updateSilentModeTaskById(String name, int iEnableTask, int iEnableCalSync,
                                         int iEnableFixedSch, int iSun, int iMon, int iTue, int iWed,
                                         int iThu, int iFri, int iSat, Time tStartTime, Time tEndTime,
                                         int iEnableSilentMode, int iEnableVibrate, int iVolumeLevel) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SILENT_TASK_NAME, name.toUpperCase());
        values.put(DBHelper.COLUMN_SILENT_ENABLE_TASK, iEnableTask+"");
        values.put(DBHelper.COLUMN_SILENT_ENABLE_CAL_SYNC, iEnableCalSync+"");
        values.put(DBHelper.COLUMN_SILENT_ENABLE_FIXED_SCH, iEnableFixedSch+"");
        values.put(DBHelper.COLUMN_SILENT_SUNDAY, iSun+"");
        values.put(DBHelper.COLUMN_SILENT_MONDAY, iMon+"");
        values.put(DBHelper.COLUMN_SILENT_TUESDAY, iTue+"");
        values.put(DBHelper.COLUMN_SILENT_WEDNESDAY, iWed+"");
        values.put(DBHelper.COLUMN_SILENT_THURSDAY, iThu+"");
        values.put(DBHelper.COLUMN_SILENT_FRIDAY, iFri+"");
        values.put(DBHelper.COLUMN_SILENT_SATURDAY, iSat+"");
        values.put(DBHelper.COLUMN_SILENT_START_TIME, tStartTime.toString());
        values.put(DBHelper.COLUMN_SILENT_END_TIME, tEndTime.toString());
        values.put(DBHelper.COLUMN_SILENT_ENABLE_SILENT_MODE, iEnableSilentMode+"");
        values.put(DBHelper.COLUMN_SILENT_ENABLE_VIBRATE_MODE, iEnableVibrate+"");
        values.put(DBHelper.COLUMN_SILENT_VOLUME_LEVEL, iVolumeLevel+"");
        mDatabase.update(DBHelper.TABLE_SILENT_MODE, values, DBHelper.COLUMN_SILENT_TASK_NAME+"='"+name.toUpperCase()+"'", null);
    }


    public void deleteSilentModeTask(SilentModeTask task) throws SQLException  {
        String name = task.getName().toUpperCase();
        Log.i(TAG, "the deleted task has the id: " + name);
        mDatabase.delete(DBHelper.TABLE_SILENT_MODE, DBHelper.COLUMN_SILENT_TASK_NAME + " = '" + name+"'", null);
    }

    public ArrayList<SilentModeTask> getAllSilentModeTasks() {
        ArrayList<SilentModeTask> listTasks = new ArrayList<SilentModeTask>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_SILENT_MODE,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SilentModeTask task = cursorToTask(cursor);
            listTasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listTasks;
    }

    private SilentModeTask cursorToTask(Cursor cursor) {
        SilentModeTask task = new SilentModeTask();
        task.setId(cursor.getLong(0));
        task.setName(cursor.getString(1));
        return task;
    }

}
