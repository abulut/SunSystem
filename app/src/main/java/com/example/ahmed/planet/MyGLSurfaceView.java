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

import UserInterfaceInteraction.SpinnerListener;
import loadScreenLibrary.BackgroundSplashTask;
/**
 * Created by Ahmed, Artjem, Arndt on 16.05.2015.
 */
public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;
    private InputTouchHandler intouchHandle = null;
    private PointF touchPoint = null;
    private float camDistance;
    private boolean master = false;
    private BackgroundSplashTask asyncTask;
    private SpinnerListener spinnerListener;

    public MyGLSurfaceView(Context context, SpinnerListener spLtr, BackgroundSplashTask async) {
        super(context);
        // Set the Renderer for drawing on the GLSurfaceView
        asyncTask =async;
        spinnerListener = spLtr;
        intouchHandle = new InputTouchHandler();
        mRenderer = new MyGLRenderer(context, spinnerListener, asyncTask);
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    public boolean onTouchEvent(MotionEvent me) {

        int pointerIndex = me.getActionIndex();
        int maskedAction = me.getActionMasked();
        switch (maskedAction) {

            // First TouchPoint
            case MotionEvent.ACTION_DOWN:{
                InputTouchHandler.actionDown(me);
                break;
            }
            //Second TouchPointer
            case MotionEvent.ACTION_POINTER_DOWN: {
                InputTouchHandler.actionPointerDown(me, pointerIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                touchPoint = InputTouchHandler.actionMove(me);
                camDistance = InputTouchHandler.getCameraDistance();
                mRenderer.setTouchPoint(touchPoint);
                mRenderer.setCameraDistance(camDistance);
                //Logger.log("Touchpointer on Surface  " + touchPoint);
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
