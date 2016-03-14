package com.cs683.atshudy.assistmode.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ATshudy on 3/11/2015.
 */
public interface WiFiModeDAO {
    public void open() throws SQLException ;
    public void close() ;
    public WiFiModeTask createWiFiModeTask(String name, int iEnableTask, int iEnableConnectionTimeOut,
                                           int iConnectionTimout, int iEnableLocation, String sLocationName, int iEnableBatteryUsage,
                                           int iMinBatteryUsageLevel) throws SQLException ;

    public Map<String, Object> getWiFiModeTaskByName(String taskName) ;
    public ArrayList<Map<String, Object>> getWiFimodeTaskJobs();
    public void setCompleteState();
    public void updateWiFiModeTaskById(String name, int iEnableTask, int iEnableConnectionTimeOut,
                                         int iConnectionTimout, int iEnableLocation, String sLocationName, int iEnableBatteryUsage,
                                         int iMinBatteryUsageLevel) throws SQLException ;
    public void deleteWiFiModeTask(WiFiModeTask task) throws SQLException ;
    public ArrayList<WiFiModeTask> getAllWiFiModeTaskModeTasks() ;

}
