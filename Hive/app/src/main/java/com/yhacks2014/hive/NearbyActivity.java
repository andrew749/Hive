/**
 * Created by Sun on 11/1/2014.
 */

package com.yhacks2014.hive;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.GoogleMap;
import com.yhacks2014.hive.api.HiveCommunicator;
import com.yhacks2014.hive.fragments.EventListingFragment;

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