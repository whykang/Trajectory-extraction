package com.wang.tracker.sensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.wang.tracker.tool.WaveRecorder;

public class StepEventListener implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor stepDetectorSensor, rotationVectorSensor, linearAccelerationSensor, mageSensor,accelerationSensor;
    private StepEventHandler stepEventHandler;

    private float[] rotationMatrixFromVector = new float[16];
    private float[] rotationMatrix = new float[16];
    private float[] orientationVals = new float[3];


    float accValues[] = new float[3];
    //地磁传感器数据
    float magValues[] = new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    float r[] = new float[9];
    //模拟方向传感器的数据（原始数据为弧度）
    float values[] = new float[3];
    private Context context;





    public StepEventListener(SensorManager sensorManager, StepEventHandler stepEventHandler, Context con) {
        this.sensorManager = sensorManager;
        this.stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        this.rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.mageSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.accelerationSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepEventHandler = stepEventHandler;
        context=con;

    }

    public void start() {
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);//步数检测
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);//旋转矢量传感器
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);//线性加速度-无重力
        sensorManager.registerListener(this, mageSensor, SensorManager.SENSOR_DELAY_NORMAL);//磁场
        sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);//加速度传感器
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    /**
     * Update the corresponding information to the data object for different sensors
     * */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ROTATION_VECTOR:
//                SensorManager.getRotationMatrixFromVector(rotationMatrixFromVector, sensorEvent.values);
//                SensorManager.remapCoordinateSystem(rotationMatrixFromVector,
//                        SensorManager.AXIS_X, SensorManager.AXIS_Z,
//                        rotationMatrix);
//                SensorManager.getOrientation(rotationMatrix, orientationVals);
               // OrientationData.getInstance().update(orientationVals);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                float linearAcceleration = (float) Math.sqrt(
                        sensorEvent.values[0] * sensorEvent.values[0]
                                + sensorEvent.values[1] * sensorEvent.values[1]
                                + sensorEvent.values[2] * sensorEvent.values[2]);
                AccelerationMagnitudeData.getInstance().update(linearAcceleration);
                WaveRecorder.getInstance().update(linearAcceleration);
                //mAcceleValues = sensorEvent.values;
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                stepEventHandler.onStep();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magValues = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accValues = sensorEvent.values.clone();
                calculateOrientation();
                break;
        }




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }


    public void calculateOrientation(){

        SensorManager.getRotationMatrix(r, null, accValues, magValues);

        SharedPreferences dataBase =context. getSharedPreferences("Sha", context.MODE_PRIVATE);
        SharedPreferences.Editor edit = dataBase.edit();

        /**
         * R：旋转数组
         * values：模拟方向传感器的数据
         */

        int a=0;
        int num=20;

        String name = dataBase.getString("ch1", "0");

        num = dataBase.getInt("pnum", 15);

        if(name.equals("1"))
        {
            a=1;

        }

        SensorManager.getOrientation(r, values);

        OrientationData.getInstance().update(values,a,num);

        stepEventHandler.onStep1();

        //将弧度转化为角度后输出
//        float azimuth = (float) Math.toDegrees(values[0]);
//        float pitch = (float) Math.toDegrees(values[1]);
//        float roll = (float) Math.toDegrees(values[2]);

        //LogRecorder.getInstance().i("angle", azimuth+"   "+"______________");
//        if(azimuth >= 0 && azimuth <= 90){
//            LogRecorder.getInstance().i("angle", azimuth+"   "+"正指向东北方向");
//            //textViewx.setText(azimuth+"   "+"正指向东北方向");
//        }else{
//            LogRecorder.getInstance().i("angle", azimuth+"");
//            //textViewx.setText(azimuth+"");
//        }
//        if(pitch <=0 && pitch >= -90){
//            LogRecorder.getInstance().i("angle", pitch+"   "+"手机顶部正往上抬起");
//            //textViewy.setText(pitch+"   "+"手机顶部正往上抬起");
//        }else{
//            LogRecorder.getInstance().i("angle", pitch+"");
//           // textViewy.setText(pitch+"");
//        }
//        if(roll <=0 && pitch >= -90){
//            LogRecorder.getInstance().i("angle", roll+"   "+"手机右侧正往上抬起");
//           //textViewz.setText(roll+"   "+"手机右侧正往上抬起");
//        }else{
//            LogRecorder.getInstance().i("angle", roll+"");
//           // textViewz.setText(roll+"");
//        }


    }

}
