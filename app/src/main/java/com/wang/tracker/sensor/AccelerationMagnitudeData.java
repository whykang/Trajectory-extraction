package com.wang.tracker.sensor;

import java.util.Date;

public class AccelerationMagnitudeData {
    private static AccelerationMagnitudeData instance;
    private long time;
    private float magnitude;

    public static synchronized AccelerationMagnitudeData getInstance() {
        if (instance != null) return instance;
        instance = new AccelerationMagnitudeData();
        return instance;
    }

    public void update(float newValue) {
        time = new Date().getTime();
        magnitude = newValue;
    }

    public long getTime() {
        return time;
    }

    public float getMagnitude() {
        return magnitude;
    }
}
