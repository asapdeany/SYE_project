package com.example.deansponholz.sye_project;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
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
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;

/**
 * Created by deansponholz on 4/29/17.
 */

public class GameActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener, IAccelerationListener{

    //Instance Data
    public static final int CAMERA_WIDTH = Constants_Display.width;
    public static final int CAMERA_HEIGHT = Constants_Display.height;
    System_UI_Manager system_ui_manager;
    private Scene m_Scene;

    //Physics
    protected PhysicsWorld mPhysicsWorld;
    FixtureDef objectFixtureDef;

    //Background
    private TextureRegion regionBackground;
    private BitmapTextureAtlas textureBackground;
    private SpriteBackground spriteBackground;

    //Gun
    private Double gunHeight, gunWidth;
    private BitmapTextureAtlas textureGun;
    private TiledTextureRegion regionGun;
    private AnimatedSprite spriteGun;

    //PlayerShot
    private Rectangle playerShot;


    //Crosshair
    private Double crosshairHeight, crosshairWidth;
    private BitmapTextureAtlas textureCrosshair;
    private TextureRegion regionCrosshair;
    private Sprite spriteCrosshair;
    private boolean isSpriteShrinking;


    //DISKS
    private BitmapTextureAtlas textureRedDisk;
    private TextureRegion regionRedDisk;
    private Sprite spriteRedDisk0, spriteRedDisk1, spriteRedDisk2;
    private float diskFireRate;

    /*
    private BitmapTextureAtlas textureBlueDisk;
    private TextureRegion regionBlueDisk;
    private Sprite spriteBlueDisk0, spriteBlueDisk1, spriteBlueDisk2;
    */

    ArrayList<Sprite> diskArrayList = new ArrayList<Sprite>();
    MoveModifier modifierDiskMovement0, modifierDiskMovement1, modifierDiskMovement2;

    //TIMERS
    private TimerHandler countDownTimerHandler;
    private TimerHandler releaseDiskTimerHandler;
    private TimerHandler bulletHideTimerHandler;


    //HUD
    private Text startTimerText, gameLevelText, currentScoreText;
    private BitmapTextureAtlas startTimerFontTexture, gameLevelFontTexture, currentScoreFontTexture;
    private Font countdownFont, levelFont, currentScoreFont;
    private int levelCount = 1;
    private int hitCount = 0;
    private int diskCount = 0;
    private int currentScore = 0;
    //This represents the sprite sheet(image) rows and columns
    //We have 3 Rows and 3 Columns
    private static int   SPR_COLUMN  = 3;
    private static int   SPR_ROWS  = 3;

    boolean didDiskFall = false;

    Body diskBody, roofBody, bottomBody, leftBody, rightBody, bulletBody;

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

        gameLevelFontTexture = new BitmapTextureAtlas(getTextureManager(), 2048, 2048);
        levelFont = FontFactory.createFromAsset(this.getFontManager(),
                gameLevelFontTexture, this.getAssets(),
                "Droid.ttf", 100, true,
                android.graphics.Color.RED);
        gameLevelFontTexture.load();
        levelFont.load();

        currentScoreFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 2048, 2048);
        currentScoreFont = FontFactory.createFromAsset(this.getFontManager(), currentScoreFontTexture, this.getAssets(), "Droid.ttf", 100, true, Color.RED);
        currentScoreFontTexture.load();
        currentScoreFont.load();


        startTimerText = new Text(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, countdownFont, "3", this.getVertexBufferObjectManager());
        startTimerText.setPosition((CAMERA_WIDTH/2) - startTimerText.getWidth()/2, (CAMERA_HEIGHT/2) - startTimerText.getHeight()/2);
        gameLevelText = new Text(0, 0, levelFont, ("Game Level: " + levelCount), this.getVertexBufferObjectManager());
        currentScoreText = new Text((float) (CAMERA_WIDTH * 0.65), 0,currentScoreFont, ("Current Score: " + currentScore), this.getVertexBufferObjectManager());


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


        //playerShot = new Rectangle(60, 60, 60, 60, getVertexBufferObjectManager());

        spriteRedDisk0 = new Sprite(0, CAMERA_HEIGHT/2, 230, 65, regionRedDisk, getVertexBufferObjectManager());


        //spriteRedDisk1 = new Sprite(300, 600, 230, 65, regionRedDisk, getVertexBufferObjectManager());
        //spriteRedDisk2 = new Sprite(300, 800, 230, 65, regionRedDisk, getVertexBufferObjectManager());


        //diskArrayList.add(spriteRedDisk0);
        //diskArrayList.add(spriteRedDisk1);
        //diskArrayList.add(spriteRedDisk2);

        //Random random = new Random();



        //Timers
        countDownTimerHandler = new TimerHandler(1, true,new ITimerCallback() {

            int secondCount = 3;

            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {

                secondCount--;
                startTimerText.setText(Integer.toString(secondCount));


                if (secondCount <=0){
                    m_Scene.unregisterUpdateHandler(countDownTimerHandler);
                    m_Scene.detachChild(startTimerText);
                    startGame();
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
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false){
            @Override
            public void onUpdate(float pSecondsElapsed){
                super.onUpdate(pSecondsElapsed);

                if (spriteRedDisk0.getX() > CAMERA_WIDTH){
                    createDisk();
                    shootDisk();
                }
                if (didDiskFall){
                    createDisk();
                    shootDisk();
                    didDiskFall = false;
                }
            }
        };
        objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);

        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        bottomBody = PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        bottomBody.setUserData("ground");
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(ground, bottomBody, true, true));

        final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        roofBody = PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
        roofBody.setUserData("roof");
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(roof, roofBody, true, true));

        final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
        leftBody = PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
        leftBody.setUserData("left");
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(left, leftBody, true, true));

        final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
        rightBody = PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);
        rightBody.setUserData("right");
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(right, rightBody, true, true));



        //m_Scene.attachChild(spriteRedDisk0);


        m_Scene.attachChild(ground);
        m_Scene.attachChild(roof);
        m_Scene.attachChild(left);
        m_Scene.attachChild(right);


        m_Scene.attachChild(spriteGun);
        m_Scene.attachChild(spriteCrosshair);



        m_Scene.attachChild(startTimerText);
        m_Scene.attachChild(gameLevelText);
        m_Scene.attachChild(currentScoreText);
        m_Scene.registerUpdateHandler(countDownTimerHandler);



        mPhysicsWorld.setContactListener(createContactListener());
        m_Scene.registerUpdateHandler(mPhysicsWorld);

        return m_Scene;

    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        int myEventAction = pSceneTouchEvent.getAction();

        switch (myEventAction){
            case MotionEvent.ACTION_DOWN:

                spriteCrosshair.registerEntityModifier(new ScaleModifier(1.45f, 1, 0));

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                spriteCrosshair.clearEntityModifiers();
                //THIS MADE MY LIFE VERY EASY
                if (spriteGun.isAnimationRunning()){
                    break;
                }
                else{
                    spriteCrosshair.setScale(1.0f, 1.0f);
                    spriteGun.animate(100, false);


                    playerShot = new Rectangle(spriteCrosshair.getX() + (spriteCrosshair.getWidth()/2),
                            spriteCrosshair.getY() + (spriteCrosshair.getHeight() / 2),
                            60, 60, getVertexBufferObjectManager());


                    bulletBody = PhysicsFactory.createBoxBody(mPhysicsWorld, playerShot, BodyDef.BodyType.StaticBody, objectFixtureDef);
                    bulletBody.setUserData("bullet");
                    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(playerShot, bulletBody, true, true));

                    //playerShot.setVisible(false);

                    pScene.attachChild(playerShot);

                    try {
                        checkShot(spriteRedDisk0, playerShot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }
        }
        return false;
    }

    public void startGame(){


        diskFireRate = 10;


        createDisk();
        shootDisk();


    }

    public void createDisk(){

        final FixtureDef diskFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
        spriteRedDisk0 = new Sprite(0, CAMERA_HEIGHT/2, 230, 65, regionRedDisk, getVertexBufferObjectManager());
        diskBody = PhysicsFactory.createBoxBody(mPhysicsWorld, spriteRedDisk0, BodyDef.BodyType.KinematicBody, diskFixtureDef);
        diskBody.setUserData("disk");
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteRedDisk0, diskBody, true, true));





    }

    public void shootDisk(){

        diskCount++;

        if (diskCount <= 3){
            m_Scene.attachChild(spriteRedDisk0);
            diskBody.setLinearVelocity(diskFireRate, 0);
        }
        else {

            if (hitCount == 3){
                levelCount++;
                gameLevelText.setText("Game Level: " + levelCount);
                diskFireRate = diskFireRate + 5;
                hitCount = 0;
                diskCount = 0;
                shootDisk();
            }
            else{
                gameLevelText.setText("Gameover");
            }

        }
    }


    private ContactListener createContactListener(){

        ContactListener contactListener = new ContactListener() {

            @Override
            public void beginContact(Contact contact) {

                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x2.getBody().getUserData().equals("ground")&&x1.getBody().getUserData().equals("disk"))
                {
                    mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(spriteRedDisk0));
                    diskBody.setActive(false);

                    mPhysicsWorld.destroyBody(diskBody);

                    spriteRedDisk0.detachSelf();

                    didDiskFall = true;




                }
                else if (x2.getBody().getUserData().equals("disk")&&x1.getBody().getUserData().equals("ground")){

                    mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(spriteRedDisk0));
                    diskBody.setActive(false);

                    mPhysicsWorld.destroyBody(diskBody);

                    spriteRedDisk0.detachSelf();

                    didDiskFall = true;



                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

        };

        return contactListener;
    }


    public boolean checkShot(Sprite diskSprite , Rectangle playerShot) throws Exception{


        float diffX = Math.abs( (diskSprite.getX() +  diskSprite.getWidth()/2 )-
                (playerShot.getX() + playerShot.getWidth()/2 ));
        float diffY = Math.abs( (diskSprite.getY() +  diskSprite.getHeight()/2 )-
                (playerShot.getY() + playerShot.getHeight()/2 ));

        if(diffX < (diskSprite.getWidth()/2 + playerShot.getWidth()/3) && diffY < (diskSprite.getHeight()/2 + playerShot.getHeight()/3)){
            Log.d("HIT", "HIT");
            hitCount++;
            mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(playerShot));
            bulletBody.setActive(false);
            mPhysicsWorld.destroyBody(bulletBody);
            playerShot.detachSelf();


            diskBody.setAngularVelocity(10);
            diskBody.setLinearVelocity(0, 20);

            diskBody.setType(BodyDef.BodyType.DynamicBody);


            return true;
        }else {

            Log.d("Miss", "Miss");
            mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(playerShot));
            bulletBody.setActive(false);
            mPhysicsWorld.destroyBody(bulletBody);
            playerShot.detachSelf();
            return false;
        }
    }


    @Override
    protected synchronized void onResume() {
        system_ui_manager.hideView();
        this.enableAccelerationSensor(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEngine.stop();
        this.disableAccelerationSensor();
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {

        final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
        this.mPhysicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
        spriteCrosshair.setPosition((float)-SensorHandler.xPos * 43 + (CAMERA_WIDTH/2 -(textureCrosshair.getWidth()/2)),
                (float)(SensorHandler.yPos * 43 + (CAMERA_HEIGHT/2) - Constants_Display.difference));

        //spriteGun.setX((float)-SensorHandler.xPos * 20);
        spriteGun.setX((float)-SensorHandler.xPos * 22  + ((CAMERA_WIDTH/2) - (textureGun.getWidth()/2)));
    }
}
