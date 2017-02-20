package com.example.deansponholz.sye_project;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by deansponholz on 1/24/17.
 */

public class Fragment_Calibrate extends Fragment {


    public SensorHandler sensorHandler = null;

    Display display;
    WindowManager wm;
    Point size;
    int width;
    int height;
    Button startButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calibrate, container, false);
        wm = (WindowManager) root.getContext().getSystemService(Context.WINDOW_SERVICE);
        offSetCalculator();
        sensorHandler = new SensorHandler(root.getContext());
        RelativeLayout fragment_calibrate = (RelativeLayout) root.findViewById(R.id.fragment_calibrate);
        CalibrateDrawView calibrateDrawView = new CalibrateDrawView(this.getActivity());
        fragment_calibrate.addView(calibrateDrawView);

        startButton = (Button) root.findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Play.class);
                getActivity().startActivity(intent);
            }
        });
        return root;

    }

    public class CalibrateDrawView extends View {

        //onDraw
        Canvas canvas;
        Paint paint = new Paint();


        public CalibrateDrawView(Context context) {
            super(context);
            initMyView();
        }

        public void initMyView() {
            //Drawing Tools
            canvas = new Canvas();
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawCircle(width / 2, height / 2, 100, paint);

            canvas.drawCircle((float)(-sensorHandler.xPos*43 + 1280), (float) (sensorHandler.yPos*38 + 720), 80, paint);
            //canvas.drawLine((float) (-sensorHandler.xPos * 15), (float) (sensorHandler.yPos * 15), width / 2, height / 2, paint);


            invalidate();
        }

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


        //screen Pixels
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }
}
