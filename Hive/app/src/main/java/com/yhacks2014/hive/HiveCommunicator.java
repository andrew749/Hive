package com.yhacks2014.hive;


import android.net.Uri;
import android.util.Log;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by andrew on 01/11/14.
 */
public class HiveCommunicator {
    final static String token="";
    public HiveCommunicator(){

    }

    String URL = "http://hive-events.herokuapp.com/event/info/";
    String result = "";
    public void getEventInfo(String id){
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);
        HttpParams params=request.getParams();
        params.setParameter("id",id);
request.setParams(params);
                ResponseHandler<String> handler = new BasicResponseHandler();
        try {
            result = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();
    } // end callWebService()
}
