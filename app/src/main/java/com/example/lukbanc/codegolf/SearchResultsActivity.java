package com.example.lukbanc.codegolf;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by lukbanc on 11/7/17.
 */

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add layout here
        setContentView(R.layout.activity_solution_edit);
        handleIntent(getIntent());
    }

    public void updateView() {}

    public void goBack(View v) {
        this.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(SearchResultsActivity.this, query, Toast.LENGTH_LONG).show();
        }
    }

}
