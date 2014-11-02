package com.yhacks2014.hive;

import android.content.Context;
import android.location.Location;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by andrew on 31/10/14.
 */
public class CardAdapter extends BaseAdapter {
    private final Location myLoc;
    ArrayList<Event> events=new ArrayList<Event>();
    Context context;
    public CardAdapter(Context context, ArrayList<Event> events, Location myLoc){
        this.events=events;
        this.context=context;
        this.myLoc = myLoc;
    }
    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.maincard,viewGroup,false);
        TextView tv=(TextView)view.findViewById(R.id.nameText);
        tv.setText(events.get(i).name);

        //Location
        Location loc = new Location("dummyprovider");
        loc.setLatitude(Double.valueOf(events.get(i).coordinates[1]));
        loc.setLongitude(Double.valueOf(events.get(i).coordinates[0]));

        TextView locationtv=(TextView)view.findViewById(R.id.Location);
        locationtv.setText(myLoc.distanceTo(loc) + " " + myLoc.bearingTo(loc));
        //locationtv.setText(events.get(i).coordinates[0]+","+events.get(i).coordinates[1]);
        TextView timetv=(TextView)view.findViewById(R.id.timetext);
        DateFormat format=new SimpleDateFormat("MMM dd HH:MM");
        Date date= null;
        date = new Date(events.get(i).startTime);

            timetv.setText(format.format(date));


        return view;
    }
}
