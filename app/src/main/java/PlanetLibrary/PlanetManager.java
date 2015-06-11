package PlanetLibrary;

import android.content.Context;


import com.threed.jpct.Object3D;
import com.threed.jpct.World;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Artjem on 28.05.2015.
 */
public class PlanetManager{

    JSONObject planetInfo;
    JSONArray planetsinfoArray;
    Context ctx;
    World world;
    ArrayList<Object3D> planets;
    PlanetObj planet;

    public PlanetManager(Context ctx, World w) {

        this.ctx = ctx;
        this.world = w;
        planets = new ArrayList<>();
        try {
            planetInfo = new JSONObject(loadJSONFromAsset());
            planetsinfoArray = planetInfo.getJSONArray("Planets");

            for(int i = 0; i < planetsinfoArray.length();i++){
                String planetName = planetsinfoArray.getJSONObject(i).getString("name");
                float posx = (float)planetsinfoArray.getJSONObject(i).getDouble("posx");
                float posy = (float)planetsinfoArray.getJSONObject(i).getDouble("posy");
                float posz = (float)planetsinfoArray.getJSONObject(i).getDouble("posz");
                float diam = (float)planetsinfoArray.getJSONObject(i).getDouble("diameter");
                boolean hemitoADD = planetsinfoArray.getJSONObject(i).getBoolean("hemi");
                int addColor = planetsinfoArray.getJSONObject(i).getInt("addColor");


                planet =  new PlanetObj(ctx.getApplicationContext(),planetName , diam);
                planet.moveObj(posx, posy, posz);
                planet.setName(planetName);
                planet.getPlanetObj().setAdditionalColor(addColor,addColor,addColor);
                world.addObject(planet.getPlanetObj());
                if(hemitoADD == true) {
                    planet.addHemi(world, 6);
                }
                planets.add(i,planet.getPlanetObj().cloneObject());
            }




        }catch (JSONException jex){
        }
    }


    public Object3D getPlanetOBJFromIndex(int i){
        return planets.get(i);
    }







//thx @ http://stackoverflow.com/questions/24110619/android-catch-unhandled-exception-and-show-the-dialog
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = this.ctx.getAssets().open("planetinfo.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
