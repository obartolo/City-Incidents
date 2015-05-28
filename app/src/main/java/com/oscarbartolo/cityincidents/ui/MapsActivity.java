package com.oscarbartolo.cityincidents.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oscarbartolo.cityincidents.Base.Incident;
import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
                                                                LocationListener, View.OnClickListener {
    private SharedPreferences sharedPref;
    private GoogleMap mMap;
    private Marker marker;
    private Button btIncident;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        sharedPref = getSharedPreferences("com.obartolo.preferences", MODE_PRIVATE);

        type = getIntent().getStringExtra("type");

        if (!type.equalsIgnoreCase("newincident")){
            View fragment = findViewById(R.id.mapIncident);
            fragment.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
        }

        if (type.equalsIgnoreCase("explore")){
            new Connect().execute();
        } else if (type.equalsIgnoreCase("oneincident")){
            addOneMarker(new LatLng(Double.parseDouble(getIntent().getStringExtra("latitude")), Double.parseDouble(getIntent().getStringExtra("longitude"))), getIntent().getStringExtra("incident"));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (type.equalsIgnoreCase("newincident")) {
                    addOneMarker(latLng, "-1");
                }
            }
        });
        mMap.setOnMarkerClickListener(this);
        btIncident = (Button) findViewById(R.id.btIncident);
        btIncident.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!type.equalsIgnoreCase("oneincident")) {
            cameraToMyLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about_me) {
            Intent intent = new Intent().setClass(MapsActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", "");
            editor.putInt("login", -1);
            editor.putInt("id", -1);
            editor.commit();

            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            intent.putExtra("finish", true);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            MapsInitializer.initialize(this);

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapIncident)).getMap();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void cameraToMyLocation(){
            Location location = mMap.getMyLocation();
            CameraUpdate camera;
            if (location != null){
                camera = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                mMap.moveCamera(camera);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 2000, null);
            } else {
                camera = CameraUpdateFactory.newLatLng(new LatLng(37, -122));
                mMap.moveCamera(camera);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f), 2000, null);
            }
    }

    private void cameraMarkerLocation(LatLng latLng){
        CameraUpdate camera = CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(camera);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 2000, null);

    }

    private void addMarkers(List<Incident> incidents){
        if (incidents.size() > 0){
            for (Incident incident : incidents){
                addOneMarker(new LatLng(Double.parseDouble(incident.getLatitude()), Double.parseDouble(incident.getLongitude())), String.valueOf(incident.getId()));
            }
        }
    }

    private void addOneMarker(LatLng latLng, String id){
        if (type.equalsIgnoreCase("newincident")) {
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        } else if ((type.equalsIgnoreCase("oneincident")) || (type.equalsIgnoreCase("explore"))){
            marker = mMap.addMarker(new MarkerOptions().position(latLng));
            marker.setSnippet(id);
            cameraMarkerLocation(latLng);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (type.equalsIgnoreCase("newincident")) {
            return false;
        } else if ((type.equalsIgnoreCase("oneincident")) || (type.equalsIgnoreCase("explore"))){
            Intent intent = new Intent(MapsActivity.this, IncidentActivity.class);

            intent.putExtra("incident", marker.getSnippet());
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btIncident){
            if (marker == null) {
                if (!addMarkerToMyLocation()){
                    return;
                }
            }

            LatLng latLng = marker.getPosition();

            Intent intent = new Intent().setClass(MapsActivity.this, NewIncidentActivity.class);
            intent.putExtra("position", latLng);
            startActivity(intent);
            finish();
        }
    }

    private boolean addMarkerToMyLocation(){
        Location location = mMap.getMyLocation();
        if (location == null){
            return false;
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        addOneMarker(latLng, "-1");
        return true;
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

    public class Connect extends AsyncTask<Void, Void, List> {

        @Override
        protected List<Incident> doInBackground(Void... voids) {
            try {
                final String url = "http://192.168.1.12:8080/getincidents";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Incident[] incidents = restTemplate.getForObject(url, Incident[].class);
                List<Incident> incidentList = new ArrayList<>();
                for (Incident incident : incidents){
                    incidentList.add(incident);
                }
                return incidentList;
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List incidentList) {
            addMarkers(incidentList);
        }
    }
}
