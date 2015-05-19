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
import android.view.MotionEvent;

import com.threed.jpct.Logger;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;
    private InputTouchHandler intouchHandle = null;
    private PointF touchPoint = null;

    public MyGLSurfaceView(Context context) {
        super(context);
        // Set the Renderer for drawing on the GLSurfaceView
        intouchHandle = new InputTouchHandler();
        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    //private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    //private float xpos = -1;
    //private float ypos = -1;
    public boolean onTouchEvent(MotionEvent me) {
        /*float x = me.getX();
        float y = me.getY();

        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            xpos = me.getX();
            ypos = me.getY();
            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_UP) {
            xpos = -1;
            ypos = -1;
            //touchTurn = 0;
            //touchTurnUp = 0;
            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_MOVE) {
            float xd = me.getX() - xpos;
            float yd = me.getY() - ypos;

            xpos = me.getX();
            ypos = me.getY();

            //touchTurn = xd / -100f;
            //touchTurnUp = yd / -100f;
            // reverse direction of rotation above the mid-line
            if (y > getHeight() / 2) {
                xd = xd * -1 ;
            }

            // reverse direction of rotation to left of the mid-line
            if (x < getWidth() / 2) {
                yd = yd * -1 ;
            }
            // set Y postion from cube
            mRenderer.setAngleY(
                    mRenderer.getAngleY() +
                            ((yd / -100f)));
            mRenderer.setAngleX(
                    mRenderer.getAngleX() +
                            ((xd / 100f)));
            requestRender();


            return true;
        }

        try {
            Thread.sleep(15);
        } catch (Exception e) {
            // No need for this...
        }

        xpos = x;
        ypos = y;
        return super.onTouchEvent(me);
        */


        int pointerIndex = me.getActionIndex();
        int maskedAction = me.getActionMasked();
        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:{
                InputTouchHandler.actionDown(me);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                InputTouchHandler.actionPointerDown(me, pointerIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                touchPoint = InputTouchHandler.actionMove(me);
                mRenderer.setTouchPoint(touchPoint);
                Logger.log("Touchpointer on Surface  " + touchPoint);
                break;
            }

            case MotionEvent.ACTION_UP:{
                InputTouchHandler.actionUp(me);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:{
                InputTouchHandler.actionPointerUp(me, pointerIndex);
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }

        }
        return true;
    }

    protected boolean isFullscreenOpaque() {
        return true;
    }

}
