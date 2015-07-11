package loadScreenLibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ahmed.planet.MainActivity;
import com.example.ahmed.planet.R;

/**
 * Created by Ahmed, Artjem, Arndt on 11.06.2015.
 */
public class SplashActivity extends Activity{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //the first Activity with e logo and then the MainActivity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                //starts the next Activity
                startActivity(i);
                //closed the open Activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}