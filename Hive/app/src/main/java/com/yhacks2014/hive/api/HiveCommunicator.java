package com.yhacks2014.hive.api;


import android.util.Log;

import com.yhacks2014.hive.Event;

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
import org.json.JSONArray;
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
   public String email = "me@me.com";
    public String password = "abc123";
    String URL = "http://hive-events.herokuapp.com/event/info/";
    String URL_CREATE = "http://hive-events.herokuapp.com/event/create/";
    String URL_DELETE = "http://hive-events.herokuapp.com/event/delete/";
    String result = "";
    String URL_LOGIN = "http://hive-events.herokuapp.com/api/login/";
    String URL_GETALL = "http://hive-events.herokuapp.com/event/byUser/";

    public HiveCommunicator() {
    }

    public String loginWithCredentials(String email, String password) {
        HttpResponse httpResponse = null;
        HttpEntity httpEntity=null;
        String json=null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_LOGIN);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));


            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
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
        JSONObject object = new JSONObject();
        String token = null;
        try {
            object = new JSONObject(json);
token=object.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;

    }

    public JSONObject getEventInfo(String id) {
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
        JSONObject array = new JSONObject();
        try {
            array = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public JSONArray getAllUserEvents(String token) {
        HttpResponse httpResponse = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_GETALL);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("access_token", token));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setHeader("x-access-token", token);

            httpResponse = httpClient.execute(httpPost);
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
        JSONArray array = new JSONArray();
        try {
            array = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
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

    public boolean createEvent(String name, long start, long end, boolean visibility, String[] coordinates, String token) {
        HttpResponse httpResponse = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_CREATE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("datetime_start", start + ""));
            nameValuePairs.add(new BasicNameValuePair("datetime_end", end + ""));
            nameValuePairs.add(new BasicNameValuePair("access_token", token));
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

    public boolean deleteEntry(String id, String token) {
        HttpResponse httpResponse = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_DELETE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", id + ""));
            nameValuePairs.add(new BasicNameValuePair("access_token", token));
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

    public ArrayList<Event> getEvents(JSONArray response) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (int i = 0; i < response.length(); i++) {
            try {
                events.add(new Event(getTime(response.getJSONObject(i))[0].getTime(), getTime(response.getJSONObject(i))[1].getTime(), getName(response.getJSONObject(i)), getCoordinates(response.getJSONObject(i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }
}
