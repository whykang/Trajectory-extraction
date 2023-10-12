package com.wang.tracker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.wang.tracker.pojo.StepPosition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;


/**
 *
 * @Description TODO
 * @Author WangHongyue
 * @CreateTime 2023-10-12  SDNU 304LAB
 */


public class Utils {

//
//    public static void writeTxt(Context con, String mContent, boolean flag){
//
//        String mStrPath = Environment.getExternalStorageDirectory().getPath() + "/transcript.txt";
//
//        File file = new File(mStrPath);
//        FileOutputStream fos = null;
//        try {
//            if (!file.exists()) {
//                File parentDir = new File(file.getParent());
//                if (!parentDir.exists()) {
//                    parentDir.mkdirs();
//                    file.createNewFile();
//                }
//            }
//            fos = new FileOutputStream(file, flag);
//            fos.write(mContent.getBytes());
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("TAG555555", e.toString());
//        } finally {
//        }
//    }

    public static void write1(Context con,List<StepPosition> pos) throws IOException {

        Long time=new Date().getTime();
        String mStrPath = Environment.getExternalStorageDirectory().getPath() + "/data_"+time+".txt";
        File file = new File( mStrPath);
        if (!file.exists()) {
            File parentDir = new File(file.getParent());
            if (!parentDir.exists()) {
                parentDir.mkdirs();
                file.createNewFile();
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                    "UTF-8"));

            for(int i=0;i<pos.size();i++)
            {
                bw.write(pos.get(i).getTime()+"-"+pos.get(i).getDx() + "-" + pos.get(i).getDy());
                bw.newLine();
            }

            bw.close();
            Log.i("TAG", "写入成功");
            Toast.makeText(con,pos.size()+"个轨迹点数据写入成功,存储地址："+mStrPath,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.err.println("write errors :" + e);
            Toast.makeText(con,"写入失败,错误原因："+e,Toast.LENGTH_SHORT).show();
        }


    }


}
