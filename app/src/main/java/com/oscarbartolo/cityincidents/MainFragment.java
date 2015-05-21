package com.oscarbartolo.cityincidents;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button btExplore = (Button) view.findViewById(R.id.btExplore);
        Button btNewIncident = (Button) view.findViewById(R.id.btNewIncident);

        btExplore.setOnClickListener(this);
        btNewIncident.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btExplore){
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("type", "explore");
            startActivity(intent);
        } else if (view.getId() == R.id.btNewIncident){
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("type", "newincident");
            startActivity(intent);
        }
    }
}
