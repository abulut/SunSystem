package UserInterfaceInteraction;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Ahmed, Artjem, Arndt on 20.05.2015.
 */

//Listens to changes in the spinner item of the UI and calls the corresponding function
public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private Object spinnerItemName;         //Stores the current planet name
    private int spinnerItemID;              //Stores the current planet ID
    private int formerSpinnerItemID;        //Stores the ID of the planet that was selected before
    private Activity activity;
    private boolean planetChanged = false;  //Changes the rendering method in MyGLRenderer, if
                                            //another planet is selected
    private boolean appStarted = false;     //Used to initialize the SpinnerListener on App start

    public SpinnerListener(Activity activity) {
        this.activity = activity;
    }

    //Called when another planet in the spinner is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // If-statement used on startup of the app to avoid a cameratransition when starting the app, because onItemSelected function
        // is automatically run one time on start
        if (!appStarted){
            spinnerItemName = parent.getItemAtPosition(position);
            spinnerItemID = position;
            appStarted = true;

        } else if (appStarted) {
            formerSpinnerItemID = spinnerItemID;
            spinnerItemName = parent.getItemAtPosition(position);
            spinnerItemID = position;
            planetChanged = true;

            //Closes the fragment with the planetInfo, if it is still open
            FragmentManager fm = activity.getFragmentManager();
            DisplayInfoFragment f = (DisplayInfoFragment) fm.findFragmentByTag("dif");
            if (f != null) {
                FragmentTransaction t = fm.beginTransaction();
                t.remove(f);
                t.commit();
            }
        }

    }
    //Automatically generated method for onItemSelectedListener, used when nothing is selected
    //in the spinner. Not implemented here
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Various getters and setters to communicate with the spinner
    public Object getSpinnerItemName() {
        return spinnerItemName;
    }

    public int getSpinnerItemID() {return spinnerItemID;}

    public boolean getPlanetChanged() {
        return planetChanged;
    }

    public void setPlanetChangedFalse() {
        planetChanged = false;
    }

    public int getFormerSpinnerItemID() {
        return formerSpinnerItemID;
    }
}
