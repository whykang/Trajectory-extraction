package com.wang.tracker.log;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class LogRecorder {
    Context context = null;
    public static LogRecorder instance;

    static String STORAGE_PATH = "";

    public LogRecorder(Context context) {
        this.context = context;
        STORAGE_PATH = context.getExternalFilesDir("log").toString();
    }

    public static synchronized LogRecorder getInstance() {
        if (instance != null) return instance;
        return null;
    }

    public static synchronized LogRecorder getInstance(Context context) {
        if (instance != null) return instance;
        instance = new LogRecorder(context);
        return instance;
    }


    public int e(String TAG, String msg) {
        writeFile(String.valueOf(new Date().getTime()) + '-' + TAG, msg);
        Log.e(TAG, STORAGE_PATH);
        return 0;
    }


    public void i(String TAG, String msg) {
        writeFile(String.valueOf(new Date().getTime()) + '-' + TAG, msg);
        Log.i(TAG, msg);
    }

    public void writeFile(String fileName, String content) {
        File path = new File(STORAGE_PATH);
        if (!path.exists()) path.mkdir();
        File file = new File(STORAGE_PATH + File.separator + fileName);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                boolean hasFile = file.createNewFile();
                fos = new FileOutputStream(file);
            } else {
                fos = new FileOutputStream(file, true);
            }
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(content);
            osw.write("\r\n");
        } catch (Exception e) {
        } finally {
            // 关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
