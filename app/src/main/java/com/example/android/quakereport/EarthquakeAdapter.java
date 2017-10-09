package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by meghnabanerjee on 5/18/17.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        Earthquake currentEarthquake = getItem(position);
        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        double magnitudeValue = currentEarthquake.getMagnitude();
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        magnitudeFormat.format(magnitudeValue);
        magnitude.setText(Double.toString(magnitudeValue));


        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);



        if(currentEarthquake.getLocation().indexOf(" of ")>-1) {
            String[] placeData = currentEarthquake.getLocation().split(" of ");
            TextView direction_view = (TextView) listItemView.findViewById(R.id.location_offset);
            direction_view.setText(placeData[0] + " of ");

            TextView location_view = (TextView) listItemView.findViewById(R.id.primary_location);
            location_view.setText(placeData[1]);

        }

        else
        {
            TextView direction_view = (TextView) listItemView.findViewById(R.id.location_offset);
            direction_view.setText("Near the");

            TextView location_view = (TextView) listItemView.findViewById(R.id.primary_location);
            location_view.setText(currentEarthquake.getLocation());
        }



        TextView date_view = (TextView) listItemView.findViewById(R.id.date);
        Date date = new Date(currentEarthquake.getDate());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
        String dateToDisplay = dateFormatter.format(date);
        date_view.setText(dateToDisplay);

        TextView time_view = (TextView) listItemView.findViewById(R.id.time);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        String timeToDisplay = timeFormatter.format(date);
        time_view.setText(timeToDisplay);

        return listItemView;
    }

    public int getMagnitudeColor(double magnitude)
    {
        switch((int) Math.floor(magnitude)) {
            case 1:
                return ContextCompat.getColor(getContext(), R.color.magnitude1);


            case 2:
                return ContextCompat.getColor(getContext(), R.color.magnitude2);


            case 3:
                return ContextCompat.getColor(getContext(), R.color.magnitude3);


            case 4:
                return ContextCompat.getColor(getContext(), R.color.magnitude4);


            case 5:
                return ContextCompat.getColor(getContext(), R.color.magnitude5);


            case 6:
                return ContextCompat.getColor(getContext(), R.color.magnitude6);


            case 7:
                return ContextCompat.getColor(getContext(), R.color.magnitude7);

            case 8:
                return ContextCompat.getColor(getContext(), R.color.magnitude8);

            case 9:
                return ContextCompat.getColor(getContext(), R.color.magnitude9);

            default:
                return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
        }

    }

}


