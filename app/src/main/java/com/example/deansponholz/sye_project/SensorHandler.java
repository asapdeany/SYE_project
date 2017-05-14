package com.example.deansponholz.sye_project;

import android.content.Context;
import android.hardware.SensorManager;



/**
 * Created by deansponholz on 1/19/17.
 */
public class SensorHandler {


    SensorData s = null;

    public double accelmagZ, accelmagX, accelmagY;
    public double gyroZ, gyroX, gyroY;
    public static double zPos, xPos, yPos;


    public SensorHandler(Context context) {
        s = new SensorData((SensorManager) context.getSystemService(Context.SENSOR_SERVICE), this);

    }

    public void setAccelMagValues(float[] values) {
        accelmagZ = values[0] * 180 / Math.PI;
        accelmagX = values[1] * 180 / Math.PI;
        accelmagY = -values[2] * 180 / Math.PI;
    }

    public void setGyroValues(float[] values) {
        gyroZ = values[0] * 180 / Math.PI;
        gyroX = values[1] * 180 / Math.PI;
        gyroY = -values[2] * 180 / Math.PI;
    }

    public void setFusedValues(float[] values) {
        zPos = values[0] * 180 / Math.PI;
        xPos = values[1] * 180 / Math.PI;
        yPos = -values[2] * 180 / Math.PI;

    }
}
