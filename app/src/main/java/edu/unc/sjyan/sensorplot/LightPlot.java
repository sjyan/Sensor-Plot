package edu.unc.sjyan.sensorplot;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import java.util.List;
import android.os.Handler;
import android.content.Intent;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;

public class LightPlot extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor s;
    private List<Sensor> l;
    private PlotView pv;
    private static float currentValue = 0.0f;
    private static float currentAlpha = 1.0f;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        l = sm.getSensorList(Sensor.TYPE_ALL);
        s = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        sm.registerListener(this, s, 1000000);
        pv = (PlotView) findViewById(R.id.plotview);
        pv.registerSensor("LIGHT");

        handler.post(runnableCode);
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            TextView light = (TextView) findViewById(R.id.textView21);
            light.animate().alpha(currentValue / 1000).setDuration(100);

            pv.addPoint(currentValue);
            pv.invalidate();
            handler.postDelayed(runnableCode, 200);
        }
    };

    public void onClick(View v) {
        if(v.getId() == R.id.imageButton) {
            handler.removeCallbacksAndMessages(null);
            finish();
            Intent intentHome = new Intent(this, Chooser.class);
            startActivity(intentHome);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentValue = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
