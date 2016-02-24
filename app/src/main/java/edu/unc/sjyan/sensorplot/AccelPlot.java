package edu.unc.sjyan.sensorplot;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import java.util.List;
import android.os.Handler;
import android.view.animation.Animation;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Stephen on 2/23/16.
 */
public class AccelPlot extends AppCompatActivity implements SensorEventListener {
    private SensorManager sm;
    private Sensor s;
    private List<Sensor> l;
    private PlotView pv;
    private AnimationDrawable anim;
    private static float currentValue = 0;
    private Handler handler = new Handler();
    private int frameCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accel);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        l = sm.getSensorList(Sensor.TYPE_ALL);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this, s, 1000000);
        pv = (PlotView) findViewById(R.id.plotview2);
        pv.registerSensor("ACCEL");

        anim = new AnimationDrawable();
        anim.setOneShot(false);
        ImageView megaman = (ImageView) findViewById(R.id.imageView);
        megaman.getLayoutParams().height = 150;
        megaman.getLayoutParams().width = 150;
        megaman.setBackgroundDrawable(anim);
        megaman.post(new Starter());
        handler.post(runnableCode);



    }

    class Starter implements Runnable {
        @Override
        public void run() {
            anim.start();
        }
    }


    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            int id = getResources().getIdentifier("mega" + Integer.toString(1 + frameCounter % 4),
                    "drawable", getPackageName());
            Drawable frame = getResources().getDrawable(id);
            anim.addFrame(frame, (int) ((10000 - currentValue * 500) / (currentValue * currentValue)));
            String message = Integer.toString((int)((10000 - currentValue * 500) / (currentValue * currentValue)));
            Log.i("Frame Interval", message);

            pv.addPoint(currentValue);
            pv.invalidate();
            frameCounter++;

            handler.postDelayed(runnableCode, 200);
        }
    };

    public void onClick(View v) {
        if(v.getId() == R.id.imageButton2) {
            handler.removeCallbacksAndMessages(null);
            finish();
            Intent intentHome = new Intent(this, Chooser.class);
            startActivity(intentHome);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        currentValue = (float)(Math.sqrt(x * x + y * y + z * z));
        pv.addPoint(currentValue);
        pv.invalidate();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


