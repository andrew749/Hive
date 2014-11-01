package com.yhacks2014.hive;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andrew on 31/10/14.
 */
public class CardAdapter extends BaseAdapter {
    ArrayList<Event> events=new ArrayList<Event>();
    Context context;
    public CardAdapter(Context context, ArrayList<Event> events){
        this.events=events;
        this.context=context;
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
        view=inflater.inflate(R.layout.maincard,null,false);
        //TextView tv=(TextView)view.findViewById(R.id.nameText);
        //tv.setText(events.get(i).name);
        return view;
    }
}
