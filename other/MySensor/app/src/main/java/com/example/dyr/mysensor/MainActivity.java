package com.example.dyr.mysensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SensorManager msensorManager;
    List<TextView> mSensorList;

    public static final String MYNOTE = "com.dyr.sensor.PASS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorList = new ArrayList<>();

        Button detectButton = (Button) findViewById(R.id.detectButton);
        Button detailsButton = (Button) findViewById(R.id.detailsButton);

        TextView textView0 = (TextView) findViewById(R.id.textView);
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        TextView textView2 = (TextView) findViewById(R.id.textView3);

        mSensorList.add(textView0);
        mSensorList.add(textView1);
        mSensorList.add(textView2);

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSensor(mSensorList);
            }
        });

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllSensor();
            }
        });
    }

    public void setSensor(List<TextView> listText){
        int i = 0;
        msensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensor = msensorManager.getSensorList(Sensor.TYPE_ALL);
        if(deviceSensor.isEmpty()){
            Toast.makeText(this, "No sensor found!!", Toast.LENGTH_SHORT).show();
        }

        for(Sensor s: deviceSensor){
            if(i > listText.size()) break;
            listText.get(i).setText(s.getName());
            i++;
        }
    }

    public void getAllSensor(){
        Intent getDetails = new Intent(this,SensorActivity.class);
        getDetails.putExtra("optional", MYNOTE);
        startActivity(getDetails);
    }
}
