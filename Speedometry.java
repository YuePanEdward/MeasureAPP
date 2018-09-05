package com.test.measureapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Speedometry extends AppCompatActivity {

    private SensorManager sensorManager;

    private TextView Vx;
    private TextView Vy;
    private TextView Vz;
    private TextView V;

    float vz=0,vx=0,vy=0,rz=0,ry=0,rx=0,sx=0,sy=0,sz=0,s=0,v=0,vxc=0,vyc=0,vzc=0;
    private float[] mGData = new float[3];
    private float[] mGData2 = new float[3];
    private float[] mMData = new float[3];
    private float[] mR = new float[16];
    private float[] mI = new float[16];
    private float[] mOrientation = new float[3];
    private float[] gravity=new float[3];
    List<Float> acxList =new ArrayList<Float>();
    List<Float> acyList =new ArrayList<Float>();
    List<Float> aczList =new ArrayList<Float>();
    List<Float> acsxList =new ArrayList<Float>();
    List<Float> acsyList =new ArrayList<Float>();
    List<Float> acszList =new ArrayList<Float>();
    List<Float> vxList =new ArrayList<Float>();
    List<Float> vyList =new ArrayList<Float>();
    List<Float> vzList =new ArrayList<Float>();
    List<Float> vxcList =new ArrayList<Float>();
    List<Float> vycList =new ArrayList<Float>();
    List<Float> vzcList =new ArrayList<Float>();
    int span=100;//平滑窗口大小  0.2s
    int k=0;
    int ks=-span; //平滑后时序
    int kb,ke;
    int flag=0;
    float a0x,dax,a0y,day,a0z,daz;
    float dt=(float)0.001; //1000Hz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Vx= (TextView) findViewById(R.id.textView25);
        Vy= (TextView) findViewById(R.id.textView27);
        Vz= (TextView) findViewById(R.id.textView26);

        V= (TextView) findViewById(R.id.textView10);
        sensorManager = (SensorManager) getSystemService
                (Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.
                TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        Sensor linearaccelerometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravitySensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_GRAVITY);
       /* Sensor accelerometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_LINEAR_ACCELERATION);*/
        sensorManager.registerListener(listener, magneticSensor,
                1000); //1000 Hz
        sensorManager.registerListener(listener, accelerometerSensor,
                1000);
        sensorManager.registerListener(listener,  linearaccelerometerSensor,
                1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }
    @Override
    protected void onStop() {
        // unregister listener
        sensorManager.unregisterListener(listener);
        super.onStop();
    }
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

     public void Calibrate(View v) {
         if (flag==0) {
             flag = 1;
             Toast.makeText(Speedometry.this, "Move a bit and then click it agian.",
                     Toast.LENGTH_LONG).show();
         }
         else if (flag==5) {
             flag = 2;
             Toast.makeText(Speedometry.this, "Calibration Completed.",
                     Toast.LENGTH_LONG).show();
         }
     }

    public void Start(View v)
    {
        flag = 3;
        //Vx.setText("Flag=3");
        //Toast.makeText(Speedometry.this, "Start measurment.",
          //      Toast.LENGTH_LONG).show();
    }


    private SensorEventListener listener = new SensorEventListener() {
        /*float[] accelerometerValues = new float[3];
          float[] magneticValues = new float[3];*/

        public void onSensorChanged(SensorEvent event) {

            int type = event.sensor.getType();
            //if (type == Sensor.TYPE_LINEAR_ACCELERATION)ACCLEREATEMETER
            if (type == Sensor.TYPE_ACCELEROMETER) {
                mGData=event.values;
            }
            if (type == Sensor.TYPE_LINEAR_ACCELERATION) {
                mGData2 = event.values;
            }
            if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMData=event.values;
            }
            if (type == Sensor.TYPE_GRAVITY) {
                gravity=event.values;
            }
            /*else {
                    return;
                }*/
                /*for (int i = 0; i < 3; i++) {
                    data[i] = event.values[i];
                }*/
            /*float xValue = sensorEvent.values[0];// Acceleration minus Gx on the x-axis
            float yValue = sensorEvent.values[1];//Acceleration minus Gy on the y-axis
            float zValue = sensorEvent.values[2];//Acceleration minus Gz on the z-axis
            mTvInfo.setText("x轴： "+xValue+"  y轴： "+yValue+"  z轴： "+zValue);
            if(xValue > mGravity) {
                mTvInfo.append("\n重力指向设备左边");
            } else if(xValue < -mGravity) {
                mTvInfo.append("\n重力指向设备右边");
            } else if(yValue > mGravity) {
                mTvInfo.append("\n重力指向设备下边");
            } else if(yValue < -mGravity) {
                mTvInfo.append("\n重力指向设备上边");
            } else if(zValue > mGravity) {
                mTvInfo.append("\n屏幕朝上");
            } else if(zValue < -mGravity) {
                mTvInfo.append("\n屏幕朝下");
            }*/
            // 根据设备传输过来的向量数据计算倾斜矩阵mR以及旋转矩阵mI
            SensorManager.getRotationMatrix(mR, mI, mGData, mMData);
            // 根据旋转矩阵mR计算出设备的方向
            SensorManager.getOrientation(mR, mOrientation);
            float azimuth = (float) Math.toDegrees(mOrientation[0]);
            //手机绕x轴旋转的度数
            float pitch = (float) Math.toDegrees(mOrientation[1]);
            //手机绕y轴旋转的度数
            float roll = (float) Math.toDegrees(mOrientation[2]);

            /* // 判断当前是加速度传感器还是地磁传感器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
// 注意赋值时要调用clone()方法
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
// 注意赋值时要调用clone()方法
                magneticValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            double g = 9.8;
            SensorManager.getRotationMatrix(R, null, accelerometerValues,
                    magneticValues);
            // SensorManager.getOrientation(R, values);
            //Log.d("MainActivity", "value[0] is " + Math.toDegrees(values[0]));*/
            double g = 9.8;
            float xa = mGData2[0];
            float ya = mGData2[1];
            float za = mGData2[2];//用线性加速度传感器（还要作标定改正）,或者滤除重力 [linear accerlation 应该是已经滤除g的]
            // alpha is calculated as t / (t + dT)//重力滤波
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate
     /*       float alpha =1;
            alpha-=0.2;
            float xa1,ya1,za1;

          gravity[0] = alpha * gravity[0] + (1 - alpha) * xa;
          gravity[1] = alpha * gravity[1] + (1 - alpha) * ya;
          gravity[2] = alpha * gravity[2] + (1 - alpha) * za;

         /*xa1 = xa - gravity[0];
            ya1= ya - gravity[1];
          za1 = za- gravity[2];*/
            float xag=xa*mR[0]+ya*mR[4]+za*mR[8];
            float yag=xa*mR[1]+ya*mR[5]+za*mR[9];
            float zag=xa*mR[2]+ya*mR[6]+za*mR[10];//化为xag，yag，zag，世界坐标系下的加速度 ,*R Coordinate System transfer
            acxList.add(xag);
            acyList.add(yag);
            aczList.add(zag);
            /*
            Log.d("Ruler", "xa is " +xa);
            Log.d("Ruler", "ya is " +ya);
            Log.d("Ruler", "za is " +za);
            Log.d("Ruler", "xag is " +xag);
            Log.d("Ruler", "yag is " +yag);
            Log.d("Ruler", "zag is " +zag);
            */


            if(k>=2*span) {

                List<Float> sortList = new ArrayList<Float>(acxList.subList(k - 2 * span, k));
                Collections.sort(sortList);
                float mid = sortList.get(sortList.size() / 2);
                float xas = acxList.get(k - span) - mid;
                acsxList.add(xas);

                List<Float> sortList2 = new ArrayList<Float>(acyList.subList(k - 2 * span, k));
                Collections.sort(sortList);
                float mid2 = sortList.get(sortList2.size() / 2);
                float yas = acyList.get(k - span) - mid2;
                acsyList.add(yas);

                List<Float> sortList3 = new ArrayList<Float>(aczList.subList(k - 2 * span, k));
                Collections.sort(sortList);
                float mid3 = sortList.get(sortList3.size() / 2);
                float zas = aczList.get(k - span) - mid3;
                acszList.add(zas);

                //数值积分  //500Hz
                vx += xas * dt;
                vy += yas * dt;
                vz += zas * dt;
                if (k % 100 == 0)  //显示 5Hz
                {
                    Vx.setText("VX= " + (float) Math.round(vx * 100) / 100 + "m/s ");
                    Vy.setText("VY= " + (float) Math.round(vy * 100) / 100 + "m/s ");
                    Vz.setText("VZ= " + (float) Math.round(vz * 100) / 100 + "m/s ");
                    v = (float) Math.sqrt((double) (vx * vx + vy * vy + vz * vz));
                    V.setText("Speed= " + (float) Math.round(v * 1000) / 1000 + "m/s");
                }

                /*vxList.add(vx);
                vyList.add(vy);
                vzList.add(vz);



                if (flag == 1) {
                    kb = ks;
                    Log.d("Ruler", "kb= " +kb);
                    flag= 5;
                }
                if (flag == 2) {
                    ke = ks;
                    Log.d("Ruler", "ke= " +ke);
                    flag = 0;
                }

            }

            if (flag == 3) {


                a0x = (vxList.get(kb) * ((1 + ke) * ke / 2 * dt * dt) - ((1 + kb) * kb / 2 * dt * dt) * vxList.get(ke)) / ((kb * dt * ((1 + ke) * ke / 2 * dt * dt)) - (((1 + kb) * kb / 2 * dt * dt) * ke * dt));
                dax = (kb * dt * vxList.get(ke) - vxList.get(kb) * ke * dt) / ((kb * dt * ((1 + ke) * ke / 2 * dt * dt)) - (((1 + kb) * kb / 2 * dt * dt) * ke * dt));

                a0y = (vyList.get(kb) * ((1 + ke) * ke / 2 * dt * dt) - ((1 + kb) * kb / 2 * dt * dt) * vyList.get(ke)) / ((kb * dt * ((1 + ke) * ke / 2 * dt * dt)) - (((1 + kb) * kb / 2 * dt * dt) * ke * dt));
                day = (kb * dt * vyList.get(ke) - vyList.get(kb) * ke * dt) / ((kb * dt * ((1 + ke) * ke / 2 * dt * dt)) - (((1 + kb) * kb / 2 * dt * dt) * ke * dt));

                a0z = (vzList.get(kb) * ((1 + ke) * ke / 2 * dt * dt) - ((1 + kb) * kb / 2 * dt * dt) * vzList.get(ke)) / ((kb * dt * ((1 + ke) * ke / 2 * dt * dt)) - (((1 + kb) * kb / 2 * dt * dt) * ke * dt));
                daz = (kb * dt * vzList.get(ke) - vzList.get(kb) * ke * dt) / ((kb * dt * ((1 + ke) * ke / 2 * dt * dt)) - (((1 + kb) * kb / 2 * dt * dt) * ke * dt));

                Log.d("Speedometry", "a0x=  " +a0x +"   dax=  "+dax);
                Log.d("Speedometry", "a0x=  " +a0y +"   dax=  "+day);
                Log.d("Speedometry", "a0x=  " +a0z +"   daz=  "+daz);

                flag = 4;
            }

            if (flag ==4 ){

                vxc = vx - a0x * ks * dt - (1 + ks) * ks / 2 * dax * dt * dt;
                vyc = vy - a0y * ks * dt - (1 + ks) * ks / 2 * day * dt * dt;
                vzc = vz - a0z * ks * dt - (1 + ks) * ks / 2 * daz * dt * dt;




                if (k % 100 == 0)  //显示 5Hz
                {
                    Vx.setText("Speed X= " + (float) Math.round(vxc * 1000) / 1000 + "m/s");
                    Vy.setText("Speed Y= " + (float) Math.round(vyc * 1000) / 1000 + "m/s");
                    Vz.setText("Speed Z= " + (float) Math.round(vzc * 1000) / 1000 + "m/s");
                    v = (float) Math.sqrt((double) (vxc * vxc + vyc * vyc + vzc * vzc));
                    V.setText("Speed= " + (float) Math.round(v * 1000) / 1000 + "m/s");
                }*/
            }
            k++;
            ks++;
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
/*
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.
        TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, magneticSensor,
        SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener, accelerometerSensor,
        SensorManager.SENSOR_DELAY_GAME);
        }
@Override
protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
        sensorManager.unregisterListener(listener);
        }
        }
        private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
float[] magneticValues = new float[3];
@Override
public void onSensorChanged(SensorEvent event) {
// 判断当前是加速度传感器还是地磁传感器
if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
// 注意赋值时要调用clone()方法
accelerometerValues = event.values.clone();
} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
// 注意赋值时要调用clone()方法
magneticValues = event.values.clone();
}
float[] R = new float[9];
float[] values = new float[3];
SensorManager.getRotationMatrix(R, null, accelerometerValues,
magneticValues);
SensorManager.getOrientation(R, values);
Log.d("MainActivity", "value[0] is " + Math.toDegrees(values[0]));
*/
// Set distance to zero at start-up:
/*var distance_X = 0
    var velocity_X = 0
    function update_acceleration_X (acceleration_X) {
        velocity_X = velocity_X + acceleration_X
        distance_X = distance_X + velocity_X
    }
    // To use the distance value just read the distance_X variable:
    function get_distance_X_and_reset () {
        x = distance_X
        distance_X = 0
        return x
    }*/

