package com.wang.tracker.pojo;

public class StepPosition {
    private Long time;
    private Float dx;
    private Float dy;

    public StepPosition(Long time, Float dx, Float dy) {
        this.time = time;
        this.dx = dx;
        this.dy = dy;
    }

    public Float getDx() {
        return dx;
    }

    public void setDx(float x) {

        this.dx=x;
      //  return dx;
    }

    public void setDy(float y) {

        this.dy=y;
        //  return dx;
    }

    public Float getDy() {
        return dy;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "StepPosition{" +
                "time=" + time +
                ", dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}