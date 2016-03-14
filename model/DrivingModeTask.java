package com.cs683.atshudy.assistmode.model;

/**
 * Created by ATshudy on 3/11/2015.
 */
public class DrivingModeTask extends Task {
    public static final String TAG = "DrivingModeTask";

    private long mId;
    private String mName;
    private final int mParentTab = 3;


    public DrivingModeTask() {
    }

    public DrivingModeTask(String name) {
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

    public int getParentTab(){return this.mParentTab;}
}
