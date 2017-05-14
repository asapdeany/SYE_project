package com.example.deansponholz.sye_project;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.LayoutGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by deansponholz on 4/2/17.
 */

public class Physics_Example extends LayoutGameActivity implements IAccelerationListener, IOnSceneTouchListener {
    // ===========================================================
    // Constants https://github.com/m5/andengine-pixel-perfect
    // ===========================================================

    protected static final int CAMERA_WIDTH = 731;
    protected static final int CAMERA_HEIGHT = 411;

    // ===========================================================
    // Fields
    // ===========================================================

    private BitmapTextureAtlas mBitmapTextureAtlas;

    private Scene mScene;

    protected ITiledTextureRegion mBoxFaceTextureRegion;
    protected ITiledTextureRegion mCircleFaceTextureRegion;

    protected PhysicsWorld mPhysicsWorld;

    private TextureRegion mBgTexture;
    private BitmapTextureAtlas mBackgroundTexture;

    private int mFaceCount = 0;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();

        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

    }

    @Override
    protected void onSetContentView() {
        super.setContentView(this.getLayoutID());

        this.mRenderSurfaceView = (RenderSurfaceView) this.findViewById(this.getRenderSurfaceViewID());

        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
        //his.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.BILINEAR);

        //this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "pidgey_0.png", 0, 0);
        this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "pidgey_0.png", 0, 0, 1, 1); // 64x32
        //this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32


        this.mBackgroundTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024);
        mBgTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "fishery.jpg", 0, 0);

        this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);

        this.mBitmapTextureAtlas.load();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        this.mEngine.registerUpdateHandler(new FPSLogger());


        this.mScene = new Scene();


        final int centerX = (int) (CAMERA_WIDTH - mBgTexture.getWidth()) / 2;
        final int centerY = (int) (CAMERA_HEIGHT - mBgTexture.getHeight()) / 2;
        SpriteBackground bg = new SpriteBackground(new Sprite(centerX, centerY, mBgTexture, getVertexBufferObjectManager()));
        this.mScene.setBackground(bg);
        //this.mScene.setBackground(new Background(255, 255, 255));
        this.mScene.setOnSceneTouchListener(this);

        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);



        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
        final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

        this.mScene.attachChild(ground);
        this.mScene.attachChild(roof);
        this.mScene.attachChild(left);
        this.mScene.attachChild(right);

        this.mScene.registerUpdateHandler(this.mPhysicsWorld);

    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {

    }



    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        if(this.mPhysicsWorld != null) {
            if(pSceneTouchEvent.isActionDown()) {
                this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                Log.d("touch", "touch");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(final AccelerationData pAccelerationData) {
        final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
        this.mPhysicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
    }

    @Override
    public void onResumeGame() {
        super.onResumeGame();

        this.enableAccelerationSensor(this);
    }

    @Override
    public void onPauseGame() {
        super.onPauseGame();

        this.disableAccelerationSensor();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void addFace(final float pX, final float pY) {
        this.mFaceCount++;
        Debug.d("Faces: " + this.mFaceCount);

        final AnimatedSprite face;
        final Body body;

        final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

        if(this.mFaceCount % 2 == 0) {
            //face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
            face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
            //face.setScale(MathUtils.random(0.5f, 1.25f));
            body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, face, BodyType.KinematicBody, objectFixtureDef);

        } else {

            face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
            //face.setScale(MathUtils.random(0.5f, 1.25f));

            body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, objectFixtureDef);
        }

        //face.animate(200);

        this.mScene.attachChild(face);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_play;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.xml_rendersurfaceview;

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
