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
import android.widget.EditText;
import android.widget.Toast;

import com.oscarbartolo.cityincidents.Base.Person;
import com.oscarbartolo.cityincidents.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText etUser, etPass;
    private Button btContinue, btNewUser;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences("com.obartolo.preferences", MODE_PRIVATE);

        btContinue = (Button) findViewById(R.id.btContinue);
        btNewUser = (Button) findViewById(R.id.btNewUser);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);

        btContinue.setOnClickListener(this);
        btNewUser.setOnClickListener(this);

        checkLogIn();
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
            Intent intent = new Intent().setClass(LoginActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btContinue:
                logIn();
                break;
            case R.id.btNewUser:
                newUser();
                break;
        }
    }

    public void newUser(){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
        return;
    }

    public void logIn(){
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();
        new Connect().execute(user, pass);
    }

    public void checkLogIn(){
        int logIn = sharedPref.getInt("login", -1);
        int id = sharedPref.getInt("id", -1);
        String email = sharedPref.getString("email", "");
        if ((logIn != -1) && (id != -1) && (!email.equalsIgnoreCase("")) ){
            next();
        }
    }

    private void next(){
        Intent intent = new Intent().setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public class Connect extends AsyncTask<String, Void, Person> {

        @Override
        protected Person doInBackground(String... datum) {
            try {
                final String url = "http://192.168.1.12:8080/login?email=" + datum[0] + "&pass=" + datum[1];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Person person = restTemplate.getForObject(url, Person.class);
                return person;
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Person person) {
            if (person == null || person.getEmail() == null){
                Toast.makeText(getApplicationContext(), R.string.error_user, Toast.LENGTH_LONG).show();
            } else if (person.getEmail() != null) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("email", person.getEmail());
                editor.putInt("login", 0);
                editor.putInt("id", person.getId());
                editor.commit();
                next();
            }
        }
    }
}
