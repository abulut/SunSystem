package UserInterfaceInteraction;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ahmed.planet.R;

/**
 * Created by Ahmed, Artjem, Arndt on 20.05.2015.
 */
//Class to display the information of planets in a fragment
public class DisplayInfoFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View that the informations are inserted to
        View v = inflater.inflate(R.layout.fragment_display_info, container, false);

        //Receives the bundle from MainActivity with the currently chosen planet
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String planet = bundle.getString("planet");

            // Gets the array with planet information from .xml
            int infoID = getResources().getIdentifier(planet + "_info", "array", getActivity().getApplicationContext().getApplicationInfo().packageName);
            String[] planetData = getResources().getStringArray(infoID);
            ListView listView = (ListView) v.findViewById(R.id.infoFragmentListview);
            //Creates a list element for each planet-information piece
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.info_item, planetData);
            listView.setAdapter(adapter);
        }
        return v;
    }
}
