package UserGLInteraction;

import android.annotation.TargetApi;
import android.graphics.PointF;
import android.os.Build;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Ahmed, Artjem, Arndt on 16.05.2015.
 */
public class InputTouchHandler {

    private static ArrayList<PointF> touchPointList;
    private static float MAX_ZOOM, MIN_ZOOM;
    private static Float distance;
    private static Float tempDistanc;



    public InputTouchHandler(){
        touchPointList = new ArrayList<>();
        distance = 30f;
        MAX_ZOOM = 80f;
        MIN_ZOOM = 23f;
    }

    // First TouchPoint
    public static void actionDown(MotionEvent me){

        touchPointList.add(0, new PointF(me.getX(0), me.getY(0)));
    }
    //First TouchPoint up
    public static void actionUp(MotionEvent me){
        touchPointList.remove(0);
    }
    //Second TouchPointer
    public static void actionPointerDown(MotionEvent me, int pointerIndex){

        touchPointList.add(pointerIndex, new PointF(me.getX(pointerIndex), me.getY(pointerIndex)));
        tempDistanc = calcDistance(touchPointList.get(0).x , touchPointList.get(0).y , touchPointList.get(1).x , touchPointList.get(1).y);
    }
    //Second TouchPointer up
    public static void actionPointerUp(MotionEvent me, int pointerIndex){
        touchPointList.remove(pointerIndex);
    }

    // on Touch change
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static PointF actionMove(MotionEvent me){

        //set the First touchpoint
        float xd = (me.getX() - touchPointList.get(0).x);
        float yd = (me.getY() - touchPointList.get(0).y);

        touchPointList.get(0).x = me.getX();
        touchPointList.get(0).y = me.getY();


        if(touchPointList.size() == 2) {

            //set the second touchpoint
            touchPointList.get(1).x = me.getX(1);
            touchPointList.get(1).y = me.getY(1);

            //calculate new distance deppent on first and second touchpoint position
            float distancDelta = (calcDistance(touchPointList.get(0).x , touchPointList.get(0).y , touchPointList.get(1).x , touchPointList.get(1).y) - tempDistanc)/100;

            if(distancDelta < 0 && distance <= MAX_ZOOM){
                //Zoom Out
                distance = Math.max(distance - distancDelta,MIN_ZOOM);
            }else if(distancDelta > 0 && distance >= MIN_ZOOM ){
                // Zoom In
                distance = Math.min(distance - distancDelta,MAX_ZOOM);
            }

            return new PointF(0,0);


        }


        return new PointF(xd/10f,yd/100f);

    }
    //function to calculate the di
    private static float calcDistance(float x1, float y1, float x2, float y2){
        float d = ((float) Math.sqrt((float) Math.pow((x2 - x1), 2) + (float) Math.pow((y2 - y1), 2)));

        return d;
    }
    public static void setMaxMinZoom(float nMax, float nMin){
        MAX_ZOOM = nMax;
        MIN_ZOOM = nMin;
    }
    public static float getCameraDistance(){
        return distance;
    }
}
