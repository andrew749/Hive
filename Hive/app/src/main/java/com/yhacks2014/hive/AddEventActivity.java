package com.yhacks2014.hive;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.yhacks2014.hive.api.HiveCommunicator;
import com.yhacks2014.hive.fragments.TimePickerFragment;

/**
 * Created by andrew on 01/11/14.
 */
public class AddEventActivity extends ActionBarActivity implements TimePickerFragment.OnFragmentInteractionListener {
    EditText location, name;
    Spinner time;
    Button create,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent);
        location=(EditText)findViewById(R.id.editText3);
        time=(Spinner)findViewById(R.id.spinner);

        name=(EditText)findViewById(R.id.eventName);
        create=(Button)findViewById(R.id.button);
        delete=(Button)findViewById(R.id.button2);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FragmentManager fm = getSupportFragmentManager();
                    TimePickerFragment editNameDialog = new TimePickerFragment();
                    editNameDialog.show(fm, "fragment_edit_time");
                }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
