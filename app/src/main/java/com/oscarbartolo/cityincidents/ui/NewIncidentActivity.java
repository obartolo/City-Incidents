package com.oscarbartolo.cityincidents.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;

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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btCreate) {
            btCreate.setEnabled(false);
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

        ByteArrayOutputStream os = new ByteArrayOutputStream();



        Bitmap bmp = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        img = stream.toByteArray();
        String image = Base64.encodeToString(img, Base64.DEFAULT);

        /*if (){
            //TODO comprobar que ha cambiado la imagen
            Toast.makeText(getApplicationContext(), R.string.error_image, Toast.LENGTH_LONG).show();
            return;
        }*/

        /*//Toast.makeText(getApplicationContext(), R.string.processing_image, Toast.LENGTH_LONG).show();

        String image = "";
        /*File file = new File(picturePath);
        try {
            FileInputStream stream = new FileInputStream(file);
            img = new byte[(int) file.length()];
            stream.read(img);

            image = Base64.encodeBytes(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //Toast.makeText(getApplicationContext(), R.string.image_process, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), image, Toast.LENGTH_LONG).show();*/

        new Connect().execute(title, description, image, latitude, longitude, id);
    }

    public class Connect extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... datum) {
            try {
                final String url = "http://192.168.1.12:8080/addincident?title=" + datum[0] + "&description=" + datum[1] + "&img=" + datum[2] +
                                    "&lat=" + datum[3] + "&lon=" + datum[4] + "&id=" + datum[5] ;

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                boolean newIncident = restTemplate.getForObject(url, Boolean.class);
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
