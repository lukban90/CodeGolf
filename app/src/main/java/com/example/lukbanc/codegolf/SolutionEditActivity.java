package com.example.lukbanc.codegolf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class SolutionEditActivity extends AppCompatActivity {

    DatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_edit);

        dbm = new DatabaseManager(this);
    }

    public void sendSolution() {
        EditText et = (EditText) findViewById(R.id.puzzle_soln);
        String solution = et.getText().toString();

        dbm.fetchJson("67.171.28.34/py/submit_solution.py", new JsonAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject result) {

            }

            @Override
            public void onTaskCompleted(JSONArray result) {

            }
        });
    }
}
