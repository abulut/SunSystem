package PlanetLibrary;

import android.content.Context;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;

import java.io.IOException;

/**
 * Created by Artjem on 20.05.2015.
 */
public class PlanetObj extends Object3D {

    public static String planetTexture = null;
    public static Context ctx = null;

    private static Object3D planet = null;
    private static Object3D hemisphere = null;
    private static float size;
    private SimpleVector realpos = new SimpleVector();
    public PlanetObj(Context ctx, String planetTexture, float size){

        super(planet);

        this.planetTexture = planetTexture;
        this.ctx = ctx;
        this.size = size;
        loadTexture(planetTexture,"","");
        loadTexture(planetTexture,"N","n");

        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture + "N"), TextureInfo.MODE_MODULATE);

        try{
            planet = Object3D.mergeAll(Loader.load3DS(ctx.getResources().getAssets().open("planet2.3ds"), this.size));
            planet.setScale(1);
            planet.rotateX(-(float) Math.PI / 2);
            planet.setCulling(true);

            planet.setTexture(ti);
            planet.setSpecularLighting(false);

            planet.setSpecularLighting(true);
            planet.compile();

            planet.strip();

        }catch (IOException e){

        }



    }


    public void addHemi(World world, int alpha){

        loadTexture(planetTexture, "H","h");
        loadTexture(planetTexture, "HN", "hn");

        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture+"H"));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture + "HN"), TextureInfo.MODE_MODULATE);

        try{
            hemisphere = Object3D.mergeAll(Loader.load3DS(ctx.getResources().getAssets().open("planet2.3ds"), this.size+0.05f));
            hemisphere.setScale(1);
            hemisphere.rotateX(-(float) Math.PI / 2);
            hemisphere.setCulling(true);


            hemisphere.setTexture(ti);
            hemisphere.setTransparency(alpha);

            hemisphere.setSpecularLighting(true);
            hemisphere.compile();

            hemisphere.strip();

        }catch (IOException e){

        }
        world.addObject(this.hemisphere);
        this.hemisphere.translate(planet.getTransformedCenter());
    }


    public static void loadTexture(String pt,String en, String et){
        TextureManager.getInstance().addTexture(pt + en, new Texture(BitmapHelper.rescale(BitmapHelper.convert(ctx.getResources().getDrawable(ctx.getResources().getIdentifier(pt + et, "drawable", ctx.getPackageName()), ctx.getTheme())), 512, 512), true));
    }


    public  void moveObj(float x,float y,float z){
        planet.translate(x,y,z);
        realpos = new SimpleVector(x,y,z);
    }


    public Object3D getPlanetObj(){
        return planet;
    }
    public Object3D getHemiObj(){
        return hemisphere;
    }

    public void rotateY(float speed){
        this.getPlanetObj().rotateY(speed);
    }

}


