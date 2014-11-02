package com.yhacks2014.hive;

import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.yhacks2014.hive.fragments.DrawerFragment;
import com.yhacks2014.hive.fragments.EventListingFragment;
import com.yhacks2014.hive.fragments.RSVPListingFragment;

import java.util.ArrayList;


public class HiveMainActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, EventListingFragment.OnFragmentInteractionListener, DrawerFragment.OnFragmentInteractionListener, RSVPListingFragment.OnFragmentInteractionListener{
    ArrayList<Event> events=new ArrayList<Event>();
    CardAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private LocationClient mLocationClient;

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

        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        //inflater.inflate(R.menu.hive_main,menu);
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
                /* mNearbyFragment = (RSVPFragment) getSupportFragmentManager().findFragmentByTag("NearbyFragment");
                if(mNearbyFragment == null){
                    //set up map
                    mNearbyFragment = RSVPFragment.newInstance(null, null);
                   // getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,mNearbyFragment,"NearbyFragment").commit();
                } */
                break;
            case 2:
                break;

        }
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
        }
    }

        /*
         * Called when the Activity becomes visible.
         */
        @Override
        protected void onStart() {
            super.onStart();
            // Connect the client.
            mLocationClient.connect();
        }

        /*
         * Called when the Activity is no longer visible.
         */
        @Override
        protected void onStop() {
            // Disconnecting the client invalidates it.
            mLocationClient.disconnect();
            super.onStop();
        }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public Location getLocation(){
        if(mLocationClient.isConnected())
        return mLocationClient.getLastLocation();
        else return null;
    }
}
