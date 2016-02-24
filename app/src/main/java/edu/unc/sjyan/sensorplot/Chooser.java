package edu.unc.sjyan.sensorplot;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Stephen on 2/23/16.
 */
public class Chooser extends AppCompatActivity {

    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        setTitle("SensorPlot");

        TextView accelAvailability = (TextView) findViewById(R.id.textView12);
        TextView accelInfo = (TextView) findViewById(R.id.textView13);
        TextView lightAvailability = (TextView) findViewById(R.id.textView14);
        TextView lightInfo = (TextView) findViewById(R.id.textView15);
        TextView magAvailability = (TextView) findViewById(R.id.textView16);
        TextView magInfo =(TextView) findViewById(R.id.textView17);

        // Accel
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelAvailability.append("Accelerometer is present");
            float maxRange = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getMaximumRange();
            float res = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getResolution();
            int minDelay = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getMinDelay();

            accelInfo.append("maxRange: " + maxRange + ", res: " + res + ", minDelay: " + minDelay);
        } else {
            accelAvailability.append(" Accelerometer is unavailable");
            accelInfo.append("N/A");
        }

        // Light
        if(sm.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            lightAvailability.append("Light Sensor is present");
            float maxRange = sm.getDefaultSensor(Sensor.TYPE_LIGHT).getMaximumRange();
            float res = sm.getDefaultSensor(Sensor.TYPE_LIGHT).getResolution();
            int minDelay = sm.getDefaultSensor(Sensor.TYPE_LIGHT).getMinDelay();

            lightInfo.append("maxRange: " + maxRange + ", res: " + res + ", minDelay: " + minDelay);
        } else {
            lightAvailability.append("Light Sensor is unavailable");
            lightInfo.append("N/A");
        }

        // Magnetism
        if(sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            magAvailability.append("MagField Sensor is present");
            float maxRange = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).getMaximumRange();
            float res = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).getResolution();
            int minDelay = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).getMinDelay();

            magInfo.append("maxRange: " + maxRange + ", res: " + res + ", minDelay: " + minDelay);
        } else {
            magAvailability.append("MagField Sensor is unavailable");
            magInfo.append("N/A");
        }


    }

    public void onClick(View v) {
        if(v.getId() == R.id.button) {
            // Accelerometer
            Intent intentA = new Intent(this, AccelPlot.class);
            startActivity(intentA);

        } else if(v.getId() == R.id.button2) {
            // Light
            Intent intentL = new Intent(this, LightPlot.class);
            startActivity(intentL);
        } else if(v.getId() == R.id.button3) {
            // MagField
            Intent intentM = new Intent(this, MagPlot.class);
            startActivity(intentM);
        }

    }
}
