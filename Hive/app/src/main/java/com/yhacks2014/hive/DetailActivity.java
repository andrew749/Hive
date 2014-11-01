package com.yhacks2014.hive;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yhacks2014.hive.api.HiveCommunicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andrew on 01/11/14.
 */
public class DetailActivity extends ActionBarActivity {
    Event event;
    GoogleMap map;
    HiveCommunicator communicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaillayout);
        event = (Event) getIntent().getSerializableExtra("data");
        DateFormat format=new SimpleDateFormat("MMM dd yyyy");
        Date date1,date2;
         communicator=new HiveCommunicator();
        date1 = new Date(event.startTime);
        date2=new Date(event.endtime);
        TextView nameText = (TextView) findViewById(R.id.namePlace);
        TextView duration = (TextView) findViewById(R.id.durationView);
        nameText.setText(event.name);
        duration.setText(format.format(date1) + " - " + format.format(date2));
        LatLng position = new LatLng(Double.parseDouble(event.coordinates[0]), Double.parseDouble(event.coordinates[1]));
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (map != null) {
            Marker hamburg = map.addMarker(new MarkerOptions().position(position)
                    .title(event.name));

        }
        final Button deleteButton=(Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            deleteEntry e=new deleteEntry();
                e.execute();
            }
        });
    }
    class deleteEntry extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            communicator.deleteEntry(event.id,communicator.loginWithCredentials("me@me.com", "abc123"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }
}
