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

////////

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

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



    Constants_Display constants_display;
    System_UI_Manager system_ui_manager;

    RelativeLayout fragment_play;
    Integer radiusValue;
    Button button_shoot;

    Runnable reloadAnimation, updateImageView;
    List<Bitmap> imgList = new ArrayList<Bitmap>();

    final int[] layers = new int[7];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_play, container, false);

        //calculate screen dimensions
        constants_display = new Constants_Display(root.getContext());
        sensorHandler = new SensorHandler(root.getContext());
        system_ui_manager = new System_UI_Manager(getActivity());


        //fragment
        fragment_play = (RelativeLayout) root.findViewById(R.id.fragment_play);
        final PracticeDrawView practiceDrawView = new PracticeDrawView(this.getActivity());
        fragment_play.addView(practiceDrawView);



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
            radiusValue = (int) (constants_display.height * 0.075);
        }


        @Override
        public void onDraw(Canvas canvas){
            canvas.drawCircle((float)(-sensorHandler.xPos*43 + (constants_display.width)/2), (float) (sensorHandler.yPos*38 + ((constants_display.height)/2) - Constants_Display.difference), radiusValue, paint);
            testGun0.setX((float)(-sensorHandler.xPos*20 + (constants_display.width)/2) - (bitmap0.getWidth()/2));
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

                button_shoot.setVisibility(View.GONE);
                if (i <= imgList.size()-1){
                    testGun0.setImageBitmap(imgList.get(i));
                    i++;
                }
                if (i > imgList.size()-1){
                    i = 0;
                    button_shoot.setVisibility(View.VISIBLE);
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


                    radiusValue = (int) (constants_display.height * 0.075);

                    //handler.postDelayed(updateImageView, 0);
                    handler.post(updateImageView);

                    fragment_play.setBackgroundColor(Color.RED);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_play.setBackground(getResources().getDrawable(R.drawable.fishery_android));

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
        system_ui_manager.decorView.setSystemUiVisibility(system_ui_manager.uiOptions);
        //decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }
}
