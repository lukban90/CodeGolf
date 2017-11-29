package com.example.lukbanc.codegolf;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kyle on 11/28/2017.
 */
class JsonAsyncTask extends AsyncTask<String, Void, Boolean> {

    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    private String requestMethod = REQUEST_METHOD_GET;

    public interface OnTaskCompleted {
        void onTaskCompleted(JSONObject result);
        void onTaskCompleted(JSONArray result);
    }

    private String postData;

    public void setRequestMethod(String method){
        requestMethod = method;
    }

    private OnTaskCompleted listener = null;

    public void setTaskCompletedListener(OnTaskCompleted listener){
        this.listener = listener;
    }

    public void setPostData(String postData){
        this.postData = postData;
    }

    protected void onPreExecute() {
        super.onPreExecute();

    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream),1024);
        String line;
        do {
            line = reader.readLine();
            if(line != null)
                sb.append(line);
        } while(line != null);
        stream.close();
        return sb.toString();
    }

    public JSONObject getJsonObject(){return jsonObject;}

    @Override
    protected Boolean doInBackground(String ... urls) {
        try {

            //------------------>>
            if(urls.length < 1)
                return false;
            String urlString = urls[0];
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod(requestMethod);

            if(requestMethod.equals(REQUEST_METHOD_POST)){
                int len = postData.length();
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(len));
                urlConnection.setDoOutput(true);
                OutputStream outStream = urlConnection.getOutputStream();
                outStream.write(postData.getBytes());
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String content = readStream(in);
            if(content.charAt(0)=='['){
                // parse it as an array
                jsonArray = new JSONArray(content);
            }
            else if(content.charAt(0)=='{'){
                jsonObject = new JSONObject(content);
            }
            else{
                Log.e("RESPONSE NOT JSON", content);
            }
            return true;

        } catch (Exception e) {
            Log.e("json fetch", e.toString());
            e.printStackTrace();
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {
        if(listener != null) {
            if(jsonObject != null){
                listener.onTaskCompleted(jsonObject);
            }
            else if(jsonArray != null){
                listener.onTaskCompleted(jsonArray);
            }
        }
    }
}