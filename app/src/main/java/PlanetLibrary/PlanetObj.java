package PlanetLibrary;

import android.content.Context;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.BitmapHelper;

import java.io.IOException;

/**
 * Created by Ahmed, Artjem, Arndt on 20.05.2015.
 */
public class PlanetObj {

    public static String planetTexture = null;
    public static Context ctx = null;

    private static Object3D planet = null;
    private static Object3D hemisphere = null;
    private static float size;
    private static String name;


    public PlanetObj(Context ctx, String planetTexture, float size){

        this.planetTexture = planetTexture;
        this.ctx = ctx;
        this.size = size;
        this.name = planetTexture;
        loadTexture(planetTexture,"","");
        loadTexture(planetTexture,"N","n");

        /*Load the Texture for the Planet */
        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture + "N"), TextureInfo.MODE_MODULATE);

        try{
            /* Load planet.3ds and Create a 3D Object for the Planet
            *  and set Options for default view
            */
            planet = Object3D.mergeAll(Loader.load3DS(ctx.getResources().getAssets().open("planet.3ds"), this.size));
            planet.setScale(1);
            planet.rotateX(-(float) (Math.PI / 360)*180);
            planet.setCulling(true);
            planet.setTexture(ti);
            planet.setSpecularLighting(true);
            planet.compile();

            planet.strip();

        }catch (IOException e){

        }



    }

            /* Load Texture for Hemisphere
            *  Load planet.3ds and Create a 3D Object for the Hemisphere
            *  and set Options for default view
            *
            */
    public void addHemi(int alpha){

        loadTexture(planetTexture, "H","h");
        loadTexture(planetTexture, "HN", "hn");

        TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID(planetTexture+"H"));
        ti.add(TextureManager.getInstance().getTextureID(planetTexture + "HN"), TextureInfo.MODE_MODULATE);

        try{
            hemisphere = Object3D.mergeAll(Loader.load3DS(ctx.getResources().getAssets().open("planet.3ds"), this.size+0.05f));


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
       // world.addObject(this.hemisphere);
        this.hemisphere.translate(planet.getTransformedCenter());
    }



    //function to Load Texture from Folder
    public static void loadTexture(String pt,String en, String et){
        TextureManager.getInstance().addTexture(pt + en, new Texture(BitmapHelper.rescale(BitmapHelper.convert(ctx.getResources().getDrawable(ctx.getResources().getIdentifier(pt + et, "drawable", ctx.getPackageName()))), 512, 512), true));
        //Api ab 22
        //TextureManager.getInstance().addTexture(pt + en, new Texture(BitmapHelper.rescale(BitmapHelper.convert(ctx.getResources().getDrawable(ctx.getResources().getIdentifier(pt + et, "drawable", ctx.getPackageName()), ctx.getTheme())), 512, 512), true));
    }

    // Move the Planet
    public  void moveObj(float x,float y,float z){
        planet.translate(x,y,z);

    }
    // return the 3D Planet Object
    public Object3D getPlanetObj(){
        return this.planet;
    }

    //return the 3D Hemisphere Object
    public Object3D getHemiObj(){
        return this.hemisphere;
    }




}


