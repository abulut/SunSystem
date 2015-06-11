package UserInterfaceInteraction;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;


public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private Object spinnerPosition;
    private Activity activity;

    public SpinnerListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Object spinnerPos;
        //spinnerPos = parent.getItemAtPosition(position);
        spinnerPosition = parent.getItemAtPosition(position);

        FragmentManager fm = activity.getFragmentManager();
        DisplayInfoFragment f = (DisplayInfoFragment) fm.findFragmentByTag("dif");
        if (f != null) {
            FragmentTransaction t = fm.beginTransaction();
            t.remove(f);
            t.commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Object getSpinnerPosition() {
        return spinnerPosition;
    }
}
