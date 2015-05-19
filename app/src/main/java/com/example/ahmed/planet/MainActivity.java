package com.example.ahmed.planet;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.threed.jpct.Logger;

import java.lang.reflect.Field;


public class MainActivity extends ActionBarActivity {
    private GLSurfaceView mGLView;
    private static MainActivity master = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (master != null) {
            copy(master);
        }
        super.onCreate(savedInstanceState);
        Logger.log("TEST");
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }



    @Override
    protected void onPause() {
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        super.onResume();
        mGLView.onResume();
    }
    private void copy(Object src) {
        try {
            Logger.log("Copying data from master Activity!");
            Field[] fs = src.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
