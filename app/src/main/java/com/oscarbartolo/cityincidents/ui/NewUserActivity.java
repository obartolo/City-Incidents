package com.oscarbartolo.cityincidents.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class NewUserActivity extends Activity implements View.OnClickListener {

    private EditText etUser, etPass, etRePass;
    private Button btContinue, btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        etRePass = (EditText) findViewById(R.id.etRePass);

        btCancel = (Button) findViewById(R.id.btCancel);
        btContinue = (Button) findViewById(R.id.btContinue);

        btContinue.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about_me) {
            Intent intent = new Intent().setClass(NewUserActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btCancel:
                cancel();
                break;
            case R.id.btContinue:
                singIn();
                break;
        }
    }

    public void cancel(){
        finish();
    }

    public void singIn(){
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();
        String rePass = etRePass.getText().toString();
        if (user.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), R.string.error_new_user_email, Toast.LENGTH_LONG).show();
            return;
        }
        if (pass.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), R.string.error_pass_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if (!pass.equalsIgnoreCase(rePass)){
            Toast.makeText(getApplicationContext(), R.string.error_pass, Toast.LENGTH_LONG).show();
            return;
        }

        new Connect().execute(user, pass);
    }

    public class Connect extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... datum) {
            try {
                final String url = "http://192.168.1.12:8080/singin?email=" + datum[0] + "&pass=" + datum[1];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                boolean singIn = restTemplate.getForObject(url, Boolean.class);
                return singIn;
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean singIn) {
            if (singIn){
                Toast.makeText(getApplicationContext(), R.string.ok_new_user, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_new_user, Toast.LENGTH_LONG).show();
            }

        }
    }

}
