package com.example.lenovo.delaytest;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    private SensorManager sensorManager;
    private TextView Velocityx;
    private TextView Velocityy;
    private TextView Velocityz;
    private TextView Rx;
    private TextView Ry;
    private TextView Rz;
    private TextView Rx1;
    private TextView Ry1;
    private TextView Rz1;
    private TextView Rx2;
    private TextView Ry2;
    private TextView Rz2;
    float vz=0,vx=0,vy=0,rz=0,ry=0,rx=0;
    private float[] mGData = new float[3];
    private float[] mGData2 = new float[3];
    private float[] mMData = new float[3];
    private float[] mR = new float[16];
    private float[] mI = new float[16];
    private float[] mOrientation = new float[3];
    private float[] gravity=new float[3];
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Velocityx = (TextView) findViewById(R.id.velocityx);
        Velocityy = (TextView) findViewById(R.id.velocityy);
        Velocityz = (TextView) findViewById(R.id.velocityz);
        Rx = (TextView) findViewById(R.id.textViewrx);
        Ry = (TextView) findViewById(R.id.textViewry);
        Rz = (TextView) findViewById(R.id.textViewrz);
        Rx1= (TextView) findViewById(R.id.textView2);
        Ry1= (TextView) findViewById(R.id.textView3);
        Rz1= (TextView) findViewById(R.id.textView4);
        Rx2= (TextView) findViewById(R.id.textView5);
        Ry2= (TextView) findViewById(R.id.textView6);
        Rz2= (TextView) findViewById(R.id.textView7);
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
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, accelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener,  linearaccelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
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
            //double g = 9.8;
            float xa = mGData2[0];
            float ya = mGData2[1];
            float za = mGData2[2];//用线性加速度传感器（还要作标定改正）,或者滤除重力
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
            float zag=xa*mR[2]+ya*mR[6]+za*mR[10];//化为xag，yag，zag，世界坐标系下的
            Log.d("MainActivity", "xa is " +xa);
            Log.d("MainActivity", "ya is " +ya);
            Log.d("MainActivity", "za is " +za);
            Log.d("MainActivity", "xag is " +xag);
            Log.d("MainActivity", "yag is " +yag);
            Log.d("MainActivity", "zag is " +zag);
            float xVabs=Math.abs(xa);
            float yVabs=Math.abs(ya);
            float zVabs=Math.abs(za);
            if (xVabs > 14 || yVabs > 14 || zVabs> 14) {

                Toast.makeText(MainActivity.this, "你动得太用力了",
                        Toast.LENGTH_SHORT).show();
            }

            vz+=zag*0.02;
            vy+=yag*0.02;
            vx+=xag*0.02;

            rz+=vz*0.02;
            ry+=vy*0.02;
            rx+=vx*0.02;

            k++;


                Velocityx.setText("azimuth= " +azimuth+"度");
                Velocityy.setText("pitch " +pitch+"度");
                Velocityz.setText("roll " + roll+"度");//角度
                Rx.setText("ax="+xag+"m/s2");
                Ry.setText("ay=" +yag+"m/s2");
                Rz.setText("az=" +zag+"m/s2");//加速度
                Rx1.setText("vx=" +vx+"m/s");
                Ry1.setText("vy=" +vy+"m/s");
                Rz1.setText("vz=" +vz+"m/s");//速度
                Rx2.setText("sx=" +rx+"m");
                Ry2.setText("sy=" +ry+"m");
                Rz2.setText("sz=" +rz+"m");//距离

                /*Velocityx.setText("x轴上的速度 " + vx + " m/s");
                Velocityy.setText("y轴上的速度 " + vy + " m/s");
                Velocityz.setText("z轴上的速度 " + vz + " m/s");
                Rx.setText("x轴位移 " + rx + " m");
                Ry.setText("y轴位移 " + ry + " m");
                Rz.setText("z轴位移 " + rz + " m");*/



        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    public void gotoSecondActivity(View v){
        Intent it = new Intent(this,SecondActivity.class);
        startActivity(it);
    }
    public void gotoThirdActivity(View v){
        Intent it = new Intent(this,ThirdActivity.class);
        startActivity(it);
    }
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
