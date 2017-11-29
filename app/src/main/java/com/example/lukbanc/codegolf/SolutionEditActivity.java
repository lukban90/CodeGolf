package com.example.lukbanc.codegolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

public class SolutionEditActivity extends AppCompatActivity {

    DatabaseManager dbm;
    int puzzleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_edit);

        puzzleId = getIntent().getIntExtra(DatabaseManager.COL_PUZZLE_ID, 0);
        dbm = new DatabaseManager(this);

        updateView();
    }

    public void updateView() {
        String url = dbm.getSelectPuzzleByIdUrl(puzzleId);
        dbm.fetchJson(url, new JsonAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject result) {
                if(result != null){
                    Log.d("YAY","do something with the result.");
                    Puzzle p = new Puzzle();
                    try {
                        p.setPuzzleId(result.getInt(DatabaseManager.COL_PUZZLE_ID));
                        p.setPuzzleTitle(result.getString(DatabaseManager.COL_PUZZLE_TITLE));
                        p.setDescription(result.getString(DatabaseManager.COL_PUZZLE_DESC));
                    }
                    catch (Exception e) {
                    }
                    String pInfo = String.format(Locale.ENGLISH,
                            "(%d, %s, %s)", p.getPuzzleId(), p.getPuzzleTitle(), p.getDescription());
                    Log.d("PUZZLE INFO", pInfo);

                    populatePuzzleText(p);
                }
            }
            @Override
            public void onTaskCompleted(JSONArray result) {
                if(result != null){
                    return;
                }
            }
        });
    }

    public void populatePuzzleText(Puzzle puzzle) {
        TextView nameTV  = (TextView) findViewById(R.id.puzzle_problem);
        TextView titleTV = (TextView) findViewById(R.id.title);

        titleTV.setText(puzzle.getPuzzleTitle());
        nameTV.setText(puzzle.getDescription());
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
