package com.yhacks2014.hive;

import android.location.Location;
import android.opengl.Visibility;

/**
 * Created by andrew on 31/10/14.
 */
public class Event {
    /**
     * the Event datatype to be passed to all parameters
     *
     * @param startTime The start time
     * @param endTime  The end time of the event
     * @param name     The name of the event
     */
    String name;
    long startTime;
    long endtime;
    String[] coordinates;
    public Event(long startTime,long endTime, String name,String[] coordinates){
        this.name=name;
        this.startTime=startTime;
        this.endtime=endTime;
        this.coordinates=coordinates;
    }

}
