package com.cs683.atshudy.assistmode.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ATshudy on 3/11/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";

    // columns of the silent mode table
    public static final String TABLE_SILENT_MODE = "silent_mode";
    public static final String COLUMN_SILENT_TASK_ID = "silent_task_id";
    public static final String COLUMN_SILENT_TASK_NAME = "silent_task_name";
    public static final String COLUMN_SILENT_ENABLE_TASK = "enable_task";
    public static final String COLUMN_SILENT_ENABLE_CAL_SYNC = "enable_cal_sync";
    public static final String COLUMN_SILENT_ENABLE_FIXED_SCH = "enable_fixed_sch";
    public static final String COLUMN_SILENT_SUNDAY = "sunday";
    public static final String COLUMN_SILENT_MONDAY = "monday";
    public static final String COLUMN_SILENT_TUESDAY = "tuesday";
    public static final String COLUMN_SILENT_WEDNESDAY = "wednesday";
    public static final String COLUMN_SILENT_THURSDAY = "thursday";
    public static final String COLUMN_SILENT_FRIDAY = "friday";
    public static final String COLUMN_SILENT_SATURDAY = "saturday";
    public static final String COLUMN_SILENT_START_TIME = "start_time";
    public static final String COLUMN_SILENT_END_TIME = "end_time";
    public static final String COLUMN_SILENT_ENABLE_SILENT_MODE = "enable_silent_mode";
    public static final String COLUMN_SILENT_ENABLE_VIBRATE_MODE = "enable_vibrate_mode";
    public static final String COLUMN_SILENT_VOLUME_LEVEL = "volume_level";

    // columns of the WiFi mode table
    public static final String TABLE_WIFI_MODE = "wifi_mode";
    public static final String COLUMN_WIFI_TASK_ID = "wifi_task_id";
    public static final String COLUMN_WIFI_TASK_NAME = "wifi_task_name";
    public static final String COLUMN_WIFI_ENABLE_TASK = "enable_task";
    public static final String COLUMN_WIFI_ENABLE_CONN_TIMEOUT = "enable_connection_timout";
    public static final String COLUMN_WIFI_CONN_TIMEOUT = "connection_timout";
    public static final String COLUMN_WIFI_ENABLE_LOCATION = "enable_location";
    public static final String COLUMN_WIFI_LOCATION = "location";
    public static final String COLUMN_WIFI_ENABLE_BATTERY_USEAGE = "enable_battery_usage";
    public static final String COLUMN_WIFI_MIN_BATTERY_USEAGE = "min_battary_usage_level";

    // columns of the Driving mode table
    public static final String TABLE_DRIVING_MODE = "driving_mode";
    public static final String COLUMN_DRIVING_TASK_ID = "driving_task_id";
    public static final String COLUMN_DRIVING_TASK_NAME = "driving_task_name";
    public static final String COLUMN_DRIVING_ENABLE_TASK = "enable_task";
    public static final String COLUMN_DRIVING_ENABLE_SEND_TEXT = "enable_send_text_msg";
    public static final String COLUMN_DRIVING_ENABLE_HANDS_FREE_MODE = "enable_hands_free_mode";

    private static final String DATABASE_NAME = "assistmode.db";
    private static final int DATABASE_VERSION = 5;

    // SQL statement of the silent mode table creation
    private static final String SQL_CREATE_TABLE_SILENT_MODE = "CREATE TABLE " + TABLE_SILENT_MODE + "("
            + COLUMN_SILENT_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SILENT_TASK_NAME + " TEXT NOT NULL, "
            + COLUMN_SILENT_ENABLE_TASK  + " INTEGER enable_task, "
            + COLUMN_SILENT_ENABLE_CAL_SYNC  + " INTEGER enable_cal_sync, "
            + COLUMN_SILENT_ENABLE_FIXED_SCH  + " INTEGER enable_fixed_sch, "
            + COLUMN_SILENT_SUNDAY  + " INTEGER sunday, "
            + COLUMN_SILENT_MONDAY  + " INTEGER monday, "
            + COLUMN_SILENT_TUESDAY  + " INTEGER tuesday, "
            + COLUMN_SILENT_WEDNESDAY  + " INTEGER wednesday, "
            + COLUMN_SILENT_THURSDAY  + " INTEGER thursday, "
            + COLUMN_SILENT_FRIDAY  + " INTEGER friday, "
            + COLUMN_SILENT_SATURDAY  + " INTEGER saturday, "
            + COLUMN_SILENT_START_TIME  + " TIME hour_start_time, "
            + COLUMN_SILENT_END_TIME  + " TIME hour_end_time, "
            + COLUMN_SILENT_ENABLE_SILENT_MODE  + " INTEGER enable_silent_mode, "
            + COLUMN_SILENT_ENABLE_VIBRATE_MODE  + " INTEGER enable_vibrate_mode, "
            + COLUMN_SILENT_VOLUME_LEVEL  + " INTEGER volume_level"
            +");";

    // SQL statement of the Wifi mode table table creation
    private static final String SQL_CREATE_TABLE_WIFI_MODE = "CREATE TABLE " + TABLE_WIFI_MODE + "("
            + COLUMN_WIFI_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_WIFI_TASK_NAME + " TEXT NOT NULL, "
            + COLUMN_WIFI_ENABLE_TASK + " INTEGER enable_task, "
            + COLUMN_WIFI_ENABLE_CONN_TIMEOUT + " INTEGER enable_connection_timout, "
            + COLUMN_WIFI_CONN_TIMEOUT + " INTEGER connection_timout, "
            + COLUMN_WIFI_ENABLE_LOCATION + " INTEGER enable_location, "
            + COLUMN_WIFI_LOCATION + " TEXT location, "
            + COLUMN_WIFI_ENABLE_BATTERY_USEAGE + " INTEGER enable_battery_usage, "
            + COLUMN_WIFI_MIN_BATTERY_USEAGE + " INTEGER min_battary_usage_level"
            +");";
    // SQL statement of the Driving mode table table creation
    private static final String SQL_CREATE_TABLE_DRIVING_MODE = "CREATE TABLE " + TABLE_DRIVING_MODE + "("
            + COLUMN_DRIVING_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DRIVING_TASK_NAME + " TEXT NOT NULL, "
            + COLUMN_DRIVING_ENABLE_TASK + " INTEGER enable_task, "
            + COLUMN_DRIVING_ENABLE_SEND_TEXT + " INTEGER enable_send_text_msg, "
            + COLUMN_DRIVING_ENABLE_HANDS_FREE_MODE + " INTEGER enable_hands_free_mode"
            +");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_SILENT_MODE);
        database.execSQL(SQL_CREATE_TABLE_WIFI_MODE);
        database.execSQL(SQL_CREATE_TABLE_DRIVING_MODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading the database from version " + oldVersion + " to " + newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SILENT_MODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI_MODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVING_MODE);

        // recreate the tables
        onCreate(db);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
}
