package UserInterfaceInteraction;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

import com.threed.jpct.Logger;


public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private Object spinnerItemName;
    private int spinnerItemID;
    private Activity activity;
    private boolean planetChanged = false;
    private boolean appStarted = false;
    private int formerSpinnerItemID;

    public SpinnerListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // If statement used on startup of the app to avoid a cameratransition when starting the app, because onItemSelected function
        // is automatically run one time on start
        if (!appStarted){
            spinnerItemName = parent.getItemAtPosition(position);
            spinnerItemID = position;
            appStarted = true;
            Logger.log("App nicht gestartet");

        } else if (appStarted) {
            formerSpinnerItemID = spinnerItemID;
            spinnerItemName = parent.getItemAtPosition(position);
            spinnerItemID = position;
            planetChanged = true;
            Logger.log("App gestartet");

            FragmentManager fm = activity.getFragmentManager();
            DisplayInfoFragment f = (DisplayInfoFragment) fm.findFragmentByTag("dif");
            if (f != null) {
                FragmentTransaction t = fm.beginTransaction();
                t.remove(f);
                t.commit();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
