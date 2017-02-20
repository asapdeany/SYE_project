package com.example.deansponholz.sye_project;

import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by deansponholz on 2/20/17.
 */

public class Constants_Display {

    int x;
    WindowManager wm;
    public Constants_Display(){
        x = 5;
    }

    public void offSetCalculator() {


        //Screen Inches
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int widthtest = dm.widthPixels;
        int heighttest = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) widthtest / (double) dens;
        double hi = (double) heighttest / (double) dens;
        double xtest = Math.pow(wi, 2);
        double ytest = Math.pow(hi, 2);
        double screenInches = Math.sqrt(xtest + ytest);


        /*
        //screen Pixels
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        */
    }
}
