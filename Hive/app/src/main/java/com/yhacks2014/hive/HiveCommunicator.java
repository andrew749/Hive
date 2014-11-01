package com.yhacks2014.hive;




import android.location.Location;
import android.text.format.Time;
import android.util.Log;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by andrew on 01/11/14.
 */
public class HiveCommunicator {
    final static String token="";
    public HiveCommunicator(){
    }
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    String URL = "http://hive-events.herokuapp.com/event/info/";
    String result = "";
    public JSONObject getJSONFromUrl(String id) {
        // Making HTTP request
        try {
            // defaultHttpClient
            HttpClient httpClient = new DefaultHttpClient( );
            HttpGet httpPost = new HttpGet(URL+id);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            JSONArray a=new JSONArray(json);
            jObj = a.getJSONObject(0);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }
    //gets the coordinates of the location
    public String[] getCoordinates(JSONObject response){
        String [] result=new String[2];
        try{
            JSONObject small=response.getJSONObject("location");
            result[0]=small.getJSONArray("coordinates").get(0).toString();
            result[1]=small.getJSONArray("coordinates").get(1).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    public String getName(JSONObject response){
        try {
            return response.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean getVisibility(JSONObject response){
        try {
            return response.getBoolean("visibility");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Date getTime(JSONObject response){
        Date date=new Date();
        try {
            date=new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(response.getString("datetime"));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date;
    }

}
