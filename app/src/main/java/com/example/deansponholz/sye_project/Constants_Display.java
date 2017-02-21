package com.example.deansponholz.sye_project;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by deansponholz on 2/20/17.
 */

public class Constants_Display {

    WindowManager wm;
    Display display;
    Point size;
    Bitmap test;

    int dens, width, height;
    int widthtest;
    int heighttest;

    double wi, hi, xtest, ytest, screenInches;


    int bitmapHeight, bitmapWidth;
    String imageType;


    public Constants_Display(Context context){

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        offSetCalculator();
    }

    public Bitmap loadBitmapEfficiently(Context context, Resources res, int resId, int reqWidth, int reqHeight){


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);




    }

    public List readBitmapDimensions(Context context, int resID){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resID, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        List<Object> objectList = new ArrayList<Object>();
        objectList.add(imageHeight);
        objectList.add(imageWidth);
        objectList.add(imageType);

        return objectList;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void offSetCalculator() {

        //Screen Inches
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        widthtest = dm.widthPixels;
        heighttest = dm.heightPixels;
        dens = dm.densityDpi;
        wi = (double) widthtest / (double) dens;
        hi = (double) heighttest / (double) dens;
        screenInches = Math.sqrt(xtest + ytest);

        //screen Pixels
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        Log.d("widthtest", Integer.toString(widthtest));
        Log.d("heighttes", Integer.toString(heighttest));
        Log.d("width", Integer.toString(width));
        Log.d("heigh", Integer.toString(height));
        Log.d("dens", Integer.toString(dens));
        Log.d("wi", Double.toString(wi));
        Log.d("hi", Double.toString(hi));
        Log.d("xtest", Double.toString(xtest));
        Log.d("ytest", Double.toString(ytest));
        Log.d("screenInches", Double.toString(screenInches));

    }
}
