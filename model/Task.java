package com.cs683.atshudy.assistmode.model;

import java.io.Serializable;

/**
 * Created by ATshudy on 3/15/2015.
 */
public abstract class Task implements Serializable {

    private static final long serialVersionUID = 1;
    protected long mId;
    protected String mName;

    public abstract String getName() ;
    public abstract long getId();
    public abstract void setId(long mId);
    public abstract void setName(String name);
    public abstract int getParentTab();
}
