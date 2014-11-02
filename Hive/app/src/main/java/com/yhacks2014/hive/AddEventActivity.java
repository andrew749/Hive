package com.yhacks2014.hive;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.yhacks2014.hive.api.HiveCommunicator;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andrew on 01/11/14.
 */
public class AddEventActivity extends ActionBarActivity implements  View.OnClickListener {
    EditText location, name;
    TextView startdate,starttime,finishdate,finishtime;
    DateFormat format;
    int dateState=0;
    Button create,delete;
    public static final String DATEPICKER_TAG = "Pick a Date";
    public static final String TIMEPICKER_TAG = "Pick a Time";
    public static final String DATEPICKEREND = "Pick an End Date";
    public static final String TIMEPICKEREND = "Pick a End Time";
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent);
datestart =new Date();
dateend=new Date();
        location=(EditText)findViewById(R.id.editText3);
        starttime=(TextView)findViewById(R.id.starttime);
        startdate=(TextView)findViewById(R.id.startdate);
        finishdate=(TextView)findViewById(R.id.finishdate);
        finishtime=(TextView)findViewById(R.id.finishtime);
        name=(EditText)findViewById(R.id.eventName);
        create=(Button)findViewById(R.id.button);
        delete=(Button)findViewById(R.id.button2);
        startdate.setOnClickListener(this);
        starttime.setOnClickListener(this);
        finishtime.setOnClickListener(this);
        finishdate.setOnClickListener(this);
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog();
        datePickerDialog=DatePickerDialog.newInstance(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
         timePickerDialog = com.sleepbot.datetimepicker.time.TimePickerDialog.newInstance(null, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, true);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });
    delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startdate:
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {
                        datestart.setDate(i3);
                        datestart.setMonth(i2);
                        datestart.setYear(i);
                        startdate.setText(datestart.toLocaleString());

                    }
                });
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

                break;
            case R.id.starttime:
                timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {
                        datestart.setHours(i);
                        datestart.setMinutes(i2);
                        starttime.setVisibility(View.INVISIBLE);
                        startdate.setText(datestart.toLocaleString());

                    }
                });
                timePickerDialog.show(getSupportFragmentManager(),TIMEPICKER_TAG);

                break;
            case R.id.finishdate:
                datePickerDialog.show(getSupportFragmentManager(),DATEPICKEREND);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {
                        dateend.setYear(i);
                        dateend.setMonth(i2);
                        dateend.setDate(i3);
                        finishdate.setText(dateend.toLocaleString());
                    }
                });
                break;
            case R.id.finishtime:
                timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {
                        dateend.setHours(i);
                        dateend.setMinutes(i2);
                        finishtime.setVisibility(View.INVISIBLE);
                        finishdate.setText(dateend.toLocaleString());
                    }
                });
                timePickerDialog.show(getSupportFragmentManager(),TIMEPICKEREND);
                break;
        }
    }
    public void createEvent(){
        createEventTask c=new createEventTask();
        c.execute();
    }
Date datestart,dateend;

class createEventTask extends AsyncTask<Void,Void,Void>{

    @Override
    protected Void doInBackground(Void... voids) {
        HiveCommunicator communicator=new HiveCommunicator();
        String[] coordinates={"-72","41"};
        communicator.createEvent(name.getText().toString(), datestart.getTime(),dateend.getTime(),true,coordinates ,communicator.loginWithCredentials("me@me.com","abc123"));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        finish();
    }
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
