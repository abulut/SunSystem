package PlanetLibrary;

import android.content.Context;

import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import loadScreenLibrary.BackgroundSplashTask;

/**
 * Created by Ahmed, Artjem, Arndt on 28.05.2015.
 */
public class PlanetManager{

    JSONObject planetInfo;
    JSONArray planetsinfoArray;
    Context ctx;
    World world;
    ArrayList<Object3D> planets;
    ArrayList<Object3D> hemispheres;

    List<HashMap<String, Float>> orbitPosList;
    HashMap<String, Float> orbitPos;

    ArrayList<Float> planetsDiam;

    PlanetObj planet;
    ArrayList <SimpleVector> planetTranslation;


    private int counter=0;
    private BackgroundSplashTask asycnTask;

    ArrayList <Float> myOrbitAngles;
    ArrayList <Float> orbitSpeed;

    public PlanetManager(Context ctx, World w, BackgroundSplashTask async) {
        planets = new ArrayList<>();
        hemispheres = new ArrayList<>();
        orbitPos = new HashMap<String, Float>();
        orbitPosList = new ArrayList<HashMap<String, Float>>();
        planetsDiam = new ArrayList<Float>();


        planetTranslation = new ArrayList<>();
        myOrbitAngles = new ArrayList<>();
        orbitSpeed = new ArrayList<>();
        this.ctx = ctx;
        this.world = w;
        this.asycnTask = async;

        try {
            planetInfo = new JSONObject(loadJSONFromAsset());
            planetsinfoArray = planetInfo.getJSONArray("Planets");

            for(int i = 0; i < planetsinfoArray.length();i++){
                String planetName = planetsinfoArray.getJSONObject(i).getString("name");
                float posx = (float)planetsinfoArray.getJSONObject(i).getDouble("posx");
                float posy = (float)planetsinfoArray.getJSONObject(i).getDouble("posy");
                float posz = (float)planetsinfoArray.getJSONObject(i).getDouble("posz")*2f;
                planetsDiam.add(i,(float)planetsinfoArray.getJSONObject(i).getDouble("diameter"));
                float diam = planetsDiam.get(i) * 0.5f;
                boolean hemitoADD = planetsinfoArray.getJSONObject(i).getBoolean("hemi");
                int addColor = planetsinfoArray.getJSONObject(i).getInt("addColor");

                orbitPos.put("aphel", (float) planetsinfoArray.getJSONObject(i).getDouble("aphel") / 100);
                orbitPos.put("perihel", (float) planetsinfoArray.getJSONObject(i).getDouble("perihel") / 100);
                orbitPosList.add(i, orbitPos);
                orbitSpeed.add(i, (float) planetsinfoArray.getJSONObject(i).getDouble("rotateSpeed") / 10000);

                myOrbitAngles.add(i, 0f);


                planetTranslation.add(i,new SimpleVector(posx,posy,posz));

                planet =  new PlanetObj(ctx.getApplicationContext(),planetName , diam);
                planet.moveObj(planetTranslation.get(i).x, planetTranslation.get(i).y, planetTranslation.get(i).z);


                planet.getPlanetObj().setAdditionalColor(addColor, addColor, addColor);
                world.addObject(planet.getPlanetObj());



                if(hemitoADD == true) {
                    planet.addHemi(6);
                    planet.getHemiObj().addParent(planet.getPlanetObj());
                    world.addObject(planet.getHemiObj());
                }else{

                }

                hemispheres.add(i, planet.getHemiObj());
                planets.add(i, planet.getPlanetObj());


                counter+=1;
                asycnTask.setCounter(counter);

            }

            Logger.log("PLAnetCHILD --------"+hemispheres.get(3).getParents().length);


        }catch (JSONException jex){
        }

    }

    public Object3D getPlanetOBJFromIndex(int i){
        return planets.get(i);
    }
    public Object3D getHemispOBJFromIndex(int i) { return hemispheres.get(i); }
    public Float getPlanetDiamByIndex(int i){ return planetsDiam.get(i);}

    public void onRender(){


        //thx @ http://www.jpct.net/forum2/index.php/topic,3007.15.html
        for(int i = 0;i < planets.size();i++) {


            planets.get(i).rotateY(-0.05f);


            myOrbitAngles.set(i,myOrbitAngles.get(i) + orbitSpeed.get(i) );
          //  myOrbitAngle += myOrbitSpeed;
            if (myOrbitAngles.get(i) > Math.PI * 2)
                myOrbitAngles.set(i, myOrbitAngles.get(i)%(float)Math.PI *2);
            //    myOrbitAngles.get(i) %= Math.PI * 2;
            SimpleVector sv = new SimpleVector();

            //double rad = (myOrbitAngle * (Math.PI / 180)); // Converting Degrees To Radians
            double x = 0 - planetTranslation.get(i).z * (Math.cos(myOrbitAngles.get(i)) * orbitPosList.get(i).get("aphel"));
            double z = 0 - planetTranslation.get(i).z * (Math.sin(myOrbitAngles.get(i)) * orbitPosList.get(i).get("perihel"));

            sv.x = (float) x;
            sv.z = (float) z;
            sv.y = 0;


            planets.get(i).clearTranslation();
            planets.get(i).translate(sv);
            planets.get(i).rotateY(orbitSpeed.get(i));


        }
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
