/**
 * Created by Sun on 11/1/2014.
 */

package com.yhacks2014.hive;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.yhacks2014.hive.fragments.EventListingFragment;
import com.yhacks2014.hive.fragments.NearbyFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NearbyActivity extends ActionBarActivity {
    Event event;
    GoogleMap map;
    HiveCommunicator communicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearbyactivity);
        EventListingFragment eventListingFragment=new EventListingFragment();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.nearbylist,eventListingFragment).commit();


    }
}