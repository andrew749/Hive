package com.yhacks2014.hive;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_main);
        ArrayList<Event> events=new ArrayList<Event>();
        //events will be an array of events
        events.add(new Event(0,0,"Hello",null));
        events.add(new Event(0,0,"RuoTaiSun",null));
        GridView layout= (GridView) findViewById(R.id.mainlayout);
        CardAdapter adapter=new CardAdapter(getApplicationContext(), events);
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
    class getCats extends AsyncTask<Void,Void,Void>{
HiveCommunicator communicator;
        JSONObject result;
        @Override
        protected Void doInBackground(Void... voids) {
             communicator=new HiveCommunicator();
            Log.d("result","getting");
            result= communicator.getJSONFromUrl("545470d17fd5470b00393775");
            String[]co={"0.0","0.0"};
            Log.d("creating evenet","ww");
            communicator.createEvent("Andrew",1,2,true,co);

            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Info",result.toString());
            Log.d("Latitude/long",communicator.getCoordinates(result)[0]+","+communicator.getCoordinates(result)[1]);
            Log.d("name",communicator.getName(result));
            Log.d("date",communicator.getTime(result).toString());
        }
    }
}
