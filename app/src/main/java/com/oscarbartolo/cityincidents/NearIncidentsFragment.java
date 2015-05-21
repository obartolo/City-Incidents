package com.oscarbartolo.cityincidents;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Oscar on 20/05/2015.
 */
public class NearIncidentsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_incidents, container, false);

        return view;
    }
    //TODO conseguir ubicacion
    //TODO preparar el adapter, conectar con el sevidor y mostrar los datos
    //TODO guardar los datos y pasarlos a la ventana incident
}
