package com.example.quakereport;

public class Quake {
    private String mLocation;
    private double mMagnitude;
    private long mTimeInMilliSec;
    private String mUrl;
    public Quake(double mag,String loc,long date, String url)
    {
        mLocation=loc;
        mMagnitude=mag;
        mTimeInMilliSec=date;
        mUrl=url;
    }

    public String getmLocation() {
        return mLocation;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public long getmTimeInMilliSec() {
        return mTimeInMilliSec;
    }

    public String getUrl() {
        return mUrl;
    }
}
