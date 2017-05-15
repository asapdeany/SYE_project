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

import java.util.Random;

/**
 * Created by deansponholz on 1/24/17.
 */

public class Fragment_Calibrate extends Fragment {


    public SensorHandler sensorHandler = null;

    Button startButton, calibrateButton;


    Constants_Display constants_display;
    System_UI_Manager system_ui_manager;

    RelativeLayout fragment_calibrate;
    private static final int LINE_SPACING = 100;
    Random rand = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calibrate, container, false);

        system_ui_manager = new System_UI_Manager(getActivity());
        constants_display = new Constants_Display(root.getContext());

        sensorHandler = new SensorHandler(root.getContext());
        fragment_calibrate = (RelativeLayout) root.findViewById(R.id.fragment_calibrate);

        CalibrateDrawView calibrateDrawView = new CalibrateDrawView(this.getActivity());
        fragment_calibrate.addView(calibrateDrawView);



        startButton = (Button) root.findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });
        calibrateButton = (Button) root.findViewById(R.id.button_calibrate);
        calibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants_Display.difference = 0;
                double center = (constants_display.height / 2);
                double currentY = (sensorHandler.yPos*38 + (constants_display.height)/2);
                //Log.d("Y", Double.toString(currentY));
                Constants_Display.difference = currentY - center;
                Log.d("difference", Double.toString(Constants_Display.difference));

            }
        });
        return root;

    }

    public class CalibrateDrawView extends View {

        //onDraw
        Canvas canvas;
        float fishX, fishY;
        Paint paintCircle = new Paint();
        Paint paintLine = new Paint();


        public CalibrateDrawView(Context context) {
            super(context);
            initMyView();
        }

        public void initMyView() {
            //Drawing Tools
            canvas = new Canvas();
            paintCircle = new Paint();
            paintCircle.setColor(Color.BLACK);
            paintCircle.setStrokeWidth(7);
            paintCircle.setStyle(Paint.Style.STROKE);

            paintLine = new Paint();
            paintLine.setColor(Color.RED);
            paintLine.setStrokeWidth(3.25f);
            paintLine.setStyle(Paint.Style.STROKE);

        }

        @Override
        public void onDraw(Canvas canvas) {


            int yOffset = (Constants_Display.height / 2) - 60;
            int xOffset = (Constants_Display.width / 2) - 55;

            canvas.drawCircle(constants_display.width / 2, constants_display.height / 2, 10, paintCircle);

            canvas.drawCircle((float)(-sensorHandler.xPos*43 + (constants_display.width)/2),
                    (float) (sensorHandler.yPos*38 + ((constants_display.height)/2) - Constants_Display.difference),
                    80, paintCircle);



            fishX = (float) (-sensorHandler.xPos * 5) + xOffset;
            fishY = (float) (sensorHandler.yPos * 5) + yOffset;




            //Loop through to create 10 vertical lines
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(fishX + (i * LINE_SPACING), -Constants_Display.height, fishX + (i * LINE_SPACING), +Constants_Display.height, paintLine);

            }
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(fishX + (i * -LINE_SPACING), -Constants_Display.height, fishX + (i * -LINE_SPACING), +Constants_Display.height, paintLine);

            }

            //Loop through to create 10 horizontal lines
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(0, fishY + (i * LINE_SPACING), Constants_Display.width, fishY + (i * LINE_SPACING), paintLine);

            }
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(0, fishY - (i * LINE_SPACING), Constants_Display.width, fishY - (i * LINE_SPACING), paintLine);
            }
            //middle vertical line from landscape point of view
            canvas.drawLine(fishX, -Constants_Display.height, fishX, Constants_Display.height, paintLine);
            //middle horizontal line from landscape point of view
            canvas.drawLine(0, fishY, Constants_Display.width, fishY, paintLine);
            invalidate();
        }

    }

    @Override
    public void onResume() {
        system_ui_manager.hideView();
        super.onResume();
    }
}
