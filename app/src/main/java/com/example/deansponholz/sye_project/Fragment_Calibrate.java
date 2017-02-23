package com.example.deansponholz.sye_project;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by deansponholz on 1/24/17.
 */

public class Fragment_Calibrate extends Fragment {


    public SensorHandler sensorHandler = null;


    WindowManager wm;

    Button startButton;


    Constants_Display constants_display;
    System_UI_Manager system_ui_manager;

    RelativeLayout fragment_calibrate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calibrate, container, false);
        wm = (WindowManager) root.getContext().getSystemService(Context.WINDOW_SERVICE);

        system_ui_manager = new System_UI_Manager(getActivity());
        constants_display = new Constants_Display(root.getContext());

        sensorHandler = new SensorHandler(root.getContext());
        fragment_calibrate = (RelativeLayout) root.findViewById(R.id.fragment_calibrate);

        CalibrateDrawView calibrateDrawView = new CalibrateDrawView(this.getActivity());
        fragment_calibrate.addView(calibrateDrawView);
        System.out.print("he4lo");

        //displayImages();



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
            canvas.drawCircle(constants_display.width / 2, constants_display.height / 2, 100, paint);

            canvas.drawCircle((float)(-sensorHandler.xPos*43 + (constants_display.width)/2), (float) (sensorHandler.yPos*38 + (constants_display.height)/2), 80, paint);
            //canvas.drawLine((float) (-sensorHandler.xPos * 15), (float) (sensorHandler.yPos * 15), width / 2, height / 2, paint);


            invalidate();
        }

    }


}
