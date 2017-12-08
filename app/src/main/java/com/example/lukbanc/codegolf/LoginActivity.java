package com.example.lukbanc.codegolf;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    DatabaseManager dbm;
    Context context = this;
    String signingUp = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbm = new DatabaseManager(this);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_sign_in:
                signingUp = "false";
                break;
            case R.id.radio_sign_up:
                signingUp = "true";
                break;
        }
    }

    public void submit(View v) {
        EditText nameET = (EditText) findViewById(R.id.username);
        EditText passET = (EditText) findViewById(R.id.password);

        String email = nameET.getText().toString();
        String pass = passET.getText().toString();

        String baseUrl = "http://67.171.28.34/py/user_login.py";
        String url = String.format("%s?%s=%s&%s=%s&%s=%s",baseUrl, DatabaseManager.COL_EMAIL, email, "pwd", pass, DatabaseManager.COL_IS_NEW, signingUp);

        Log.d("URL: ",url);

        dbm.fetchJson(url, new JsonAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject result) {
                if(result != null){
                    try {
                        int id = result.getInt(DatabaseManager.COL_USER_ID);
                        MainActivity.setUserId(id);
                        finish();
                    }catch (Exception e){
                        Log.e("task complete", e.toString());
                    }
                }
            }
            @Override
            public void onTaskCompleted(JSONArray result) {
                return;
            }
        });
    }
}
