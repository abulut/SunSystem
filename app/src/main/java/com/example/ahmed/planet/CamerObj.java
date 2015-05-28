package com.example.ahmed.planet;

import android.util.FloatMath;

import com.threed.jpct.Camera;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

/**
 * Created by Artjem on 09.04.2015.
 */
public class CamerObj {

    private static Camera cam;
    private static SimpleVector rotateVec;
    private static SimpleVector rotateCenter;
    private static Matrix m = new Matrix();
    static float xAxis;
    static float yAxis;
    static float distance;

    public CamerObj(World world){

        rotateVec = new SimpleVector();
        rotateCenter = new SimpleVector();
        cam = world.getCamera();

        xAxis = 0;
        yAxis = 0;
        distance = 5;
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);


    }

    public static void onRendering(float x,float y){


        if(x != xAxis){
            xAxis += x;
        }
        if(y != yAxis && yAxis >= -30 && yAxis <= 30){
            yAxis += y;
        }
        if(yAxis > 30) {
            yAxis = 30;
        }
        else if(yAxis < -30){
            yAxis = -30;
        }

            rotateVec.x = (distance * -(float) Math.sin(xAxis * ((float) Math.PI / 180)) * (float) Math.cos((yAxis) * ((float) Math.PI / 180))) + rotateCenter.x;
            rotateVec.y = (distance * -(float) Math.sin((yAxis) * ((float) Math.PI / 180))) + rotateCenter.y;
            rotateVec.z = (-distance * (float) Math.cos((xAxis) * ((float) Math.PI / 180)) * (float) Math.cos((yAxis) * ((float) Math.PI / 180))) + rotateCenter.z;


            cam.setPosition(rotateVec.x, rotateVec.y, rotateVec.z);


    }


    public static void setRotateCenter(SimpleVector newRotateCenter){
        rotateCenter = newRotateCenter;
    }
    public static void focusonPlanet(Object3D planet){
        cam.lookAt(planet.getTransformedCenter());
    }

    public static SimpleVector getCamPos(){return rotateVec;}

    public static void setCameraDistance(float newDistance){
        distance = newDistance;
    }

}
