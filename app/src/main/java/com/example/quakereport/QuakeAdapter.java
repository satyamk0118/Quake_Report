package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

public class QuakeAdapter extends ArrayAdapter<Quake> {
    public QuakeAdapter(Context context, ArrayList<Quake> list)
    {
       super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView=convertView;
        if(rootView==null)
        {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Quake currentQuake= getItem(position);
        TextView mag, loc, date, time, offset;
        mag=(TextView)rootView.findViewById(R.id.magnitude);
        offset = (TextView)rootView.findViewById(R.id.offset);
        loc=(TextView)rootView.findViewById(R.id.primary_loc);
        date=(TextView)rootView.findViewById(R.id.date);
        time = (TextView)rootView.findViewById(R.id.time);

        double magnitude = currentQuake.getmMagnitude();
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        String outMag = decimalFormatter.format(magnitude);
        mag.setText(outMag);

        String location = currentQuake.getmLocation();
        String priloc, offloc;
        int l= location.length();
        if(location.contains("of"))
        {
            int diff=location.indexOf("of");
            offloc=location.substring(0,diff+2);
            priloc=location.substring(diff+3,l-1);
            offset.setText(offloc);
        }
        else
        {
            priloc=location;
        }

        loc.setText(priloc);
        Date dateObject = new Date(currentQuake.getmTimeInMilliSec());
        String dateToDisplay = formatDate(dateObject);
        date.setText(dateToDisplay);

        String timeToDisplay = formatTime(dateObject);
        time.setText(timeToDisplay);

        GradientDrawable magnitudeCircle = (GradientDrawable)mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return rootView;
    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private int getMagnitudeColor(double mag)
    {
        int colorId;
        int M=(int)mag;
        switch(M)
        {
            case 0:
            case 1:
                colorId=R.color.magnitude1;
                break;
            case 2:
                colorId=R.color.magnitude2;
                break;
            case 3:
                colorId=R.color.magnitude3;
                break;
            case 4:
                colorId=R.color.magnitude4;
                break;
            case 5:
                colorId=R.color.magnitude5;
                break;
            case 6:
                colorId=R.color.magnitude6;
                break;
            case 7:
                colorId=R.color.magnitude7;
                break;
            case 8:
                colorId= R.color.magnitude8;
                break;
            case 9:
                colorId=R.color.magnitude9;
                break;
            default:
                colorId=R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(),colorId);
    }
}
