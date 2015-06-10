package com.example.ahmed.planet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


/**
 * Created by Ahmed on 20.05.2015.
 */
public class SplashActivity extends Activity{
    private static final int SPLASH_SHOW_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new BackgroundSplashTask().execute();

    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Integer, Void> {

        //Wird als erstes aufgerufen
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //Als zweites
        @Override
        protected Void doInBackground(Void... arg0) {

            // I have just give a sleep for this thread
            // if you want to load database, make
            // network calls, load images
            // you can do here and remove the following
            // sleep

            // do not worry about this Thread.sleep
            // this is an async task, it will not disrupt the UI

            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... arg0){

        }

        //Als letztes
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashActivity.this,MainActivity.class);
            // any info loaded can during splash_show
            // can be passed to main activity using
            // below
            //i.putExtra("loaded_info", " ");
            startActivity(i);
            finish();
        }

    }
}

