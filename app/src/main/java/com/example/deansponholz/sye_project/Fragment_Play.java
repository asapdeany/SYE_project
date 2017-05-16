package com.example.deansponholz.sye_project;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by deansponholz on 1/20/17.
 */

public class Fragment_Play extends Fragment {

    System_UI_Manager system_ui_manager;
    Constants_Display constants_display;
    Button button_play;
    RelativeLayout fragment_play;

    Bitmap bitmap_Logo;
    ImageView iv_Logo;
    public SensorHandler sensorHandler = null;
    private static final int LINE_SPACING = 100;

    PlayButtonDrawView playButtonDrawView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_play, container, false);

        //hide status bar
        system_ui_manager = new System_UI_Manager(getActivity());


        constants_display = new Constants_Display(root.getContext());

        sensorHandler = new SensorHandler(root.getContext());

        playButtonDrawView = new PlayButtonDrawView(this.getActivity());

        //Initialization
        button_play = (Button) root.findViewById(R.id.button_play);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Calibrate.class);
                getActivity().startActivity(intent);
            }
        });
        fragment_play = (RelativeLayout) root.findViewById(R.id.fragment_play);
        displayPlayButtonScene(root);
        //fragment_play.addView(playButtonDrawView);
        return root;
    }



    private void displayPlayButtonScene(View root){

        /*
        button_play_test = new Button(root.getContext());
        button_play_test.setWidth(constants_display.width / 20);
        button_play_test.setHeight(constants_display.height / 20);
        button_play_test.setBackgroundResource(R.drawable.button_menu);
        button_play_test.setX((float) (constants_display.width / 2));
        button_play_test.setY((float) (constants_display.height / 2));
        button_play_test.setText("");
        button_play_test.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        fragment_play.addView(button_play_test);


        //Efficiently load drawables into imageViews for Menu
        bitmap_Logo = constants_display.loadBitmapEfficiently(root.getContext(),
                getResources(),
                R.drawable.trademark,
                (int) (constants_display.width * 0.025),
                (int) (constants_display.height * 0.008));

        */

        final float logoX = (float)(constants_display.height * 0.8);
        final float logoY = (float)(constants_display.width * 0.020);

        iv_Logo = new ImageView(root.getContext());
        iv_Logo.setImageBitmap(bitmap_Logo);
        iv_Logo.setY(logoX);
        iv_Logo.setX(logoY);

        //
         fragment_play.addView(iv_Logo);

    }

    public class PlayButtonDrawView extends View {

        //onDraw
        Canvas canvas;
        float fishX, fishY;
        Paint paintCircle = new Paint();
        Paint paintLine = new Paint();


        public PlayButtonDrawView(Context context) {
            super(context);
            initMyView();
        }

        public void initMyView() {
            //Drawing Tools
            canvas = new Canvas();

            paintLine = new Paint();
            paintLine.setColor(Color.RED);
            paintLine.setStrokeWidth(3.25f);
            paintLine.setStyle(Paint.Style.STROKE);

        }

        @Override
        public void onDraw(Canvas canvas) {


            int yOffset = (Constants_Display.height / 2) - 60;
            int xOffset = (Constants_Display.width / 2) - 55;



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
        super.onResume();
        system_ui_manager.hideView();
        fragment_play.addView(playButtonDrawView);
    }

    @Override
    public void onPause(){
        super.onPause();
        fragment_play.removeView(playButtonDrawView);
    }
}
