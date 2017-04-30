package com.example.deansponholz.sye_project;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by deansponholz on 4/29/17.
 */

public class ResourceManager {

    //single instance is created only
    private static final ResourceManager INSTANCE = new ResourceManager();

    //common objects
    public GameActivity activity;
    public Engine engine;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    //game textures
    //TILED is for animations
    public ITiledTextureRegion playerGunTextureRegion;
    public ITiledTextureRegion playerTargetTextureRegion;
    //
    public ITextureRegion platformTextureRegion;
    public ITextureRegion cloud1TextureRegion;
    public ITextureRegion cloud2TextureRegion;

    private BuildableBitmapTextureAtlas gameTextureAtlas;


    //constructor is private to ensure nobody can call it from outside
    private ResourceManager(){

    }

    public void loadGameGraphics(){
        //8888 - 32 bit textrues and the highest quality
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
                1024, 512,
                BitmapTextureFormat.RGBA_8888,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        playerGunTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,
                activity.getAssets(),
                "image_gun_0.png", 3, 1);
        playerTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,
                activity.getAssets(), "pidgey_0.png", 1, 2);



    }

    public void create(GameActivity activity, Engine engine, Camera camera, VertexBufferObjectManager vbom) {
        //this is our game activity
        this.activity = activity;

        //to manipulate engine itself
        this.engine = engine;
        //to manipulate camera object
        this.camera = camera;
        //VERTEX BUFFER OBJECT is used to upload vertex data
        this.vbom = vbom;
    }

    public static ResourceManager getInstance(){
        return INSTANCE;
    }


}
