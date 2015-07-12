/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package GL;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;

import UserGLInteraction.CamerObj;
import com.example.ahmed.planet.R;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import PlanetLibrary.PlanetManager;
import UserInterfaceInteraction.SpinnerListener;
import loadScreenLibrary.BackgroundSplashTask;
/**
 * Created by Ahmed, Artjem, Arndt on 16.05.2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    private long time = System.currentTimeMillis();
    private static Context myContext;
    private boolean master = false;
    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor back = new RGBColor(255, 255, 255);

    private float camDistance;


    private Object3D space = null;
    private CamerObj cam = null;
    private int fps = 0;

    private Light sunlight = null;
    private PointF touchPoint = null;

    private PlanetManager pm;

    private SpinnerListener sl;

    private BackgroundSplashTask asyncTask;

    private boolean planetChanged;
    private boolean transitionOut;
    private boolean transitionLook;
    private boolean transitionIn;
    
    public MyGLRenderer(Context context, SpinnerListener spinnerListener, BackgroundSplashTask async){
        myContext=context;
        sl = spinnerListener;
        asyncTask = async;
    }
    @Override
    public void onSurfaceCreated(GL10 gl,EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (fb != null) {
            fb.dispose();
        }
        fb = new FrameBuffer(gl, width, height);


        Logger.log("Master ------------------------------------------------------------------- "+master);
        if (master == false) {


            camDistance = 30;
            touchPoint = new PointF();
            world = new World();

            //setting the Lights
            sunlight = new Light(world);
            sunlight.enable();
            sunlight.setIntensity(127, 127, 127);
            sunlight.setPosition(SimpleVector.create(0, 0, 0));
            world.setAmbientLight(30, 30, 30);
            world.setClippingPlanes(0,5000000);


            //load texture for the background/skyline
            TextureManager.getInstance().addTexture("skyline", new Texture(BitmapHelper.rescale(BitmapHelper.convert(myContext.getResources().getDrawable(R.drawable.skyline)), 1024, 1024)));

            //load Obj for the Skyline
            try{
                space = Object3D.mergeAll(Loader.load3DS(myContext.getResources().getAssets().open("planet.3ds"), 400000));
                space.setTexture("skyline");
                space.invert();
                space.rotateX(-(float) Math.PI / 2);

                space.strip();
                space.setAdditionalColor(150,150,150);
                space.build();


            }catch (IOException e){

            }
            world.addObject(space);
            //1:4 KM
            // creates the Planets from JSON
            pm = new PlanetManager(myContext, world, asyncTask);

            cam = new CamerObj(world);


            MemoryHelper.compact();

            transitionOut = true;
            transitionIn = false;
            transitionLook = false;
            master = true;


        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        planetChanged = sl.getPlanetChanged();

        if (!planetChanged){
            pm.onRender(); // rotates the Planets
            CamerObj.setCameraDistance(camDistance + pm.getPlanetDiamByIndex(sl.getSpinnerItemID())); //zooming in and out

            CamerObj.onRendering(touchPoint.x, touchPoint.y); //give the Input from the screen
            CamerObj.focusonPlanet(pm.getPlanetOBJFromIndex(sl.getSpinnerItemID())); // focus on Moving Planet
            CamerObj.setRotateCenter(pm.getPlanetOBJFromIndex(sl.getSpinnerItemID())); // set rotate center on moving planet

        } else if (planetChanged) {
            if (transitionOut){
                CamerObj.planetChangeOut(pm.getPlanetOBJFromIndex(sl.getFormerSpinnerItemID()), this);
            } else if(transitionLook){
                CamerObj.planetChangeLook(pm.getPlanetOBJFromIndex(sl.getSpinnerItemID()), this);
            } else if(transitionIn){
                CamerObj.planetChangeIn(pm.getPlanetOBJFromIndex(sl.getSpinnerItemID()), this);
            } else if (!transitionOut && !transitionLook && !transitionIn) {
                transitionOut = true;
                sl.setPlanetChangedFalse();
            }
        }

        fb.clear(back);
        world.renderScene(fb);
        world.draw(fb);
        fb.display();
        fps++;

        if (System.currentTimeMillis() - time >= 1000) {
            // Logger.log(fps + "fps");
            fps = 0;
            time = System.currentTimeMillis();
        }

        if(asyncTask.getCounter() == 9){
            asyncTask.setCounter(asyncTask.getCounter() + 1);
            asyncTask.setProgressDialogHidden();
        }
    }

    //Gets the boolean master .
    public boolean getMaster(){
        return master;
    }
    //Sets the boolean state for the transition out.
    public void setTransitionOut(boolean state){ transitionOut = state; }
    //Sets the boolean state for the transition look.
    public void setTransitionLook(boolean state){ transitionLook = state; }
    //Sets the boolean state for the transition in.
    public void setTransitionIn(boolean state){ transitionIn = state; }
    //Sets the Touchpoint from camera for the Rotation .
    public void setTouchPoint(PointF point){ touchPoint = point; }
    //Sets the camera distance for zoom in or out .
    public void setCameraDistance(float d){camDistance = d;}
}