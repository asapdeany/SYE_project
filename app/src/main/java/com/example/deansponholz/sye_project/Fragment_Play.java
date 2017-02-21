package com.example.deansponholz.sye_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;


import static android.R.attr.bitmap;

/**
 * Created by deansponholz on 1/19/17.
 */

//test bitmap animation

public class Fragment_Play extends Fragment {

    //Instance Data
    public SensorHandler sensorHandler = null;
    ImageView testGun0;
    ImageView testgun1;

    View decorView;
    Integer uiOptions;
    Bitmap bitmap0, bitmap1, bitmap2, bitmap3, bitmap4;




    RelativeLayout fragment_practice;
    Integer radiusValue;
    Button button_shoot;

    Runnable reloadAnimation, updateImageView;
    List<Bitmap> imgList = new ArrayList<Bitmap>();

    final int[] layers = new int[7];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_play, container, false);

        sensorHandler = new SensorHandler(root.getContext());

        decorView = getActivity().getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                // Note that system bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.
                    Log.d("bro", "help");
                    //decorView.setSystemUiVisibility(uiOptions);

                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.

                }
            }
        });

        fragment_practice = (RelativeLayout) root.findViewById(R.id.fragment_practice);
        final PracticeDrawView practiceDrawView = new PracticeDrawView(this.getActivity());
        fragment_practice.addView(practiceDrawView);


        loadBitmaps();
        testGun0 = (ImageView) root.findViewById(R.id.iv_gun);
        testGun0.setImageBitmap(bitmap0);
        //testgun1 = (ImageView) root.findViewById(R.id.iv_url);
        //Glide.with(this).load("http://goo.gl/gEgYUd").into(testgun1);

        button_shoot = (Button) root.findViewById(R.id.button_shoot);
        button_shoot.setOnTouchListener(new RepeatListener(0, 20, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (radiusValue > 2)
                    radiusValue = radiusValue - 2;
            }
        }));
        return root;
    }

    private void loadBitmaps(){

        //load ImageViews from Drawables
        bitmap0 = BitmapFactory.decodeResource(getResources(), R.drawable.image_gun_0);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.image_gun_1);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.image_gun_2);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.image_gun_3);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.image_gun_4);





        imgList.add(bitmap0);
        imgList.add(bitmap1);
        imgList.add(bitmap2);
        imgList.add(bitmap3);
        imgList.add(bitmap4);
        imgList.add(bitmap3);
        imgList.add(bitmap2);
        imgList.add(bitmap1);
        imgList.add(bitmap0);

    }

 

    public void reload(){
        final Handler handler = new Handler();



        reloadAnimation = new Runnable() {
            public void run() {

                for (int i = 0; i < imgList.size()-1; i++){
                    testGun0.setImageBitmap(imgList.get(i));
            }handler.postDelayed(reloadAnimation, 0);

        }
        };
    }

    public class PracticeDrawView extends View {

        Canvas canvas;
        Paint paint;

        public PracticeDrawView(Context context){
            super(context);
            initMyView();
        }
        public void initMyView(){
            canvas = new Canvas();
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(8.5f);
            paint.setStyle(Paint.Style.STROKE);
            radiusValue = 80;
        }


        @Override
        public void onDraw(Canvas canvas){
            canvas.drawCircle((float)(-sensorHandler.xPos*43 + 1280), (float) (sensorHandler.yPos*38 + 720), radiusValue, paint);
            testGun0.setX((float)(-sensorHandler.xPos*20 + (1280 - bitmap0.getWidth()/1.7)));
            invalidate();
        }
    }
    public class RepeatListener implements View.OnTouchListener {

        public Handler handler = new Handler();


        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable updateImageView = new Runnable() {

            int i = 0;
            public void run() {
                if (i <= imgList.size()-1){
                    testGun0.setImageBitmap(imgList.get(i));
                    i++;
                }
                if (i > imgList.size()-1){
                    i = 0;
                    return;
                }
                handler.postDelayed(this, 90);


            }
        };

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval after first click event
         * @param normalInterval The interval after second and subsequent click
         *       events
         * @param clickListener The OnClickListener, that will be called
         *       periodically
         */
        public RepeatListener(int initialInterval, int normalInterval,
                              View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacks(handlerRunnable);
                    handler.postDelayed(handlerRunnable, initialInterval);
                    downView = view;
                    downView.setPressed(true);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:


                    radiusValue = 80;

                    //handler.postDelayed(updateImageView, 0);
                    handler.post(updateImageView);

                    fragment_practice.setBackgroundColor(Color.WHITE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_practice.setBackgroundColor(getResources().getColor(R.color.test_purple));

                        }
                    },30);
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(handlerRunnable);
                    downView.setPressed(false);
                    downView = null;
                    return true;
            }

            return false;
        }

    }

    @Override
    public void onResume() {
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }
}
