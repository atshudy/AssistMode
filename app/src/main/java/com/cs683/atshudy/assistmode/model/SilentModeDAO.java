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
public interface SilentModeDAO {

    public void open() throws SQLException ;
    public void close();
    public SilentModeTask createSilentModeTask(String name, int iEnableTask, int iEnableCalSync,
                                               int iEnableFixedSch, int iSun, int iMon, int iTue, int iWed,
                                               int iThu, int iFri, int iSat, Time tStartTime, Time tEndTime,
                                               int iEnableSilentMode, int iEnableVibrate, int iVolumeLevel) throws SQLException ;

    public Map<String, Object> getSilentModeTaskByName(String taskName) ;
    public ArrayList<Map<String, Object>> getSilentmodeTaskJobs();
    public void setCompleteState();
    public void updateSilentModeTaskById(String name, int iEnableTask, int iEnableCalSync,
                                         int iEnableFixedSch, int iSun, int iMon, int iTue, int iWed,
                                         int iThu, int iFri, int iSat, Time tStartTime, Time tEndTime,
                                         int iEnableSilentMode, int iEnableVibrate, int iVolumeLevel) throws SQLException ;
    public void deleteSilentModeTask(SilentModeTask task) throws SQLException  ;
    public ArrayList<SilentModeTask> getAllSilentModeTasks() ;

}
