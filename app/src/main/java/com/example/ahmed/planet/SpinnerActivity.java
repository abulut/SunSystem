package com.example.ahmed.planet;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;


public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public Object spinnerPosition;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Object spinnerPos;
        //spinnerPos = parent.getItemAtPosition(position);
        spinnerPosition = parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
