package UserInterfaceInteraction;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;


public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private Object spinnerItemName;
    private int spinnerItemID;
    private Activity activity;

    public SpinnerListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Object spinnerPos;
        //spinnerPos = parent.getItemAtPosition(position);
        spinnerItemName = parent.getItemAtPosition(position);
        spinnerItemID = position;

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

    public Object getSpinnerItemName() {
        return spinnerItemName;
    }
    public int getSpinnerItemID() {return spinnerItemID;}
}
