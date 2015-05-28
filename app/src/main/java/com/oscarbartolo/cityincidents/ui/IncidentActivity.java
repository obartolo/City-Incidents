package com.oscarbartolo.cityincidents.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscarbartolo.cityincidents.Base.Incident;
import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class IncidentActivity extends Activity implements View.OnClickListener{

    private Incident incident;
    private ImageView imgIncident;
    private TextView etTitle, etDescription, etDate, etUser;
    private Button btViewMap;
    private String idIncident;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        sharedPref = getSharedPreferences("com.obartolo.preferences", MODE_PRIVATE);

        etTitle = (TextView) findViewById(R.id.etTitle);
        etDescription = (TextView) findViewById(R.id.etDescription);
        etDate = (TextView) findViewById(R.id.etDate);
        etUser = (TextView) findViewById(R.id.etUser);

        btViewMap = (Button) findViewById(R.id.btViewMap);
        btViewMap.setOnClickListener(this);

        idIncident = getIntent().getStringExtra("incident");
        new Connect().execute(idIncident);
    }

    private void showIncident(Incident incident){
        this.incident = incident;
        etTitle.setText(this.incident.getTitle());
        etDate.setText(String.valueOf(this.incident.getCreatedate()));
        //imgIncident.setImageBitmap(BitmapFactory.decodeByteArray(incident.getImage(), 0, incident.getImage().length));
        etDescription.setText(this.incident.getDescription());
        etUser.setText(this.incident.getId_person().getEmail());
        //TODO anadir imagen
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btViewMap){
            Intent intent = new Intent().setClass(IncidentActivity.this, MapsActivity.class);
            intent.putExtra("type", "oneincident");
            intent.putExtra("incident", String.valueOf(incident.getId()));
            intent.putExtra("longitude", incident.getLongitude());
            intent.putExtra("latitude", incident.getLatitude());
            startActivity(intent);
            finish();
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
            Intent intent = new Intent().setClass(IncidentActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", "");
            editor.putInt("login", -1);
            editor.putInt("id", -1);
            editor.commit();

            Intent intent = new Intent(IncidentActivity.this, LoginActivity.class);
            intent.putExtra("finish", true);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class Connect extends AsyncTask<String, Void, Incident> {

        @Override
        protected Incident doInBackground(String... id) {
            try {
                final String url = "http://192.168.1.12:8080/getoneincidents?id=" + id[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Incident incident = restTemplate.getForObject(url, Incident.class);
                return incident;
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Incident incident) {
            showIncident(incident);
        }
    }
}
