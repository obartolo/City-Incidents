package com.oscarbartolo.cityincidents.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.google.android.gms.maps.model.LatLng;
import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class NewIncidentActivity extends Activity implements View.OnClickListener {
    private final int RESULT_IMAGE = 1;
    private EditText etTitle, etDescription;
    private ImageView imgIncident;
    private ImageButton imgAddImage;
    private Button btCreate;
    private LatLng position;
    private byte[] img;
    private String picturePath;
    private SharedPreferences sharedPref;

    final static private String APP_KEY = "om0ahyk6ucrr4oy";
    final static private String APP_SECRET = "oqkgl77po8j5o9i";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);

        sharedPref = getSharedPreferences("com.obartolo.preferences", MODE_PRIVATE);

        position = getIntent().getParcelableExtra("position");

        imgAddImage = (ImageButton) findViewById(R.id.imgAddImage);
        imgIncident = (ImageView) findViewById(R.id.imgIncident);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btCreate = (Button) findViewById(R.id.btCreate);

        btCreate.setOnClickListener(this);

        position =  getIntent().getParcelableExtra("position");

        imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });


        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btCreate) {
            saveData();
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
            Intent intent = new Intent().setClass(NewIncidentActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", "");
            editor.putInt("login", -1);
            editor.putInt("id", -1);
            editor.commit();

            Intent intent = new Intent(NewIncidentActivity.this, LoginActivity.class);
            intent.putExtra("finish", true);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == RESULT_IMAGE) && (resultCode == RESULT_OK) && (data != null)) {
            Uri selectedImage = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, path, null, null, null);
            cursor.moveToFirst();

            int index = cursor.getColumnIndex(path[0]);
            picturePath = cursor.getString(index);
            cursor.close();

            imgIncident.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private void takeImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_IMAGE);
    }

    private void saveData(){
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String latitude = String.valueOf(position.latitude);
        String longitude = String.valueOf(position.longitude);
        String id = String.valueOf(sharedPref.getInt("id", -1));

        if (picturePath.equals("")){
            Toast.makeText(getApplicationContext(), R.string.error_image, Toast.LENGTH_LONG).show();
            return;
        }
        if (title.equals("")){
            Toast.makeText(getApplicationContext(), R.string.error_title, Toast.LENGTH_LONG).show();
            return;
        }


        btCreate.setEnabled(false);

        new Connect().execute(title, description, picturePath, latitude, longitude, id);
    }


    public class Connect extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... datum) {
            File file = new File(datum[2]);
            String path = "/CityIncidents Images/" + String.valueOf(new Date().getTime()) + "_" + datum[5] + ".jpg";

            try {
                mDBApi.getSession().setOAuth2AccessToken("j0_EesclvGAAAAAAAAAACA04Rzitdg-v6flmX76GcipAjZlKkLhDDICF7dpj02ha");

                FileInputStream stream = new FileInputStream(file);
                mDBApi.putFile(path, stream, file.length(), null, null);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (DropboxException e) {
                e.printStackTrace();
                return false;
            }

            try {
                final String url = "http://192.168.1.12:8080/addincident?title=" + datum[0] + "&description=" + datum[1] + "&img=" + path +
                        "&lat=" + datum[3] + "&lon=" + datum[4] + "&id=" + datum[5] ;

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                boolean newIncident = Boolean.parseBoolean(restTemplate.getForObject(url, String.class));
                return newIncident;
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean newIncident) {
            if (newIncident){
                Toast.makeText(getApplicationContext(), R.string.ok_new_incident, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_new_incident, Toast.LENGTH_LONG).show();
                btCreate.setEnabled(true);
            }

        }
    }
}
