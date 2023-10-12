package com.wang.tracker.tool;

public class WaveRecorder {
    private static WaveRecorder instance;
    float maxWave = 0;
    float minWave = 0x7fffffff;

    public synchronized static WaveRecorder getInstance() {
        if (instance != null) return instance;
        instance = new WaveRecorder();
        return instance;
    }

    public boolean update(float wave) {
        if (wave > maxWave) {
            maxWave = wave;
            return true;
        }
        if (wave < minWave) {
            minWave = wave;
            return true;
        }
        return false;
    }

    public float calculate() {
        if(maxWave == 0 || minWave == 0x7fffffff) {
            reset();
            return 0;
        }
        return maxWave - minWave;
    }

    public void reset() {
        minWave = 0x7fffffff;
        maxWave = 0;
    }

    public float calculateAndReset() {
        if(maxWave == 0 || minWave == 0x7fffffff) {
            reset();
            return 0;
        }
        return maxWave - minWave;
    }
}
