package com.example.lenovo.delaytest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends Activity {

    private SensorManager sensorManager;
    private TextView ang;
    private TextView se;
    private float[] mGData = new float[3];
    private float[] mGData2 = new float[3];
    private float[] mMData = new float[3];
    private float[] mR = new float[16];
    private float[] mI = new float[16];
    private float[] mOrientation = new float[3];
    private int flag = 0;
    private float azimuthstart, azimuthend,showang;
    private int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ang= (TextView)findViewById(R.id.textView10);
        se= (TextView)findViewById(R.id.textView11);
        sensorManager = (SensorManager) getSystemService
                (Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.
                TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, magneticSensor,
                20000);
        sensorManager.registerListener(listener, accelerometerSensor,
                20000);
    }


    public void goBack(View v) {
        finish();
    }

    public void Startmeasure(View v) {
        flag = 1;
    }
    public void Endmeasure(View v) {
        flag=2;

    }

    private SensorEventListener listener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type == Sensor.TYPE_ACCELEROMETER) {
                mGData = event.values;
            }
            if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMData = event.values;
            }
            // 根据设备传输过来的向量数据计算倾斜矩阵mR以及旋转矩阵mI
            SensorManager.getRotationMatrix(mR, mI, mGData, mMData);
            // 根据旋转矩阵mR计算出设备的方向
            SensorManager.getOrientation(mR, mOrientation);
            float azimuth = (float) Math.toDegrees(mOrientation[0]);
            //手机绕x轴旋转的度数
            float pitch = (float) Math.toDegrees(mOrientation[1]);
            //手机绕y轴旋转的度数
            float roll = (float) Math.toDegrees(mOrientation[2]);
            if (flag == 1) {
                azimuthstart = azimuth;
                flag = 0;
            }
            if (flag == 2) {
                azimuthend = azimuth;
                if(azimuthstart>0&&azimuthend<0)
                {showang=360+azimuthend-azimuthstart;}
                else{showang=azimuthend-azimuthstart;}
                int degree=(int)showang*2;
                int min=(int)((showang-degree)*60);
                float second=((showang-degree)*60-min)*60;
                ang.setText("所测角度为"+degree+"度");
                se.setText("初始"+azimuthstart+"  结束"+azimuthend);
                flag = 0;
               // sensorManager.unregisterListener(listener);

            }
            k++;
            /*
            if(k%10==0&&(Math.abs(pitch)>10||Math.abs(roll)>10)) {
                Toast.makeText(ThirdActivity.this, "请保持手机水平放置",
                        Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    /*@Override
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
    }*/
}
