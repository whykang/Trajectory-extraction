package com.wang.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description TODO
 * @Author WangHongyue
 * @CreateTime 2023-10-12  SDNU 304LAB
 */

public class Beacon extends AppCompatActivity {

    private TextView text2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        //BleManager.getInstance().init(getApplication());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        text2=findViewById(R.id.text1);

        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(10000);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,"当前设备不支持蓝牙！",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"当前设备支持蓝牙！",Toast.LENGTH_SHORT).show();
            initPermission();
            if (!mBluetoothAdapter.isEnabled()) {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 30);
            }
        }

    }

    private void init()
    {

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描（主线程）
                Log.d("666666666","111111111111");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                // 扫描到一个符合扫描规则的BLE设备（主线程）
                //Log.d("6666222222",bleDevice.getMac());
//                Log.d("6666222222_name",bleDevice.getName());
                Log.d("6666222222",bleDevice.getName());
                Log.d("66rss222","===="+bleDevice.getRssi());
                

            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);

                if(bleDevice.getName()!=null)
                {
                    if(bleDevice.getName().equals("X623"))
                    {
                        Log.d("66rss222",bleDevice.getName()+"=="+bleDevice.getRssi());
                        text2.setText(bleDevice.getName()+"=="+bleDevice.getRssi());
                    }
                }





            }

                @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                // 扫描结束，列出所有扫描到的符合扫描规则的BLE设备（主线程）

                for(int i=0;i<scanResultList.size();i++)
                {

                    Log.d("6666222222",scanResultList.get(i).getName());

                    Log.d("66rss222","===="+scanResultList.get(i).getRssi());

                    Log.d("6666222222_name"," "+scanResultList.get(i).getName());

                }

            }

        });
    }











    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults[1]== PackageManager.PERMISSION_GRANTED) {

                if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    init();
                }

            }else
                Toast.makeText(this, "拒绝了授权,下次还会再弹", Toast.LENGTH_SHORT).show();
            }
        }




    private void initPermission() {
        List<String> mPermissionList = new ArrayList<>();
        // Android 版本大于等于 12 时，申请新的蓝牙权限

            mPermissionList.add(Manifest.permission.BLUETOOTH);
            //根据实际需要申请定位权限
            mPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            mPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityCompat.requestPermissions(this, mPermissionList.toArray(new String[0]), 100);
    }
}