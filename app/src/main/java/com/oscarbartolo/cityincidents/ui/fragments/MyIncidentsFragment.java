package com.oscarbartolo.cityincidents.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oscarbartolo.cityincidents.Base.Incident;
import com.oscarbartolo.cityincidents.R;
import com.oscarbartolo.cityincidents.adapter.Adapter;
import com.oscarbartolo.cityincidents.ui.AboutMeActivity;
import com.oscarbartolo.cityincidents.ui.IncidentActivity;
import com.oscarbartolo.cityincidents.ui.LoginActivity;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MyIncidentsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Adapter adapter;
    private ListView lvMyIncidents;
    private List<Incident> incidentList;
    private SharedPreferences sharedPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        sharedPref = getActivity().getSharedPreferences("com.obartolo.preferences", getActivity().MODE_PRIVATE);
        if (sharedPref.getInt("id", -1) == -1){
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPref.getInt("id", -1) == -1){
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about_me) {
            Intent intent = new Intent().setClass(getActivity(), AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", "");
            editor.putInt("login", -1);
            editor.putInt("id", -1);
            editor.commit();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_incidents, container, false);
        new Connect().execute();

        lvMyIncidents = (ListView) view.findViewById(R.id.lvMyIncidents);

        return view;
    }

    private void fillList(List<Incident> incidentList){
        this.incidentList = incidentList;
        adapter = new Adapter(getActivity(), R.layout.adapter, this.incidentList);
        lvMyIncidents.setAdapter(adapter);
        lvMyIncidents.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Incident incident = incidentList.get(i);

        Intent intent = new Intent(getActivity(), IncidentActivity.class);
        intent.putExtra("incident", String.valueOf(incident.getId()));
        startActivity(intent);
    }

    public class Connect extends AsyncTask<Void, Void, List> {

        @Override
        protected List<Incident> doInBackground(Void... voids) {
            try {
                final String url = "http://192.168.1.12:8080/getmyincidents?id=" + String.valueOf(sharedPref.getInt("id", -1));
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
            fillList(incidentList);
        }
    }
}
