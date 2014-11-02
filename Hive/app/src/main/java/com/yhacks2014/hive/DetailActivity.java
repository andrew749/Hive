package com.yhacks2014.hive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
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
        TextView description = (TextView) findViewById(R.id.description);
        nameText.setText(event.name);
        description.setText(event.description);
        duration.setText(format.format(date1) + " - " + format.format(date2));
        LatLng position = new LatLng( Double.parseDouble(event.coordinates[1]),Double.parseDouble(event.coordinates[0]));
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (map != null) {
            Marker hamburg = map.addMarker(new MarkerOptions().position(position)
                    .title(event.name));
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(position);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);

            map.moveCamera(center);
            map.animateCamera(zoom);
        }
        final FloatingActionButton deleteButton=(FloatingActionButton)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences(
                        "Login", Context.MODE_PRIVATE);
                String token = prefs.getString("Token", "0");
                RSVPTask e=new RSVPTask(token);
                e.execute();
            }
        });
    }
    class RSVPTask extends AsyncTask<Void,Void,Void>{

        private String token;

        public RSVPTask(String token1) {
            token = token1;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            token = communicator.validateToken(token);
            communicator.postRSVP(event.id, token);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }
}
