package com.geverartsdev.lfsa2212.gmap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geverartsdev.lfsa2212.gmap.R;
import com.geverartsdev.lfsa2212.gmap.objects.Pothole;

import java.util.Date;
import java.util.List;

public class PotholeAdapter extends ArrayAdapter<Pothole> {

    private int layout;
    private List<Pothole> potholes;

    public PotholeAdapter(Context context, int resource, List<Pothole> potholes) {
        super(context, resource, potholes);
        this.layout = resource;
        this.potholes = potholes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.intensity = convertView.findViewById(R.id.potholeIntensityText);
            viewHolder.location = convertView.findViewById(R.id.potholeLocationText);
            viewHolder.time = convertView.findViewById(R.id.potholeTimeText);

            convertView.setTag(viewHolder);
        }

        final ViewHolder mainViewHolder = (ViewHolder) convertView.getTag();

        Pothole pothole = potholes.get(position);
        mainViewHolder.time.setText("Time : " + new Date(pothole.getTime()).toString());
        mainViewHolder.location.setText("Location : " + pothole.getLocationString());
        mainViewHolder.intensity.setText("Intensity : " + pothole.getIntensity());

        return convertView;
    }

    public class ViewHolder {
        TextView time;
        TextView intensity;
        TextView location;
    }
}
