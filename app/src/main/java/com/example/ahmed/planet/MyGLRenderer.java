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
package com.example.ahmed.planet;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShader;
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
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import PlanetLibrary.PlanetManager;
import PlanetLibrary.PlanetObj;
import UserInterfaceInteraction.SpinnerListener;
import loadScreenLibrary.BackgroundSplashTask;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    private long time = System.currentTimeMillis();
    private static Context myContext;
    private boolean master = false;
    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor back = new RGBColor(255, 255, 255);

    private float mAngleX;
    private float mAngleY;

    private float camDistance;

    private Object3D space = null;
    private CamerObj cam = null;
    private int fps = 0;

    private Light sunlight = null;
    private PointF touchPoint = null;
    private GLSLShader shader = null;

    private ArrayList<PlanetObj> planets = new ArrayList<>();
    private PlanetManager pm;

    private SpinnerListener sl;

    private float rotate= 0.0f;
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

            String vertex=Loader.loadTextFile(myContext.getResources().openRawResource(R.raw.vertexshader_offset));
            String fragment=Loader.loadTextFile(myContext.getResources().openRawResource(R.raw.fragmentshader_offset));

            shader=new GLSLShader(vertex, fragment);
            shader.setStaticUniform("colorMap", 0);
            shader.setStaticUniform("normalMap", 0);
            shader.setStaticUniform("invRadius", 10.0005f);





            camDistance = 30;
            touchPoint = new PointF();
            world = new World();

            sunlight = new Light(world);
            sunlight.enable();

            sunlight.setIntensity(127, 127, 127);
            sunlight.setPosition(SimpleVector.create(0, 0, 0));

            world.setAmbientLight(30, 30, 30);
            world.setClippingPlanes(0,5000000);



            TextureManager.getInstance().addTexture("skyline", new Texture(BitmapHelper.rescale(BitmapHelper.convert(myContext.getResources().getDrawable(R.drawable.skyline)), 1024, 1024)));


            try{
                space = Object3D.mergeAll(Loader.load3DS(myContext.getResources().getAssets().open("planet.3ds"), 400000));
                space.setTexture("skyline");
                space.invert();
                space.rotateX(-(float) Math.PI / 2);
 //               space.setCulling(false);

                space.strip();
                space.setAdditionalColor(150,150,150);
                space.build();


            }catch (IOException e){

            }
            world.addObject(space);
            //1:4 KM

            pm = new PlanetManager(myContext, world, asyncTask);
            Logger.log("HEMISPHERE  ---------------------    "+pm.getHemispOBJFromIndex(3));
/*
            planets.add(0, new PlanetObj(myContext.getApplicationContext(), "sun", 1391f));
            planets.get(0).getPlanetObj().setAdditionalColor(255, 255, 255);
            planets.get(0).getPlanetObj().setLighting(0);
            world.addObject(planets.get(0).getPlanetObj());




            planets.add(1, new PlanetObj(myContext.getApplicationContext(), "earth", 12.734f));
            planets.get(1).moveObj(0, 0, -150000);
            planets.get(1).getPlanetObj().setAdditionalColor(76, 76, 76);
            world.addObject(planets.get(1).getPlanetObj());
            planets.get(1).addHemi(world, 6);


*/

            cam = new CamerObj(world);
            SimpleVector sv = new SimpleVector();
            //sv.set(planet.getTransformedCenter());
            sv.y = 100;
            sv.z -= 100;


            MemoryHelper.compact();

            /*if (master == null) {
                Logger.log("Saving master Activity!");
                master = HelloWorld.this;
            }*/
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
            pm.onRender();
            CamerObj.setCameraDistance(camDistance);
            CamerObj.onRendering(touchPoint.x, touchPoint.y);
            CamerObj.focusonPlanet(pm.getPlanetOBJFromIndex(sl.getSpinnerItemID()));
            CamerObj.setRotateCenter(pm.getPlanetOBJFromIndex(sl.getSpinnerItemID()));
        //fixirt die Cam auf die Erden Hemisphere  nur zum test!!!
        //CamerObj.focusonPlanet(pm.getHemispOBJFromIndex(3));
        //CamerObj.setRotateCenter(pm.getHemispOBJFromIndex(3));

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
            asyncTask.setProgessDialogHidden();
        }
    }

    public boolean getMaster(){
        return master;
    }
    /**
     * Sets the Touchpoint from camera for the Rotation .
     */
    public void setTransitionOut(boolean state){ transitionOut = state; }
    public void setTransitionLook(boolean state){
        transitionLook = state;
    }
    public void setTransitionIn(boolean state){
        transitionIn = state;
    }

    public void setTouchPoint(PointF point){ touchPoint = point; }
    public void setCameraDistance(float d){camDistance = d;}
}