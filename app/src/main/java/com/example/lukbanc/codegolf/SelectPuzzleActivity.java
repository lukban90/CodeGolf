package com.example.lukbanc.codegolf;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by lukbanc on 11/2/17.
 */

public class SelectPuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_select);

        upDateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_puzzle, menu);

        //create searchable configuration with the SearchView
        //Credit mainly given Android App Development with minor tweaks
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchResultsActivity.class)) );

        return true;
    }



    public void upDateView() {

        String puzzleIndexUrl = "http://67.171.28.34/py/get_puzzle_index.py";
        DatabaseManager.fetchJson(puzzleIndexUrl, new JsonAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject result) {
                return;
            }
            @Override
            public void onTaskCompleted(JSONArray result) {
                if(result != null){
                    try {
                        int puzzleCount = result.length();
                        Puzzle[] titles = new Puzzle[puzzleCount];
                        for (int i = 0; i < puzzleCount; i++) {
                            JSONObject obj = result.getJSONObject(i);
                            int id = obj.getInt(DatabaseManager.COL_PUZZLE_ID);
                            String title = obj.getString(DatabaseManager.COL_PUZZLE_TITLE);
                            titles[i] = new Puzzle(id, title, "", new Date());
                        }
                        populateListView(titles);
                    }
                    catch(Exception e){
                        Log.e("upDateView", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void populateListView(Puzzle[] titles) {
        final Context parent = this;
        LinearLayout ll = (LinearLayout) findViewById(R.id.puzzle_list);
        for (int i = 0; i < titles.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(titles[i].getPuzzleTitle());
            tv.setTextSize(30);
            tv.setId(titles[i].getPuzzleId());
            tv.setPadding(0, 20, 0, 20);
            tv.setGravity(Gravity.CENTER);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(parent, SolutionEditActivity.class);
                    intent.putExtra(DatabaseManager.COL_PUZZLE_ID, view.getId());
                    startActivity(intent);
                }
            });
            ll.addView(tv);
        }
    }

    public void goBack(View v) {
        this.finish();
    }
}
