package com.oscarbartolo.cityincidents.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.content.res.Resources;

import com.oscarbartolo.cityincidents.R;
import com.oscarbartolo.cityincidents.ui.fragments.MainFragment;
import com.oscarbartolo.cityincidents.ui.fragments.MyIncidentsFragment;
import com.oscarbartolo.cityincidents.ui.fragments.NearIncidentsFragment;
import com.oscarbartolo.cityincidents.util.TabsListener;

public class MainActivity extends Activity {
    private ActionBar actionBar;
    private Resources resources;
    private Tab tabMain;
    private Tab tabMyIncidents;
    private Tab tabNearIncidents;
    private Fragment fragmentMain;
    private Fragment fragmentMyIncidents;
    private Fragment fragmentNearIncidents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTabs();

    }

    private void loadTabs(){
        resources = getResources();

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabMain = actionBar.newTab().setText(resources.getString(R.string.app_name));
        tabMyIncidents = actionBar.newTab().setText(resources.getString(R.string.my_incident_fragment));
        tabNearIncidents = actionBar.newTab().setText(resources.getString(R.string.near_incident_fragment));

        fragmentMain = new MainFragment();
        fragmentMyIncidents = new MyIncidentsFragment();
        fragmentNearIncidents = new NearIncidentsFragment();

        tabMain.setTabListener(new TabsListener(fragmentMain));
        tabMyIncidents.setTabListener(new TabsListener(fragmentMyIncidents));
        tabNearIncidents.setTabListener(new TabsListener(fragmentNearIncidents));

        actionBar.addTab(tabMain);
        actionBar.addTab(tabMyIncidents);
        actionBar.addTab(tabNearIncidents);
    }
}
