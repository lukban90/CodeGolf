package com.example.lukbanc.codegolf;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * Created by lukbanc on 11/2/17.
 */

public class SelectPuzzleActivity extends AppCompatActivity {
    private ListView listView;

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

    public void goBack(View v) {
        this.finish();
    }
}
