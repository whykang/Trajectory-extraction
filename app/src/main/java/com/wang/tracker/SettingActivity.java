package com.wang.tracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


/**
 *
 * @Description TODO
 * @Author WangHongyue
 * @CreateTime 2023-10-12  SDNU 304LAB
 */


public class SettingActivity extends AppCompatActivity {

    private CheckBox che1,che2,ly;

    private EditText xx1,yy1,numed,phcled;

    private Button save,savep,saveph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        che1=findViewById(R.id.checkBox1);
        che2=findViewById(R.id.checkBox2);
        ly=findViewById(R.id.lyfz);

        xx1=findViewById(R.id.psx);
        yy1=findViewById(R.id.psy);
        numed=findViewById(R.id.pnum);
        phcled=findViewById(R.id.phcl);



        save=findViewById(R.id.save1);
        savep=findViewById(R.id.save2);

        saveph=findViewById(R.id.save3);

        SharedPreferences dataBase =this.getSharedPreferences("Sha", this.MODE_PRIVATE);
        SharedPreferences.Editor edit = dataBase.edit();

        String name = dataBase.getString("ch1", "0");

        String name2 = dataBase.getString("ch2", "0");
        String name3 = dataBase.getString("lyfz", "0");

        int xnum = dataBase.getInt("x", 0);

        int ynum = dataBase.getInt("y", 0);

        int edpnum=dataBase.getInt("pnum", 15);

        int phclnum=dataBase.getInt("phcl", 3);

        numed.setText(""+edpnum);

        phcled.setText(""+phclnum);

        xx1.setText(""+xnum);
        yy1.setText(""+ynum);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int xnum1 = Integer.parseInt(xx1.getText().toString());
                int ynum1 = Integer.parseInt(yy1.getText().toString());
                edit.putInt("x",xnum1) ;
                edit.putInt("y",ynum1) ;
                edit.apply();
                Toast.makeText(getApplicationContext(),"已保存，重启软件生效",Toast.LENGTH_SHORT).show();

            }
        });


        savep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int num1 = Integer.parseInt(numed.getText().toString());
                edit.putInt("pnum",num1);
                edit.apply();
                Toast.makeText(getApplicationContext(),"已保存",Toast.LENGTH_SHORT).show();

            }
        });

        saveph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int num1 = Integer.parseInt(phcled.getText().toString());
                edit.putInt("phcl",num1);
                edit.apply();
                Toast.makeText(getApplicationContext(),"已保存",Toast.LENGTH_SHORT).show();

            }
        });

        if(name.equals("1"))
        {
            che1.setChecked(true);
        }

        if(name2.equals("1"))
        {
            che2.setChecked(true);
        }
        if(name3.equals("1"))
        {
            ly.setChecked(true);
        }

        che1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // arg1代表是否选中
                Log.i("tag", arg1 + "");

                if(arg1)
                {
                    edit.putString("ch1", "1") ;
                    edit.apply();
                    Toast.makeText(getApplicationContext(),"已开启卡尔曼滤波",Toast.LENGTH_SHORT).show();
                }
                else{
                    edit.putString("ch1", "0") ;
                    edit.apply();
                    Toast.makeText(getApplicationContext(),"已关闭卡尔曼滤波",Toast.LENGTH_SHORT).show();
                }


            }
        });


        ly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {
                    edit.putString("lyfz", "1") ;
                    edit.apply();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("提示：");
                    builder.setMessage("请确保蓝牙开启并授予位置权限");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setCancelable(true);            //点击对话框以外的区域是否让对话框消失

                    //设置正面按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    //设置反面按钮
                    builder.setNegativeButton("进入蓝牙调试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent =new Intent(getApplicationContext(),Beacon.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();      //创建AlertDialog对象
                    //对话框显示的监听事件
                    dialog.show();



                }
                else{
                    edit.putString("lyfz", "0") ;
                    edit.apply();
                    //Toast.makeText(getApplicationContext(),"已关闭卡尔曼滤波",Toast.LENGTH_SHORT).show();
                }

//        //设置中立按钮
//        builder.setNeutralButton("保密", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "你选择了中立", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });



            }
        });



        che2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // arg1代表是否选中
                Log.i("tag", arg1 + "");

                if(arg1)
                {
                    edit.putString("ch2", "1") ;
                    edit.apply();
                    Toast.makeText(getApplicationContext(),"已加载场景",Toast.LENGTH_SHORT).show();
                }
                else{
                    edit.putString("ch2", "0") ;
                    edit.apply();
                    Toast.makeText(getApplicationContext(),"已关闭场景",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


}