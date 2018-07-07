package com.example.livio3.run2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BBOSS on 05/07/2018.
 */

public class RaceAdapter extends ArrayAdapter<Race> {
    private final LayoutInflater mInflater;
    private Context context;
    private int resource;
    private List<Race> races;
    public RaceAdapter(@NonNull Context context, int resource, @NonNull List<Race> races) {
        super(context, resource, races);
        this.context = context;
        this.races = races;
        this.resource = resource;
        mInflater = (LayoutInflater)context
                .getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            convertView = mInflater.inflate(resource, null);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDate = convertView.findViewById(R.id.tvData);
        TextView tvLocality = convertView.findViewById(R.id.tvLocality);
        TextView tvDistance = convertView.findViewById(R.id.tvDistance);
        tvName.setText(races.get(position).getName());
        tvDate.setText(races.get(position).getDateRaceExport());
        tvLocality.setText(races.get(position).getLocality());
        tvDistance.setText(String.valueOf( races.get(position).getDistance()));
        return convertView;
    }
}

