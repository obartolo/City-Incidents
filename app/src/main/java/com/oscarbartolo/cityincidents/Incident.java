package com.oscarbartolo.cityincidents;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Incident extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);
    }

    //TODO mostra los datos del incident
    //TODO controlar boton e ir al mapa pasandole todo el incident
}
