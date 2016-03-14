package com.cs683.atshudy.assistmode.model;

/**
 * Created by ATshudy on 3/11/2015.
 */
public class WiFiModeTask extends Task {
    public static final String TAG = "WiFiModeTask";

    private long mId;
    private String mName;
    private final int mParentTab = 2;


    public WiFiModeTask() {
    }

    public WiFiModeTask(String name) {
        this.mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getParentTab(){ return this.mParentTab; }
}
