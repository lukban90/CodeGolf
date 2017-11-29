package com.example.lukbanc.codegolf;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Entity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukbanc on 11/2/17.
 */

public class SelectPuzzleActivity extends AppCompatActivity {
    private ListView listView;
    private HashMap<Integer, String> puzzleMap = new HashMap<>();
    private boolean puzzleIndexLoaded = false;

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
                        for (int i = 0; i < puzzleCount; i++) {
                            JSONObject obj = result.getJSONObject(i);
                            int id = obj.getInt(DatabaseManager.COL_PUZZLE_ID);
                            String title = obj.getString(DatabaseManager.COL_PUZZLE_TITLE);
                            if(!puzzleMap.containsKey(id))
                                puzzleMap.put(id, title);

                        }
                        puzzleIndexLoaded = true;
                    }
                    catch(Exception e){
                        Log.e("upDateView", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });

        //Replace dummies with actual puzzles
        String[] values = new String[] {"Chris is the best",
                "Chirs is number one",
                "Chirs is number one",
                "Chirs is number one",
                "Heroes for hire",
                "Heroes for hire",
                "Heroes for hire",
                "Chirs is number one",
                "Chirs is number one",
                "Heroes for hire",
                "Chirs is number one",
                "Marvel buys Fox",
                "Marvel buys Fox",
                "Marvel buys Fox",
                "Marvel buys Fox",
                "Heroes for hire",
                "Heroes for hire",
                "Heroes for hire",
                "Heroes for hire",
                "Heroes for hire",
                "Marvel buys Fox",
                "Marvel buys Fox",
                "Chirs is number one",
                "Give me back FF4"};

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview_sample,
                values);

        listView = (ListView)findViewById(R.id.list_puzzle_select);
        listView.setAdapter(myAdapter);
    }

    public void populateListView(){
        if(!puzzleIndexLoaded){
            // can't populate view yet...
            return;
        }
        ArrayAdapter<Map.Entry<Integer, String>> adapter =
                new ArrayAdapter<Map.Entry<Integer, String>>(this){

                }
    }

    public void goBack(View v) {
        this.finish();
    }
}
