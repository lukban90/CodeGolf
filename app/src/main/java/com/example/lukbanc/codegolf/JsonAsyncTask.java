package com.example.lukbanc.codegolf;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kyle on 11/28/2017.
 */
class JsonAsyncTask extends AsyncTask<String, Void, Boolean> {

    private JSONObject jsonObject = null;

    public interface OnTaskCompleted {
        void onTaskCompleted(JSONObject result);
    }

    private OnTaskCompleted listener = null;

    public void setTaskCompletedListener(OnTaskCompleted listener){
        this.listener = listener;
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
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String content = readStream(in);
            jsonObject = new JSONObject(content);
            return true;

        } catch (Exception e) {
            Log.e("json fetch", e.toString());
            e.printStackTrace();
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {
        if(listener != null)
            listener.onTaskCompleted(jsonObject);
    }
}