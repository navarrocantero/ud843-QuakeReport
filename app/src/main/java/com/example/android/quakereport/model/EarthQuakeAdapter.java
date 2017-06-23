package com.example.android.quakereport.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;

import java.util.ArrayList;

/**
 * Created by driftineo on 10/6/17.
 */

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    public EarthQuakeAdapter(Context context, ArrayList<EarthQuake> earthQuakes) {
        super(context, 0, earthQuakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        EarthQuake currentEarthQuake = this.getItem(position);
        View listItemView = convertView;



        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_listitem, parent, false);

        }


        TextView cityNameView = (TextView) listItemView.findViewById(R.id.nameView);
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitudeView);
        TextView dateView = (TextView) listItemView.findViewById(R.id.dateView);


        cityNameView.setText(currentEarthQuake.getCityName());
        dateView.setText(currentEarthQuake.getDate());
        magnitudeView.setText(currentEarthQuake.getMagnitude());



        return listItemView;
    }
}


