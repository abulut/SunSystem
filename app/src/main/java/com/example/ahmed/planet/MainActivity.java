package com.example.ahmed.planet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.threed.jpct.Logger;

import java.lang.reflect.Field;

import UserInterfaceInteraction.DisplayInfoFragment;
import UserInterfaceInteraction.SpinnerListener;


public class MainActivity extends ActionBarActivity {


    private SpinnerListener spinnerListener;
    private GLSurfaceView mGLView;
    private static MainActivity master = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        spinnerListener = new SpinnerListener(this);

        if (master != null) {
            copy(master);
        }
        super.onCreate(savedInstanceState);
        Logger.log("TEST");
        mGLView = new MyGLSurfaceView(this);

        RelativeLayout rl = new RelativeLayout(this);
        rl.addView(mGLView);

        LayoutInflater uiInflater = LayoutInflater.from(this);
        View uiView = uiInflater.inflate(R.layout.activity_main, null, false);

        rl.addView(uiView);

        setContentView(rl);

        Spinner spinner = (Spinner) findViewById(R.id.planet_spinner);
        spinner.setOnItemSelectedListener(spinnerListener);
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

    public void planetInfoFragment (View view) {
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        DisplayInfoFragment DIF = (DisplayInfoFragment) FM.findFragmentByTag("dif");
        if (DIF == null) {

            String planet = spinnerListener.getSpinnerPosition().toString();
            DIF = new DisplayInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("planet", planet);
            DIF.setArguments(bundle);
            FT.add(R.id.frInfo_id, DIF, "dif");
        } else {
            FT.remove(DIF);
        }
        FT.commit();
    }
}
