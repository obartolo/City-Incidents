package com.oscarbartolo.cityincidents.util;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.oscarbartolo.cityincidents.R;

/**
 * Created by Oscar on 29/12/2014.
 */
public class TabsListener implements ActionBar.TabListener {
    private Fragment fragmento;

    public TabsListener(Fragment fragmento) {
        this.fragmento = fragmento;
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.tab, fragmento);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(fragmento);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.tab, fragmento);
    }
}
