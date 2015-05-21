package com.oscarbartolo.cityincidents;

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
import android.widget.TextView;
import android.widget.Toast;

import com.oscarbartolo.cityincidents.Base.Person;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class Login extends Activity implements View.OnClickListener {
    //TODO pantalla completa
    //TODO cambiar espacio por una imagen

    private EditText etUser, etPass;
    private Button btContinue, btNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btContinue = (Button) findViewById(R.id.btContinue);
        btNewUser = (Button) findViewById(R.id.btNewUser);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);

        btContinue.setOnClickListener(this);
        btNewUser.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
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
        Intent intent = new Intent(this, NewUser.class);
        startActivity(intent);
        return;
    }

    public void logIn(){
        Intent intent = new Intent().setClass(Login.this, MainActivity.class);
        startActivity(intent);
        //new Connect().execute();
    }

    public class Connect extends AsyncTask<Void, Void, Person> {
        private Login login;

        @Override
        protected Person doInBackground(Void... params) {
            try {
                final String url = "http://192.168.1.12:8080/login?email=  &pass=  ";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Person person = restTemplate.getForObject(url, Person.class);
                return person;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Person person) {
            /*EditText idText = (EditText) findViewById(R.id.etUser);
            EditText nameText = (EditText) findViewById(R.id.etPass);
            idText.setText(person.getName(), TextView.BufferType.EDITABLE);
            nameText.setText(person.getName(), TextView.BufferType.EDITABLE);*/
        }

        public void setLogin(Login login){
            this.login = login;
        }
    }
}
