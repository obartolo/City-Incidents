package com.oscarbartolo.cityincidents.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oscarbartolo.cityincidents.R;
import com.oscarbartolo.cityincidents.ui.AboutMeActivity;
import com.oscarbartolo.cityincidents.ui.LoginActivity;
import com.oscarbartolo.cityincidents.ui.MapsActivity;


public class MainFragment extends Fragment implements View.OnClickListener {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button btExplore = (Button) view.findViewById(R.id.btExplore);
        Button btNewIncident = (Button) view.findViewById(R.id.btNewIncident);

        btExplore.setOnClickListener(this);
        btNewIncident.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btExplore){
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("type", "explore");
            startActivity(intent);
        } else if (view.getId() == R.id.btNewIncident){
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("type", "newincident");
            startActivity(intent);
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
}
