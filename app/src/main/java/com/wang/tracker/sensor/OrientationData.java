package com.wang.tracker.sensor;

import com.wang.tracker.Kalman;
import com.wang.tracker.SensorSingleData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrientationData {
    private static OrientationData instance;
    long time;
    private double Azimuth;
    private double Pitch;
    private double Roll;

    private int pnum;



    private Kalman mKalman=new Kalman();
    private List<Double> angles=new ArrayList<Double>();

    public static synchronized OrientationData getInstance() {
        if (instance != null) return instance;
        instance = new OrientationData();

        return instance;
    }

    public void update(float[] orientations,int a, int num) {

        this.time = new Date().getTime();

        this.pnum=num;

        if(a==1){   //开启卡尔曼滤波

            float[] orientationValues = new float[3];
            orientationValues[0] = (float)Math.toDegrees (orientations[0]);
            orientationValues[1] = (float)Math.toDegrees (orientations[1]);
            orientationValues[2] = (float)Math.toDegrees (orientations[2]);
            SensorSingleData data = mKalman.filter(new SensorSingleData(orientationValues[1],orientationValues[2],orientationValues[0]));
            this.Azimuth =(float)Math.toRadians( data.getAccZ());
           // LogRecorder.getInstance().i("angle111", this.Azimuth+"开启");
        }
        else{

            //LogRecorder.getInstance().i("angle111", this.Azimuth+"关闭");
            this.Azimuth = orientations[0];
        }



        //

        this.angles.add(this.Azimuth);

        //LogRecorder.getInstance().i("angle", this.Azimuth+"-------11");
        this.Pitch = orientations[1];
        this.Roll = orientations[2];
//        LogRecorder.getInstance().i("angle111", this.Azimuth+"99999999999999999-----------+++++++");
//        LogRecorder.getInstance().i("angle222", this.getAzimuth()+"------------------+++++++++++++");
    }

    public long getTime() {
        return time;
    }

    public double getAzimuth() {

      //  return this.Azimuth;

        if(this.angles.size()==0)
        {
            return this.Azimuth;
        }

        return process();

       // return Azimuth;
    }



    public double getPitch() {
        return Pitch;
    }

    public double getRoll() {
        return Roll;
    }


    private double process()
    {
        double result=0.0;
        double sum=0.0;

       // int num=20;
        int num =this.pnum;

        //LogRecorder.getInstance().i("angle", pnum+"pnum_++++++++++____----11");


        if(this.angles.size()<num)
        {
            for(int i=0;i<this.angles.size()-1;i++)
            {
                sum+=this.angles.get(i);
            }
            result=sum/this.angles.size();

        }


        else {

            for(int i=this.angles.size()-1;i>this.angles.size()-1-num;i--)
            {
                sum+=this.angles.get(i);
            }

            result=sum/num;

        }

        if(this.angles.size()>300)
        {
            this.angles.clear();
        }

        return result;
    }

}