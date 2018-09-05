package com.test.measureapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "From top to bottom: 1.About 2.Ruler 3.Protractor 4.Speedometer 5.Magnetometer", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goto2Activity(View v){
        Intent it = new Intent(this,Ruler.class);
        startActivity(it);
    }
    public void goto3Activity(View v){
        Intent it = new Intent(this,Protractor.class);
        startActivity(it);
    }
    public void goto4Activity(View v){
        Intent it = new Intent(this,Speedometry.class);
        startActivity(it);
    }
    public void goto5Activity(View v){
        Intent it = new Intent(this,Magnetometer.class);
        startActivity(it);
    }
    public void goto6Activity(View v){
        Intent it = new Intent(this,About.class);
        startActivity(it);
    }
}
