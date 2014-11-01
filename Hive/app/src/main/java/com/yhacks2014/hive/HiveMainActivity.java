package com.yhacks2014.hive;

import android.app.Activity;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;

import com.google.android.gms.maps.SupportMapFragment;
import com.yhacks2014.hive.fragments.DrawerFragment;
import com.yhacks2014.hive.fragments.EventListingFragment;
import com.yhacks2014.hive.fragments.NearbyFragment;

import org.json.JSONObject;

import java.util.ArrayList;


public class HiveMainActivity extends ActionBarActivity implements EventListingFragment.OnFragmentInteractionListener, DrawerFragment.OnFragmentInteractionListener, NearbyFragment.OnFragmentInteractionListener{
    ArrayList<Event> events=new ArrayList<Event>();
    CardAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NearbyFragment mNearbyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle= new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,EventListingFragment.newInstance(null,null)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.hive_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(int id) {
    //callback from drawer
        switch(id){
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,EventListingFragment.newInstance(null,null)).commit();
                break;
            case 1:
                mNearbyFragment = (NearbyFragment) getSupportFragmentManager().findFragmentByTag("NearbyFragment");
                if(mNearbyFragment == null){
                    //set up map
                    mNearbyFragment = NearbyFragment.newInstance(null,null);
                   // getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,mNearbyFragment,"NearbyFragment").commit();
                }
                break;
            case 2:
                break;

        }
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
        }
    }
}
