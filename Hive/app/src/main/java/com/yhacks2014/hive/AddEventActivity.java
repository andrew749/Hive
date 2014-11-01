package com.yhacks2014.hive;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yhacks2014.hive.api.HiveCommunicator;

/**
 * Created by andrew on 01/11/14.
 */
public class AddEventActivity extends ActionBarActivity {
    EditText location, time,name;
    Button create,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent);
        location=(EditText)findViewById(R.id.editText3);
        time=(EditText)findViewById(R.id.time);
        name=(EditText)findViewById(R.id.eventName);
        create=(Button)findViewById(R.id.button);
        delete=(Button)findViewById(R.id.button2);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
class CreateEvent extends AsyncTask<Void,Void,Void>{
    Event event;
    CreateEvent(Event event) {
        this.event=event;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HiveCommunicator communicator=new HiveCommunicator();
        communicator.createEvent(event.name,event.startTime,event.endtime,true,event.coordinates,communicator.loginWithCredentials("me@me.com","abc123"));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        finish();
    }
}
}
