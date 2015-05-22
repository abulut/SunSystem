package com.example.ahmed.planet;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DisplayInfoFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_display_info, container, false);

        // Receives the bundle from MainActivity with the currently chosen planet
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String planet = bundle.getString("planet");

            // Gets the array with planet information from .xml
            int infoID = getResources().getIdentifier(planet + "_info", "array", getActivity().getApplicationContext().getApplicationInfo().packageName);

            String[] planetData = getResources().getStringArray(infoID);
            TextView textView = (TextView) v.findViewById(R.id.infoFragmentTextview);

            StringBuilder builder = new StringBuilder();
            for (String s : planetData) {
                builder.append(s);
                builder.append("\n");
            }


            textView.setTextSize(20);
            textView.setText(builder.toString().trim());
        }

        return v;

    }
}
