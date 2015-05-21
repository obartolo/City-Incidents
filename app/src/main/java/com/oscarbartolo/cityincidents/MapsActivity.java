package com.oscarbartolo.cityincidents;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
                                                                LocationListener, View.OnClickListener {

    private GoogleMap mMap;
    private Marker marker;
    private Button btIncident;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        type = getIntent().getStringExtra("type");

        if (type.equalsIgnoreCase("explore")){
            View fragment = findViewById(R.id.mapIncident);
            fragment.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
        } else if (type.equalsIgnoreCase("newincident")){

        }

        //TODO va a venir un texto para distinguir si es explorar, nuevo o uno suelto
        //TODO quitar boton, explorar y uno suelto
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addOneMarker(latLng);
            }
        });
        btIncident = (Button) findViewById(R.id.btIncident);
        btIncident.setOnClickListener(this);
        cameraToMyLocation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        cameraToMyLocation();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            MapsInitializer.initialize(this);

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapIncident)).getMap();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void cameraToMyLocation(){
        //TODO no va al cargar por primera vez
            Location location = mMap.getMyLocation();

            if (location != null){
                CameraUpdate camera = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                mMap.moveCamera(camera);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 2000, null);
            }
    }

    private void addMarkers(List<Incident> incidents){
        //TODO marcar varios incidentes
    }

    private void addOneMarker(LatLng latLng){
        //TODO este marker de otro color que es
        if (type.equalsIgnoreCase("newincident")) {
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //TODO mostrar titulo y/o llevar a la descripcion y los
        this.marker = marker;
        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btIncident){

            if (marker == null) {
                addMarkerToMyLocation();
            }
            marker.getPosition();


            Intent intent = new Intent().setClass(MapsActivity.this, NewIncident.class);
            startActivity(intent);
            //TODO Comprabar que hay algo seleccionado(si no cojer la ubicacion actual) y pasarlo
        }
    }

    private void addMarkerToMyLocation(){
        /*setUpMapIfNeeded();
        Location location = mMap.getMyLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        addOneMarker(latLng);*/
    }
}
