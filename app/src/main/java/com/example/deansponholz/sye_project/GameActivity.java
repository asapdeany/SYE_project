package com.example.deansponholz.sye_project;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import java.math.BigDecimal;
import java.security.spec.EllipticCurve;
import java.util.ArrayList;

/**
 * Created by deansponholz on 4/29/17.
 */

public class GameActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener, SensorEventListener{

    //Instance Data
    public static final int CAMERA_WIDTH = Constants_Display.width;
    public static final int CAMERA_HEIGHT = Constants_Display.height;
    System_UI_Manager system_ui_manager;
    private Scene m_Scene;

    //Physics
    protected PhysicsWorld mPhysicsWorld;

    //Background
    private TextureRegion regionBackground;
    private BitmapTextureAtlas textureBackground;
    private SpriteBackground spriteBackground;

    //Gun
    private Double gunHeight, gunWidth;
    private BitmapTextureAtlas textureGun;
    private TiledTextureRegion regionGun;
    private AnimatedSprite spriteGun;

    //Crosshair
    private Double crosshairHeight, crosshairWidth;
    private BitmapTextureAtlas textureCrosshair;
    private TextureRegion regionCrosshair;
    private Sprite spriteCrosshair;

    //SENSORS
    private Sensor sensor;
    private SensorManager mSensorManager;
    private SensorHandler sensorHandler;

    //DISKS
    private BitmapTextureAtlas textureRedDisk;
    private TextureRegion regionRedDisk;
    private Sprite spriteRedDisk0, spriteRedDisk1, spriteRedDisk2;

    private BitmapTextureAtlas textureBlueDisk;
    private TextureRegion regionBlueDisk;
    private Sprite spriteBlueDisk0, spriteBlueDisk1, spriteBlueDisk2;

    ArrayList<Sprite> diskArrayList = new ArrayList<Sprite>();

    //TIMERS
    private TimerHandler countDownTimerHandler;
    private TimerHandler releaseDiskTimerHandler;
    private float releaseDiskTimeRate = 3;


    //HUD
    private Text startTimerText, gameLevelText;
    private BitmapTextureAtlas startTimerFontTexture, gameLevelFontTexture;
    private Font countdownFont, levelFont;
    private int levelCount = 1;
    private int hitCount = 0;
    //This represents the sprite sheet(image) rows and columns
    //We have 3 Rows and 3 Columns
    private static int   SPR_COLUMN  = 3;
    private static int   SPR_ROWS  = 3;

    @Override
    public EngineOptions onCreateEngineOptions() {

        //2560 x 1440
        Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        //Fills the whole screen with your scene
        //IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
                camera);

        engineOptions.getAudioOptions().setNeedsMusic(false).setNeedsSound(false);

        //Device will not enter sleep mode due to inactivity
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        system_ui_manager = new System_UI_Manager(this);
        return engineOptions;

    }

    @Override
    protected void onCreateResources() {

        //SET ASSET PATH
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        FontFactory.setAssetBasePath("font/");

        //HUD
        startTimerFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
        countdownFont = FontFactory.createFromAsset(this.getFontManager(),
                startTimerFontTexture, this.getAssets(),
                "Droid.ttf", 200, true,
                android.graphics.Color.RED);
        startTimerFontTexture.load();
        countdownFont.load();

        gameLevelFontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256);
        levelFont = FontFactory.createFromAsset(this.getFontManager(),
                gameLevelFontTexture, this.getAssets(),
                "Droid.ttf", 100, true,
                android.graphics.Color.RED);
        gameLevelFontTexture.load();
        levelFont.load();


        startTimerText = new Text(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, countdownFont, "3", this.getVertexBufferObjectManager());
        startTimerText.setPosition((CAMERA_WIDTH/2) - startTimerText.getWidth()/2, (CAMERA_HEIGHT/2) - startTimerText.getHeight()/2);

        gameLevelText = new Text(0, 0, levelFont, ("Game Level: " + levelCount), this.getVertexBufferObjectManager());






        //BACKGROUND
        textureBackground = new BitmapTextureAtlas(getTextureManager(), 900, 654);
        regionBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.textureBackground, this, "fishery.jpg", 0, 0);
        textureBackground.load();
        spriteBackground = new SpriteBackground(new Sprite(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, regionBackground, getVertexBufferObjectManager()));

        //GUN
        gunWidth = Constants_Display.width * (0.2);
        gunHeight = Constants_Display.height * (0.3);
        textureGun = new BitmapTextureAtlas(this.getTextureManager(), 650, 510, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        regionGun = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureGun, this.getAssets(),"guns_three_x_three_large.png",
                0, 0,
                SPR_COLUMN, SPR_ROWS);
        textureGun.load();
        spriteGun = new AnimatedSprite((
                (CAMERA_WIDTH/2) - (gunWidth.intValue()/2)), // X position
                (1490 - gunHeight.intValue()), // Y position
                gunWidth.intValue(), gunHeight.intValue(), //size
                regionGun,
                getVertexBufferObjectManager());

        //CROSSHAIR
        textureCrosshair = new BitmapTextureAtlas(getTextureManager(), 225, 225);
        regionCrosshair = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.textureCrosshair, this, "crosshair.png", 0, 0);
        textureCrosshair.load();
        spriteCrosshair = new Sprite(CAMERA_WIDTH/2 - (textureCrosshair.getWidth()/2), CAMERA_HEIGHT/2, regionCrosshair, getVertexBufferObjectManager());
        //spriteCrosshair = new Sprite(CAMERA_WIDTH/2, CAMERA_HEIGHT/2,

        //DISKS
        textureRedDisk = new BitmapTextureAtlas(getTextureManager(), 102, 28);
        //textureBlueDisk = new BitmapTextureAtlas(getTextureManager(), 102, 28);
        regionRedDisk = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.textureRedDisk, this, "disk_red.png", 0, 0);
        //regionBlueDisk = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.textureBlueDisk, this, "disk_blue.png", 0, 0);
        textureRedDisk.load();
        //textureBlueDisk.load();

        spriteRedDisk0 = new Sprite(300, 400, 230, 65, regionRedDisk, getVertexBufferObjectManager());
        spriteRedDisk1 = new Sprite(300, 600, 230, 65, regionRedDisk, getVertexBufferObjectManager());
        spriteRedDisk2 = new Sprite(300, 800, 230, 65, regionRedDisk, getVertexBufferObjectManager());


        diskArrayList.add(spriteRedDisk0);
        diskArrayList.add(spriteRedDisk1);
        diskArrayList.add(spriteRedDisk2);




        //Timers
        countDownTimerHandler = new TimerHandler(1, true,new ITimerCallback() {

            int secondCount = 3;

            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {

                secondCount--;
                startTimerText.setText(Integer.toString(secondCount));

                if (secondCount == 1) {
                    startGame();
                }

                if (secondCount <=0){
                    m_Scene.unregisterUpdateHandler(countDownTimerHandler);
                    m_Scene.detachChild(startTimerText);
                    //startGame

                }
            }
        });

        releaseDiskTimerHandler = new TimerHandler(releaseDiskTimeRate, true,new ITimerCallback() {

            int diskCount = 3;

            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {

                m_Scene.attachChild(diskArrayList.get(diskCount-1));
                Log.d("bro", "newShot");
                diskCount--;



                if (diskCount <=0){
                    Log.d("bro", "TIMEROVER");
                    m_Scene.unregisterUpdateHandler(releaseDiskTimerHandler);

                    //startGame
                }
            }
        });


    }

    @Override
    protected Scene onCreateScene() {

        //SET SCENE
        m_Scene = new Scene();
        m_Scene.setBackground(spriteBackground);
        m_Scene.setOnSceneTouchListener(this);

        //PHYSICS WORLD
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
        final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);


        m_Scene.attachChild(ground);
        m_Scene.attachChild(roof);
        m_Scene.attachChild(left);
        m_Scene.attachChild(right);


        m_Scene.attachChild(spriteGun);
        m_Scene.attachChild(spriteCrosshair);


        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        sensorHandler = new SensorHandler(this);

        m_Scene.attachChild(startTimerText);
        m_Scene.attachChild(gameLevelText);
        m_Scene.registerUpdateHandler(countDownTimerHandler);

        return m_Scene;

    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        int myEventAction = pSceneTouchEvent.getAction();

        switch (myEventAction){
            case MotionEvent.ACTION_DOWN:
                spriteCrosshair.registerEntityModifier(new ScaleModifier(1.45f, 1, 0));
                //spriteCrosshair.setScale(spriteCrosshair.getScaleX()/1.05f, spriteCrosshair.getScaleY()/1.05f);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:


                //THIS MADE MY LIFE VERY EASY
                if (spriteGun.isAnimationRunning()){
                    break;
                }
                else{
                    final Rectangle playerShot = new Rectangle(spriteCrosshair.getX() + (spriteCrosshair.getWidth()/2),
                            spriteCrosshair.getY() + (spriteCrosshair.getHeight() / 2),
                            60, 60, getVertexBufferObjectManager());
                    playerShot.setVisible(false);
                    pScene.attachChild(playerShot);
                    try {
                        isCollides(spriteRedDisk0, playerShot);
                        isCollides(spriteRedDisk1, playerShot);
                        isCollides(spriteRedDisk2, playerShot);
                        pScene.detachChild(playerShot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    spriteGun.animate(100, false);
                    spriteCrosshair.clearEntityModifiers();
                    spriteCrosshair.setScale(1.0f, 1.0f);
                    break;
                }
        }

        return false;
    }

    public void startGame(){
        m_Scene.registerUpdateHandler(releaseDiskTimerHandler);

    }

    public boolean isCollides(Sprite diskSprite ,Rectangle playerShot) throws Exception{


        float diffX = Math.abs( (diskSprite.getX() +  diskSprite.getWidth()/2 )-
                (playerShot.getX() + playerShot.getWidth()/2 ));
        float diffY = Math.abs( (diskSprite.getY() +  diskSprite.getHeight()/2 )-
                (playerShot.getY() + playerShot.getHeight()/2 ));

        if(diffX < (diskSprite.getWidth()/2 + playerShot.getWidth()/3)
                && diffY < (diskSprite.getHeight()/2 + playerShot.getHeight()/3)){

            m_Scene.detachChild(diskSprite);
            hitCount++;
            Log.d("HIT", "HIT");
            return true;
        }else
            Log.d("Miss", "Miss");
            return false;
    }


    public void initListeners(){

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //MUST PUT ALL THIS MATH IN THE SENSOR HANDLER
        //ROOKIE FUCKING CODE
        spriteCrosshair.setPosition((float)-sensorHandler.xPos * 43 + (CAMERA_WIDTH/2 -(textureCrosshair.getWidth()/2)),
                (float)(sensorHandler.yPos * 43 + (CAMERA_HEIGHT/2) - Constants_Display.difference));

        spriteGun.setX((float)-sensorHandler.xPos * 20 + ((CAMERA_WIDTH/2) - (gunWidth.intValue()/2)));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected synchronized void onResume() {
        system_ui_manager.hideView();
        super.onResume();
    }
}
