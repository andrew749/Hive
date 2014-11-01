package com.yhacks2014.hive;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;

import org.json.JSONObject;

import java.util.ArrayList;


public class HiveMain extends Activity {
    ArrayList<Event> events=new ArrayList<Event>();
    CardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_main);
        ArrayList<Event> events=new ArrayList<Event>();
        //events will be an array of events
        GridView layout= (GridView) findViewById(R.id.mainlayout);
        adapter=new CardAdapter(getApplicationContext(), events);
        layout.setAdapter(adapter);
        HiveCommunicator communicator=new HiveCommunicator();
        getCats cats=new getCats();
        cats.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hive_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class getCats extends AsyncTask<Void,Void,ArrayList<Event>>{
HiveCommunicator communicator;
        JSONObject result;
        @Override
        protected ArrayList<Event> doInBackground(Void... voids) {
             communicator=new HiveCommunicator();
            Log.d("result","getting");
            result= communicator.getJSONFromUrl("545470d17fd5470b00393775");
            String[]co={"0.0","0.0"};
            Log.d("creating evenet","ww");
            //communicator.createEvent("Andrew",1,2,true,co);
            //communicator.deleteEntry("54548fa669b617fc392ba401");
            return null ;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Info", result.toString());
            Log.d("Latitude/long",communicator.getCoordinates(result)[0]+","+communicator.getCoordinates(result)[1]);
            Log.d("name",communicator.getName(result));
            Log.d("date",communicator.getTime(result).toString());
            adapter=new CardAdapter(getApplicationContext(),aVoid);
            adapter.notifyDataSetChanged();
            Log.d("Done","a");
        }
    }
}
