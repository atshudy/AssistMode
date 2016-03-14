package com.cs683.atshudy.assistmode.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ATshudy on 3/11/2015.
 */
public class SilentModeTask extends Task{
    public static final String TAG = "SilentModeTask";

     private final int mParentTab = 1;

    /**
     * An array of task items.
     */
    public static List<Task> ITEMS = new ArrayList<Task>();

    /**
     * A map of tasks items, by ID.
     */
    public static Map<Long, Task> ITEM_MAP = new HashMap<Long, Task>();

    public SilentModeTask() {
    }

    private static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    public SilentModeTask(String name) {
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
