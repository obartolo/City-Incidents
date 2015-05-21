package com.oscarbartolo.cityincidents;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class NewIncident extends Activity implements View.OnClickListener {

    private Button btCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);

        btCreate = (Button) findViewById(R.id.btCreate);
        btCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btCreate) {
            String latitude = getIntent().getStringExtra("latitude");
            String longitude = getIntent().getStringExtra("longitude");
            //TODO cojer los datos, guardarlos en la bbdd y cerrar esta ventana
        }
    }
}
