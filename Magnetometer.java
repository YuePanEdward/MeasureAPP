package com.test.measureapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Magnetometer extends AppCompatActivity {
    private SensorManager sensorManager;
    private TextView MX,MY,MZ;
    private float[] mMData = new float[3];
    int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer);
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
        MX = (TextView) findViewById(R.id.textView20);
        MY = (TextView) findViewById(R.id.textView21);
        MZ = (TextView) findViewById(R.id.textView22);
        sensorManager = (SensorManager) getSystemService
                (Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.
                TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(listener, magneticSensor,
                20000); //50Hz
    }
    private SensorEventListener listener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMData = event.values;

            }
            float mx = (float) Math.toDegrees(mMData[0]);
            float my = (float) Math.toDegrees(mMData[1]);
            float mz = (float) Math.toDegrees(mMData[2]);
            if(k%10==0) //5Hz
            {
                MX.setText("X= " + (float)Math.round(mx*1000)/1000 + " μT");
                MY.setText("Y= " + (float)Math.round(my*1000)/1000 + " μT");
                MZ.setText("Z= " + (float)Math.round(mz*1000)/1000 + " μT");
            }
            k++;
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
