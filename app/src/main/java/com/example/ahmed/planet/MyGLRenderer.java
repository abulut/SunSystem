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

import PlanetLibrary.Hemisphere;
import PlanetLibrary.Planet;

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
    private RGBColor back = new RGBColor(50, 50, 100);

    private float mAngleX;
    private float mAngleY;

    private Object3D space = null;
    private CamerObj cam = null;
    private int fps = 0;

    private Light sun = null;
    private PointF touchPoint = null;
    private GLSLShader shader = null;

    private ArrayList<Planet> planets = new ArrayList<Planet>();
    private ArrayList<Hemisphere> hemispheres = new ArrayList<Hemisphere>();

    private float rotate= 0.0f;

    public MyGLRenderer(Context context){
        myContext=context;
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

        if (master == false) {

            touchPoint = new PointF();
            world = new World();

            sun = new Light(world);
            sun.enable();

            sun.setIntensity(127, 127, 127);
            sun.setPosition(SimpleVector.create(0, 0, 200));

            world.setAmbientLight(30, 30, 30);




            TextureManager.getInstance().addTexture("skyline", new Texture(BitmapHelper.rescale(BitmapHelper.convert(myContext.getResources().getDrawable(R.drawable.skyline)), 512, 512)));


            try{
                space = Object3D.mergeAll(Loader.load3DS(myContext.getResources().getAssets().open("planet.3ds"), 10));
                space.setTexture("skyline");
                space.setScale(30);
                space.setCulling(false);
                space.strip();
                space.setAdditionalColor(150,150,150);
                space.build();


            }catch (IOException e){

            }
            world.addObject(space);

            /*world = new World();
            world.setAmbientLight(20, 20, 20);

            sun = new Light(world);
            sun.setIntensity(250, 250, 250);
            */
            // Create a texture out of the icon...:-)
           // Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.icon)), 64, 64));
            //TextureManager.getInstance().addTexture("texture", texture);

            //planet = new PlanetObj(myContext, world, "earth");
            planets.add(new Planet(myContext.getApplicationContext(), "sun2",100f));
            planets.get(0).addColor(255, 255, 255);
            planets.get(0).getPlanetObj().setLighting(1);
            world.addObject(planets.get(0).getPlanetObj());






            planets.add(new Planet(myContext.getApplicationContext(), "earth",10f));
            planets.get(1).getPlanetObj().translate(0,0,-200);
            planets.get(1).getPlanetObj().setAdditionalColor(76,76,76);
            world.addObject(planets.get(1).getPlanetObj());

            hemispheres.add(new Hemisphere(myContext.getApplicationContext(), "earth",10.05f, 6));
            hemispheres.get(0).getPlanetObj().translate(0,0,-200);
            //   hemispheres.get(0).getPlanetObj().setAdditionalColor(127,127,0);

            world.addObject(hemispheres.get(0).getPlanetObj());


            String vertex=Loader.loadTextFile(myContext.getResources().openRawResource(R.raw.vertexshader_offset));
            String fragment=Loader.loadTextFile(myContext.getResources().openRawResource(R.raw.fragmentshader_offset));

            shader=new GLSLShader(vertex, fragment);
            shader.setStaticUniform("colorMap", 0);
            shader.setStaticUniform("normalMap", 0);
            shader.setStaticUniform("invRadius", 10.0005f);
            //planet.addColor(255, 255, 255);
            //planet.getPlanetObj().setLighting(1);


            //cube = Primitives.getCube(10);
            //cube.calcTextureWrapSpherical();
            //cube.setTexture("texture");
            //cube.strip();
            //cube.build();

            //world.addObject(planet);


            //world.addObject(planet.getPlanetObj());
            cam = new CamerObj(world);
            SimpleVector sv = new SimpleVector();
            //sv.set(planet.getTransformedCenter());
            sv.y = 100;
            sv.z -= 100;


            //Camera cam = world.getCamera();
            //cam.moveCamera(Camera.CAMERA_MOVEOUT, 150);
            //cam.lookAt(planet.getTransformedCenter());

            //SimpleVector sv = new SimpleVector();
            //sv.set(cube.getTransformedCenter());
            //sv.y -= 100;
            //sv.z -= 100;
            //sun.setPosition(sv);

            MemoryHelper.compact();

            /*if (master == null) {
                Logger.log("Saving master Activity!");
                master = HelloWorld.this;
            }*/
            master = true;
        }
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        /*if (mAngleX != 0) {
            //cube.rotateY(mAngleX);
            mAngleX = 0;
        }

        if (mAngleY != 0) {
            //cube.rotateX(mAngleY);
            mAngleY = 0;
        }*/


        CamerObj.onRendering(touchPoint.x, touchPoint.y);
        //Logger.log("Touchpointer  "+touchPoint);
        CamerObj.focusonPlanet(planets.get(1).getPlanetObj());
        CamerObj.setRotateCenter((planets.get(1).getPlanetObj().getTransformedCenter()));

        rotate = rotate -0.000005f;
        planets.get(1).getPlanetObj().rotateY(rotate);
        //Logger.log("planet");
        fb.clear(back);
        world.renderScene(fb);
        world.draw(fb);
        fb.display();

        if (System.currentTimeMillis() - time >= 1000) {
            Logger.log(fps + "fps");
            fps = 0;
            time = System.currentTimeMillis();
        }

        fps++;
    }
    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngleX() { return mAngleX; }
    public float getAngleY() { return mAngleY; }
    public PointF getTouchPoint(){ return touchPoint; }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngleX(float angle) {
        mAngleX = angle;
    }
    public void setAngleY(float angle) {
        mAngleY = angle;
    }
    public void setTouchPoint(PointF point){ touchPoint = point; }
}