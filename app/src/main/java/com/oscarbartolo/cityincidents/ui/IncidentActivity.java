package com.oscarbartolo.cityincidents.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.oscarbartolo.cityincidents.Base.Incident;
import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IncidentActivity extends Activity implements View.OnClickListener{

    private Incident incident;
    private ImageView imgIncident;
    private TextView etTitle, etDescription, etDate, etUser;
    private Button btViewMap;
    private String idIncident;
    private SharedPreferences sharedPref;

    final static private String APP_KEY = "om0ahyk6ucrr4oy";
    final static private String APP_SECRET = "oqkgl77po8j5o9i";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        sharedPref = getSharedPreferences("com.obartolo.preferences", MODE_PRIVATE);

        etTitle = (TextView) findViewById(R.id.etTitle);
        etDescription = (TextView) findViewById(R.id.etDescription);
        etDate = (TextView) findViewById(R.id.etDate);
        etUser = (TextView) findViewById(R.id.etUser);
        imgIncident = (ImageView) findViewById(R.id.imgIncident);

        btViewMap = (Button) findViewById(R.id.btViewMap);
        btViewMap.setOnClickListener(this);

        idIncident = getIntent().getStringExtra("incident");

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);

        new Connect().execute(idIncident);
    }

    private void showIncident(Incident incident){
        this.incident = incident;
        etTitle.setText(incident.getTitle());
        etDate.setText(String.valueOf(incident.getCreatedate()));
        etDescription.setText(incident.getDescription());
        etUser.setText(incident.getId_person().getEmail());

        imgIncident.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/CityIncidents" + incident.getImage()));
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
                mDBApi.getSession().setOAuth2AccessToken("j0_EesclvGAAAAAAAAAACA04Rzitdg-v6flmX76GcipAjZlKkLhDDICF7dpj02ha");

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Incident incident = restTemplate.getForObject(url, Incident.class);

                File imagePath = new File(Environment.getExternalStorageDirectory() + "/CityIncidents" + incident.getImage());
                FileOutputStream fos;
                File folder = new File(Environment.getExternalStorageDirectory() + "/CityIncidents/CityIncidents Images/");
                if (!folder.exists()){
                    folder.mkdirs();
                }
                try {
                    fos = new FileOutputStream(imagePath);
                    DropboxAPI.DropboxFileInfo info = mDBApi.getFile(incident.getImage(), null, fos, null);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.getMessage();
                } catch (DropboxException e) {
                    e.printStackTrace();
                }

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
