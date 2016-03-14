package com.cs683.atshudy.assistmode.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ATshudy on 3/11/2015.
 */
public interface DrivingModeDAO {
    public Map<String, Object> getDrivingModeTaskByName(String taskName) ;

    public void open() throws SQLException ;
    public void close();
    public DrivingModeTask createDrivingModeTask(String name , int iEnableTask, int iEnableTextMsg, int iEnableHandsFreeMode) throws SQLException ;
    public void updateDrivingModeTaskById(String name , int iEnableTask, int iEnableTextMsg, int iEnableHandsFreeMode) throws SQLException ;
    public void deleteDrivingModeTask(DrivingModeTask task) throws SQLException ;
    public ArrayList<DrivingModeTask> getAllDrivingModeTasks() ;
}
