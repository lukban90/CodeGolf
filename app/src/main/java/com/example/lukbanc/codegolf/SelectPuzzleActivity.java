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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static java.text.DateFormat.getDateTimeInstance;

/**
 * Created by lukbanc on 11/2/17.
 */

public class SelectPuzzleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_select);

        ListView listView = (ListView)findViewById(R.id.list_view_puzzle_select);

        if(listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                    Puzzle p = (Puzzle) adapter.getItemAtPosition(position);

                    Intent intent = new Intent(v.getContext(), SolutionEditActivity.class);
                    intent.putExtra(DatabaseManager.COL_PUZZLE_ID, p.getPuzzleId());
                    startActivity(intent);
                }
            });
        }
        else{
            Log.e("ProjectSelectActivity", "null projListView");
        }

        upDateView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_select_puzzle, menu);
//
//        //create searchable configuration with the SearchView
//        //Credit mainly given Android App Development with minor tweaks
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(
//                new ComponentName(this, SearchResultsActivity.class)) );
//
//        return true;
//    }

    public Date parseIso8601(String isoDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            //Date result = getDateTimeInstance().parse(isoDate);
            //Instant inst = Instant.parse(isoDate);
            //return Date.from(inst);
            //LocalDateTime ldt = LocalDateTime.parse( isoDate );
            //return ldt;
            return df.parse(isoDate);
        }
        catch (Exception e){
            Log.e("bad date", e.toString());
        }
        return null;
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
                        ArrayList<Puzzle> puzzles = new ArrayList<>();
                        for (int i = 0; i < puzzleCount; i++) {
                            JSONObject obj = result.getJSONObject(i);
                            int id = obj.getInt(DatabaseManager.COL_PUZZLE_ID);
                            String title = obj.getString(DatabaseManager.COL_PUZZLE_TITLE);
                            String added = obj.getString(DatabaseManager.COL_PUZZLE_ADDED);
                            String desc = obj.getString(DatabaseManager.COL_PUZZLE_DESC);
                            Log.d("PARSING DATE", added);
                            Date date = parseIso8601(added);
                            puzzles.add(new Puzzle(id, title, desc, date));
                        }
                        populateListView(puzzles);
                    }
                    catch(Exception e){
                        Log.e("upDateView", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void populateListView(ArrayList<Puzzle> puzzles) {
        /*final Context parent = this;
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
        }*/

        ArrayAdapter<Puzzle> projectArrayAdapter = new PuzzleAdapter(this,
                R.layout.list_view_puzzle_row, puzzles);

        ListView listView = (ListView)findViewById(R.id.list_view_puzzle_select);
        listView.setAdapter(projectArrayAdapter);

        //TextView txtProjCount = (TextView)findViewById(R.id.txt_proj_count);
        //Integer projCount = puzzles.size();
        //txtProjCount.setText(projCount.toString());
    }

    public void goBack(View v) {
        this.finish();
    }
}
