package com.wang.tracker.sensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.wang.tracker.pojo.StepPosition;
import com.wang.tracker.tool.WaveRecorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Handling step information
 * */
public class StepEventHandler {
    private final Handler activityHandler;

    private final Handler activityHandler1;

   private List<Float> angles=new ArrayList<Float>();
   private Context context;

    public StepEventHandler(Handler activityHandler, Handler activityHandler1, Context con) {


        this.activityHandler = activityHandler;
        this.activityHandler1 = activityHandler1;
        this.context=con;

    }

    /**
     * Calculate the direction and distance of this step
     * Of course, the numbers are relative
     * */
    public void onStep() {
        float angle = (float) OrientationData.getInstance().getAzimuth();

        angles.add(angle);


        float ang=angle;

        SharedPreferences dataBase =context. getSharedPreferences("Sha", context.MODE_PRIVATE);
        SharedPreferences.Editor edit = dataBase.edit();
        int phclnum=dataBase.getInt("phcl", 3);

        if(angles.size()>200)
        {
            angles.clear();
        }

        if(angles.size()>phclnum)
        {
           ang=pro(angles,phclnum);
        }


        float distance = getStepSize();
        //StepPosition stepPosition = new StepPosition(new Date().getTime(), (float) -Math.cos(angle) * distance, (float) -Math.sin(angle) * distance);

        StepPosition stepPosition = new StepPosition(new Date().getTime(), (float) Math.sin(ang) * distance, (float) Math.cos(ang) * distance);
        Message message = new Message();
        Message message1 = new Message();
        message.obj = stepPosition;
        message.arg1= (int) distance;
//        LogRecorder.getInstance().i("distance", distance+"======");
//        LogRecorder.getInstance().i("angle", angle+"----11");
//        LogRecorder.getInstance().i("stepPosition", stepPosition.toString());
        activityHandler.sendMessage(message);


    }


    public void onStep1() {
        float angle = (float) OrientationData.getInstance().getAzimuth();
        Message message1 = new Message();
        int degree = (int) Math.toDegrees(angle);//旋转角度
        if (degree < 0) {
            degree += 360;
        }

        message1.arg1 =degree;
        activityHandler1.sendMessage(message1);

    }

    private float pro(List<Float> angs,int phnum)
    {
        float result=0;
        float sum=0;

       // LogRecorder.getInstance().i("angle", phnum+"phnum_______----11");

        for (int i=angs.size()-1;i>(angs.size()-1-phnum);i--){

            sum+=angs.get(i);

        }

        result=sum/phnum;

        return result;

    }

    /**
     * Calculate this step size according to the formula
     * */
    private float getStepSize() {
        float a = (float) Math.pow(WaveRecorder.getInstance().calculateAndReset(), 0.25);
        float c = 0.5f;
        return a * c;
    }
}
