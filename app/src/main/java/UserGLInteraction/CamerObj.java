package UserGLInteraction;

import com.threed.jpct.Camera;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import GL.MyGLRenderer;

/**
 * Created by Ahmed, Artjem, Arndt on 09.04.2015.
 */
public class CamerObj {

    private static Camera cam;
    private static SimpleVector rotateVec;
    private static SimpleVector rotateCenter;

    //Booleans, Vectors and Floats used to change the focused planet
    private static float moveOut;
    private static float moveUp;
    private static SimpleVector planetChangeVector; //Invisible vector that the camera looks at, to
                                                    //smoothly swing from one planet to the next
    private static SimpleVector camPosition;
    private static boolean checkVectors=true;
    private static boolean xVectorGreater;
    private static boolean yVectorGreater;
    private static boolean zVectorGreater;
    private static float moveSpeed = (float) 3;
    private static float lookSpeed = (float) 2;

    private static Matrix m = new Matrix();
    static float xAxis;
    static float yAxis;
    static float distance;

    public CamerObj(World world){

        rotateVec = new SimpleVector();
        rotateCenter = new SimpleVector();
        cam = world.getCamera();
        moveOut = 0;
        moveUp = 0;
        planetChangeVector = new SimpleVector();
        camPosition = new SimpleVector();

        xAxis = 0;
        yAxis = 0;
        distance = 5;
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
    }

    /*for the Camera Rotation around the planet */
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

    // Move the camera away from the formerPlanet, when switching focus to another
    public static void planetChangeOut(Object3D formerPlanet, MyGLRenderer mGLR) {
        cam.lookAt(formerPlanet.getTransformedCenter());
        if (moveOut < 100) {
            if(moveUp < 20) {
                cam.moveCamera(Camera.CAMERA_MOVEUP, (float) moveSpeed);
                moveUp += moveSpeed;
            }
            cam.moveCamera(Camera.CAMERA_MOVEOUT, (float) moveSpeed);
            moveOut += moveSpeed;
        } else if (moveOut >= 100) {
            moveOut = 0;
            moveUp = 0;
            setChangeVector(formerPlanet);
            mGLR.setTransitionOut(false);
            mGLR.setTransitionLook(true);
        }
    }

    // Move the camera towards the new chosen planet
    public static void planetChangeIn(Object3D currentPlanet, MyGLRenderer mGLR) {
        cam.lookAt(currentPlanet.getTransformedCenter());
        //First checks if the coordinates of the camera have to be increased or decreased, to match those of the planet
        if (checkVectors) {
            setRotateCenter(currentPlanet);
            cam.getPosition(camPosition);
            rotateVec.x = (distance * -(float) Math.sin(xAxis * ((float) Math.PI / 180)) * (float) Math.cos((yAxis) * ((float) Math.PI / 180))) + rotateCenter.x;
            rotateVec.y = (distance * -(float) Math.sin((yAxis) * ((float) Math.PI / 180))) + rotateCenter.y;
            rotateVec.z = (-distance * (float) Math.cos((xAxis) * ((float) Math.PI / 180)) * (float) Math.cos((yAxis) * ((float) Math.PI / 180))) + rotateCenter.z;

            if(camPosition.x < rotateVec.x) {
                xVectorGreater = true;
            } else if(camPosition.x >= rotateVec.x) {
                xVectorGreater = false;
            }
            if(camPosition.y < rotateVec.y) {
                yVectorGreater = true;
            } else if(camPosition.y >= rotateVec.y) {
                yVectorGreater = false;
            }
            if(camPosition.z < rotateVec.z) {
                zVectorGreater = true;
            } else if(camPosition.z >= rotateVec.z) {
                zVectorGreater = false;
            }
            checkVectors = false;

            //Changes coordinates until those of the planet are roughly met
        } else if (!checkVectors) {
            if(xVectorGreater && camPosition.x < rotateVec.x) {
                camPosition.x += moveSpeed;
            } else if(xVectorGreater && camPosition.x >= rotateVec.x){
                camPosition.x = rotateVec.x;
            }
            if(!xVectorGreater && camPosition.x > rotateVec.x) {
                camPosition.x -= moveSpeed;
            } else if(!xVectorGreater && camPosition.x <= rotateVec.x){
                camPosition.x = rotateVec.x;
            }

            if(yVectorGreater && camPosition.y < rotateVec.y) {
                camPosition.y += moveSpeed;
            } else if(yVectorGreater && camPosition.y >= rotateVec.y){
                camPosition.y = rotateVec.y;
            }
            if(!yVectorGreater && camPosition.y > rotateVec.y) {
                camPosition.y -= moveSpeed;
            } else if(!yVectorGreater && camPosition.y <= rotateVec.y){
                camPosition.y = rotateVec.y;
            }

            if(zVectorGreater && camPosition.z < rotateVec.z) {
                camPosition.z += moveSpeed;
            } else if(zVectorGreater && camPosition.z >= rotateVec.z){
                camPosition.z = rotateVec.z;
            }
            if(!zVectorGreater && camPosition.z > rotateVec.z) {
                camPosition.z -= moveSpeed;
            } else if(!zVectorGreater && camPosition.z <= rotateVec.z){
                camPosition.z = rotateVec.z;
            }
        }
        if(camPosition.x == rotateVec.x && camPosition.y == rotateVec.y && camPosition.z == rotateVec.z){
            checkVectors = true;
            mGLR.setTransitionIn(false);
        }
        cam.setPosition(camPosition);
    }

    // Swing the direction of the camera from the old planet to the new
    public static void planetChangeLook(Object3D newPlanet, MyGLRenderer mGLR) {
        cam.lookAt(planetChangeVector);
        //First checks if the coordinates of the focused point have to be increased or decreased, to match those of the planet
        if (checkVectors) {
            if(planetChangeVector.x < newPlanet.getTransformedCenter().x) {
                xVectorGreater = true;
            } else if(planetChangeVector.x >= newPlanet.getTransformedCenter().x) {
                xVectorGreater = false;
            }
            if(planetChangeVector.z < newPlanet.getTransformedCenter().z) {
                zVectorGreater = true;
            } else if(planetChangeVector.z >= newPlanet.getTransformedCenter().z) {
                zVectorGreater = false;
            }
            checkVectors = false;

            //Changes coordinates until those of the planet are roughly met
        } else if (!checkVectors) {
            if(xVectorGreater && planetChangeVector.x < newPlanet.getTransformedCenter().x) {
                planetChangeVector.x += lookSpeed;
            } else if(xVectorGreater && planetChangeVector.x >= newPlanet.getTransformedCenter().x){
                planetChangeVector.x = newPlanet.getTransformedCenter().x;
            }
            if(!xVectorGreater && planetChangeVector.x > newPlanet.getTransformedCenter().x) {
                planetChangeVector.x -= lookSpeed;
            } else if(!xVectorGreater && planetChangeVector.x <= newPlanet.getTransformedCenter().x){
                planetChangeVector.x = newPlanet.getTransformedCenter().x;
            }
            if(zVectorGreater && planetChangeVector.z < newPlanet.getTransformedCenter().z) {
                planetChangeVector.z += lookSpeed;
            } else if(zVectorGreater && planetChangeVector.z >= newPlanet.getTransformedCenter().z){
                planetChangeVector.z = newPlanet.getTransformedCenter().z;
            }
            if(!zVectorGreater && planetChangeVector.z > newPlanet.getTransformedCenter().z) {
                planetChangeVector.z -= lookSpeed;
            } else if(!zVectorGreater && planetChangeVector.z <= newPlanet.getTransformedCenter().z){
                planetChangeVector.z = newPlanet.getTransformedCenter().z;
            }
        }
        if(planetChangeVector.x == newPlanet.getTransformedCenter().x && planetChangeVector.z == newPlanet.getTransformedCenter().z){
            checkVectors = true;
            mGLR.setTransitionLook(false);
            mGLR.setTransitionIn(true);
        }
    }

    //set the center the camera rotate around
    public static void setRotateCenter(Object3D newRotateCenter){
        rotateCenter = newRotateCenter.getTransformedCenter();
    }
    //set center the Camera Look at
    public static void focusonPlanet(Object3D planet){
        cam.lookAt(planet.getTransformedCenter());
    }
    //set the distance between Camera and rotate Center
    public static void setCameraDistance(float newDistance){
        distance = newDistance;
    }
    // set the Change Vector
    public static void setChangeVector(Object3D planet) {
        planetChangeVector.x = planet.getTransformedCenter().x;
        planetChangeVector.y = planet.getTransformedCenter().y;
        planetChangeVector.z = planet.getTransformedCenter().z;
    }
}
