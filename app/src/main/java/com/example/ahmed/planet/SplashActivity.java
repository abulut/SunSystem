package com.example.ahmed.planet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Ahmed on 20.05.2015.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread logoTimer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                    Intent gameIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(gameIntent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}

