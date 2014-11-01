/**
 * Created by Sun on 11/1/2014.
 */

package com.yhacks2014.hive;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.yhacks2014.hive.fragments.NearbyFragment;

public class NearbyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearbyactivity);
        NearbyFragment fragment=new NearbyFragment();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.map,fragment).commit();
    }
}