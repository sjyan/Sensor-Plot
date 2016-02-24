package edu.unc.sjyan.sensorplot;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Paint;
import android.hardware.SensorEventListener;
import java.util.Random;
import android.util.Log;

/**
 * Created by Stephen on 2/22/16.
 */
public class PlotView extends View {
    private ArrayList<Float> floatList;
    private static String sensorType;

    public PlotView(Context c) {
        super(c);
        doAdditionalConstructorWork();
    }

    public PlotView(Context c, AttributeSet attrs) {
        super(c, attrs);
        doAdditionalConstructorWork();
    }

    public PlotView(Context c, AttributeSet attrs, int s) {
        super(c, attrs, s);
        doAdditionalConstructorWork();
    }

    public PlotView(Context c, AttributeSet attrs, int s1, int s2) {
        super(c, attrs, s1);
        doAdditionalConstructorWork();
    }

    private void doAdditionalConstructorWork() {
        floatList = new ArrayList<>(10);

        for(int i = 0; i < 10; i++) {
            floatList.add((float) (0));
        }

    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        Paint p = new Paint();
        Paint pMean = new Paint();
        Paint pSD = new Paint();
        Paint gridP = new Paint();
        p.setColor(Color.BLUE);
        pMean.setColor(Color.GREEN);
        pSD.setColor(Color.RED);
        gridP.setColor(Color.BLACK);

        int unit_x = c.getWidth() / 9;
        int unit_y = c.getHeight() / 9;
        int data_unit_y = 0;

        if(sensorType.equals("ACCEL")) {
            data_unit_y = c.getHeight() / 17;
        } else if(sensorType.equals("LIGHT")) {
            data_unit_y = c.getHeight() / 500;
        } else if(sensorType.equals("MAG")) {
            data_unit_y = c.getHeight() / 50;
        }

        // setup grid
        for(int i = 0; i < 10; i++) {
            c.drawLine(i * unit_x, 0, i * unit_x, c.getHeight(), gridP);
            c.drawLine(0, i * unit_y, c.getHeight(), i * unit_y, gridP);
        }


        // plot values, means, deviations
        for(int i = 0; i < 10; i++) {
            float g = (float)(9.81);
            // acceleration
            if(sensorType.equals("ACCEL")) {
                // values
                c.drawCircle(i * unit_x, c.getHeight() - floatList.get(i) * data_unit_y, 8f, p);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight() - floatList.get(i) * data_unit_y, (i + 1) * unit_x,
                            c.getHeight() - floatList.get(i + 1) * data_unit_y, p);
                }

                // means
                c.drawCircle(i * unit_x, c.getHeight() - mean(i) * data_unit_y, 8f, pMean);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight() - mean(i) * data_unit_y,
                            (i + 1) * unit_x, c.getHeight() - mean(i + 1) * data_unit_y, pMean);
                }

                // stdev
                c.drawCircle(i * unit_x, c.getHeight() - sd(i) * data_unit_y, 8f, pSD);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight() - sd(i) * data_unit_y,
                            (i + 1) * unit_x, c.getHeight() - sd(i + 1) * data_unit_y, pSD);
                }
            } else if(sensorType.equals("LIGHT")) {
                // values
                c.drawCircle(i * unit_x, c.getHeight() - floatList.get(i) * data_unit_y, 8f, p);
                if (i < 9) {
                    c.drawLine(i * unit_x, c.getHeight() - floatList.get(i) * data_unit_y, (i + 1) * unit_x,
                            c.getHeight() - floatList.get(i + 1) * data_unit_y, p);
                }

                // means
                c.drawCircle(i * unit_x, c.getHeight() - mean(i) * data_unit_y, 8f, pMean);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight() - mean(i) * data_unit_y,
                            (i + 1) * unit_x, c.getHeight() - mean(i + 1) * data_unit_y, pMean);
                }

                //stdev
                c.drawCircle(i * unit_x, c.getHeight() - sd(i) * data_unit_y, 8f, pSD);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight() - sd(i) * data_unit_y,
                            (i + 1) * unit_x, c.getHeight() - sd(i + 1) * data_unit_y, pSD);
                }
            } else if(sensorType.equals("MAG")) {
                // values
                c.drawCircle(i * unit_x, c.getHeight()/2 - floatList.get(i) * data_unit_y, 8f, p);
                if (i < 9) {
                    c.drawLine(i * unit_x, c.getHeight()/2 - floatList.get(i) * data_unit_y, (i + 1) * unit_x,
                            c.getHeight()/2 - floatList.get(i + 1) * data_unit_y, p);
                }

                // means
                c.drawCircle(i * unit_x, c.getHeight()/2 - mean(i) * data_unit_y, 8f, pMean);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight()/2 - mean(i) * data_unit_y,
                            (i + 1) * unit_x, c.getHeight()/2 - mean(i + 1) * data_unit_y, pMean);
                }

                //stdev
                c.drawCircle(i * unit_x, c.getHeight()/2 - sd(i) * data_unit_y, 8f, pSD);
                if(i < 9) {
                    c.drawLine(i * unit_x, c.getHeight()/2 - sd(i) * data_unit_y,
                            (i + 1) * unit_x, c.getHeight()/2 - sd(i + 1) * data_unit_y, pSD);
                }
            }
        }

    }

    private float sd(int index) {
        float mean = mean(index);
        float sum = 0;
        int runningCount = 0;

        for(int i = index; i >= 0; i--) {
            if(runningCount < 5) {
                sum += Math.pow((floatList.get(i) - mean), 2);
            } else {
                break;
            }

            runningCount++;
        }

        return (float) (Math.sqrt(sum / runningCount));
    }

    private float mean(int index) {
        float sum = 0;
        int runningCount = 0;
        for(int i = index; i >= 0; i--) {
            if(runningCount < 5) {
                sum += floatList.get(i);
            } else {
                break;
            }

            runningCount++;
        }

        return sum / runningCount;
    }

    public void clearList() {
        floatList.clear();
    }

    public void addPoint(float v) {
        if(floatList.size() >= 10) {
            floatList.add(v);
            floatList.remove(0);
        } else {
            floatList.add(v);
        }
    }

    public void registerSensor(String s) {
        sensorType = s;
    }

}
