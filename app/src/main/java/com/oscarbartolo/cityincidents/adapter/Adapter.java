package com.oscarbartolo.cityincidents.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscarbartolo.cityincidents.Base.Incident;
import com.oscarbartolo.cityincidents.R;

import java.util.List;

/**
 * Created by Oscar on 23/05/2015.
 */
public class Adapter extends ArrayAdapter<Incident> {

    private List<Incident> incidentList;
    private LayoutInflater inflater;
    private Context context;
    private int layoutId;

    public Adapter(Context context, int layoutId, List<Incident> incidentsList) {
        super(context, layoutId, incidentsList);
        this.context = context;
        this.incidentList = incidentsList;
        this.layoutId = layoutId;
    }

    static class ItemIncident {
        ImageView imgIncident;
        TextView tvTitleIncident;
    }

    @Override
    public int getCount() {
        return incidentList.size();
    }

    @Override
    public Incident getItem(int i) {
        return incidentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemIncident itemIncident;

        if (view == null){
            inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutId, viewGroup, false);

            itemIncident = new ItemIncident();
            itemIncident.imgIncident = (ImageView) view.findViewById(R.id.imgIncident);
            itemIncident.tvTitleIncident = (TextView) view.findViewById(R.id.tvTitleIncident);

            view.setTag(itemIncident);
        } else {
            itemIncident = (ItemIncident) view.getTag();
        }

        Incident incident = incidentList.get(position);
        itemIncident.imgIncident.setImageBitmap(BitmapFactory.decodeByteArray(incident.getImage(), 0, incident.getImage().length));
        itemIncident.tvTitleIncident.setText(incident.getTitle());

        return view;
    }
}
