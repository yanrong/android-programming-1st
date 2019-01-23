package com.example.dyr.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolve;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isSolve() {
        return mSolve;
    }

    public void setTitle(String Title) {
        mTitle = Title;
    }

    public void setDate(Date Date){
        mDate = Date;
    }

    public void setSolve(boolean Solve) {
        mSolve = Solve;
    }

}

