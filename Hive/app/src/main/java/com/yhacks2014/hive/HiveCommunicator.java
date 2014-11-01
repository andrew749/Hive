package com.yhacks2014.hive;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by andrew on 01/11/14.
 */
public class HiveCommunicator {
    final static String token = "";
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    String URL = "http://hive-events.herokuapp.com/event/info/";
    String URL_CREATE = "http://hive-events.herokuapp.com/event/create/";
    String URL_DELETE = "http://hive-events.herokuapp.com/event/delete/";
    String result = "";

    public HiveCommunicator() {
    }

    public JSONObject getJSONFromUrl(String id) {
        // Making HTTP request
        try {
            // defaultHttpClient
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(URL + id);
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
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }

    //gets the coordinates of the location
    public String[] getCoordinates(JSONObject response) {
        String[] result = new String[2];
        try {
            JSONObject small = response.getJSONObject("location");
            result[0] = small.getJSONArray("coordinates").get(0).toString();
            result[1] = small.getJSONArray("coordinates").get(1).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getName(JSONObject response) {
        try {
            return response.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getVisibility(JSONObject response) {
        try {
            return response.getBoolean("visibility");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Date[] getTime(JSONObject response) {
        Date dateStart = new Date();
        Date dateEnd = new Date();
        try {
            dateStart = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(response.getString("datetime_start"));
            dateEnd = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(response.getString("datetime_end"));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Date[] time = {dateStart, dateEnd};
        return time;
    }

    public boolean createEvent(String name, long start, long end, boolean visibility, String[] coordinates) {
        HttpResponse httpResponse = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_CREATE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("datetime_start", start + ""));
            nameValuePairs.add(new BasicNameValuePair("datetime_end", end + ""));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (httpResponse != null) return true;
        else return false;
    }

    public boolean deleteEntry(long id) {
        HttpResponse httpResponse = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_CREATE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", id + ""));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (httpResponse != null) return true;
        else return false;
    }
}
