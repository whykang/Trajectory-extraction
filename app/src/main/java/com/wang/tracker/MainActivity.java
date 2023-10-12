package com.wang.tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.wang.tracker.databinding.ActivityFullscreenBinding;

import com.wang.tracker.log.LogRecorder;
import com.wang.tracker.pojo.StepPosition;
import com.wang.tracker.sensor.StepEventHandler;
import com.wang.tracker.sensor.StepEventListener;
import com.wang.tracker.tool.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *
 * @Description V2.0
 * @Author WangHongyue
 * @CreateTime 2023-10-12 15:32
 */


public class MainActivity extends AppCompatActivity {
    private static final int UI_ANIMATION_DELAY = 300;
    private static final int PERMISSION_REQUEST_CODE =100;

    private float  x=0,y=0;

    private int x623=0;

    private final Handler mHideHandler = new Handler();
    private TextView text;
    private Button cle,abu,save,set;

    private Handler positionHandler,positionHandler1;

    private int xnum=0;
    private int ynum=0;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
//                mCanvas.getWindowInsetsController().hide(
//                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mCanvas.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };

    private View mControlsView;
    private CanvasView mCanvas;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;
    private void clearView() {

        tipDialog();
    }


    /**
     * 提示对话框
     */
    public void tipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示：");
        builder.setMessage("重置后将恢复初始界面，且轨迹数据将不可保存！");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定重置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCanvas.clearPoints(xnum,ynum);

                if(positions!=null && positions.size()>0){
                    positions.clear();
                }
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.show();                              //显示对话框
    }

//    private void requestPermission() {
//        // checkSelfPermission() 检测权限
//        String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED) {
//            //TODO 申请存储权限
//            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
//                    PERMISSION_REQUEST_CODE);
//        }
//
//        else{
//           // Utils.writeTxt(getApplicationContext(),"44444444444",true);
//            Toast.makeText(getApplicationContext(),"已授予权限",Toast.LENGTH_SHORT).show();
//        }
//    }


    private ActivityFullscreenBinding binding;
    private StepEventHandler stepEventHandler;
    private StepEventListener stepEventListener;

    private List<StepPosition> positions=new ArrayList<StepPosition>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Permission permission = new Permission();
        permission.checkPermissions(this);
        //requestPermission();
        BleManager.getInstance().init(getApplication());
        abu=findViewById(R.id.about_button);
        cle=findViewById(R.id.dummy_button);
        save=findViewById(R.id.save);
        set=findViewById(R.id.set_button);
        text=findViewById(R.id.orient_text);



        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mCanvas = binding.canvasView;


        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(10000);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        UUID[] serviceUuids= new UUID[1];
        serviceUuids[0]=UUID.fromString("fda50693-a4e2-4fb1-afcf-c6eb07647825");

        String names[]=new String[1];
        names[0]="X623";
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                // .setServiceUuids(serviceUuids)
                .setDeviceName(true, names)
                // .setDeviceMac(mac)
                .setAutoConnect(false)
                .setScanTimeOut(2000)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);




        SharedPreferences dataBase =this.getSharedPreferences("Sha", this.MODE_PRIVATE);
        SharedPreferences.Editor  edit = dataBase.edit();
        
        // Inject context to LogRecorder
        LogRecorder.getInstance(getApplicationContext());

        // Pass StepPosition to CanvasView
        positionHandler = new Handler(message -> {

            StepPosition position = (StepPosition) message.obj;

            String name3 = dataBase.getString("lyfz", "0");

            if(name3.equals("0"))
            {
                positions.add(position);
                mCanvas.addPoint(position.getDx(), position.getDy());
                x+=position.getDx();
                y+=position.getDy();
            }

            else {

                BleManager.getInstance().scan(new BleScanCallback() {
                    @Override
                    public void onScanStarted(boolean success) {
                        // 开始扫描（主线程）
                    }

                    @Override
                    public void onScanning(BleDevice bleDevice) {

                        if(x623!=0)
                        {
                            positions.add(position);
                            mCanvas.addPoint(position.getDx(), position.getDy());//绘制新轨迹
                            x+=position.getDx();
                            y+=position.getDy();
                            return;
                        }
                        float xx=60; //定义蓝牙设备X轴参考点
                        float yy=60; //Y轴参考点

                        //扫描到蓝牙，进行动态修正，X623为蓝牙名称，如存在多个蓝牙设备，清自行修改
                        if(bleDevice.getName().equals("X623"))
                        {
                            xx=60;
                            yy=60;
                            position.setDx(xx-x);//根据阈值修正误差，此处阈值为0
                            position.setDy(yy-y);
                            positions.add(position);
                            mCanvas.addPoint(position.getDx(), position.getDy()); //更新坐标
                            x=xx;
                            y=yy;
                            x623+=1;
                        }

                    }
//
//                    @Override
//                    public void onLeScan(BleDevice bleDevice) {
//                        super.onLeScan(bleDevice);
//
//                        if (bleDevice.getName() != null) {
//                            if (bleDevice.getName().equals("X623")) {
//
//                            }
//                        }
//                    }

                    @Override
                    public void onScanFinished(List<BleDevice> scanResultList) {
                        // 扫描结束，列出所有扫描到的符合扫描规则的BLE设备（主线程）
                        if(scanResultList.size()==0){

                                positions.add(position);
                                mCanvas.addPoint(position.getDx(), position.getDy());
                                x+=position.getDx();
                                y+=position.getDy();

                        }

//                        for (int i = 0; i < scanResultList.size(); i++) {
//
//                            Log.d("6666222222", scanResultList.get(i).getName());
//
//                            Log.d("66rss222", "====" + scanResultList.get(i).getRssi());
//
//
//                        }

                    }

                });
            }

            return true;





        });

        //刷新箭头
        positionHandler1 = new Handler(message -> {

            int ang= message.arg1;
            mCanvas.autoDrawArrow(ang);

            text.setText("当前方向: "+ang);

            return true;
        });

        // start a thread to listen and handle the information of sensor
//        new Thread(() -> {
//            stepEventHandler = new StepEventHandler(positionHandler,positionHandler1,getApplicationContext());
//            stepEventListener = new StepEventListener((SensorManager) MainActivity.this.getSystemService(Context.SENSOR_SERVICE), stepEventHandler,getApplicationContext());
//            stepEventListener.start();
//        }).start();

        xnum = dataBase.getInt("x", 0);

        ynum = dataBase.getInt("y", 0);

        mCanvas.addPoint(xnum,ynum);
        x=xnum;
        y=ynum;

       // mCanvas.setOnClickListener(view -> toggle());

        //mCanvas.drawline(0,0, 1,0);

        //binding.dummyButton.setOnTouchListener(mDelayHideTouchListener);

        cle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearView();
                //清除重置
            }
        });

        abu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(MainActivity.this,AboutActivity.class);

                startActivity(intent);

                //关于页面
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,SettingActivity.class);

                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(positions!=null && positions.size()>0){
                        Utils.write1(getApplicationContext(),positions);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "数据点不存在", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                   // Log.d()
                    //Log.d("6666222222","+"+e.toString());
                    throw new RuntimeException(e);

                }
            }
        });



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {

            //用户点击了“确定” == grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               // Utils.writeTxt(getApplicationContext(),"44444444444",true);
                Toast.makeText(getApplicationContext(),"已授予权限",Toast.LENGTH_SHORT).show();

                new Thread(() -> {
                    stepEventHandler = new StepEventHandler(positionHandler,positionHandler1,getApplicationContext());
                    stepEventListener = new StepEventListener((SensorManager) MainActivity.this.getSystemService(Context.SENSOR_SERVICE), stepEventHandler,getApplicationContext());
                    stepEventListener.start();
                }).start();

               // Log.i("TAG", "onRequestPermissionsResult granted");
            } else {
                Log.i("TAG", "onRequestPermissionsResult denied");
                // TODO 用户拒绝权限申请，则弹出警示框
                Toast.makeText(getApplicationContext(),"未授予必要权限，运行可能异常",Toast.LENGTH_SHORT).show();
                //showWaringDialog();
            }
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
       // delayedHide(100);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

}