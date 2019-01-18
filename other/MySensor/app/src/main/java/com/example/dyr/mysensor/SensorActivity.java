package com.example.dyr.mysensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private String sensorDetails = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        TextView showDetail = (TextView) findViewById(R.id.textSensorDetail);

        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                   ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }

        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }
        if((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) != null){

            sensorDetails += "id:" + mSensor.getId() + ",name:" + mSensor.getName() + ",vendor:" +
                    ",version:"+ mSensor.getVersion() + ",max range:" + mSensor.getMaximumRange() +
                    ",min delay:"  + mSensor.getMinDelay() + "\n";
        }else{
            sensorDetails += "Null\n";
        }

        showDetail.setText(sensorDetails);
    }
}
