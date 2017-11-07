package com.example.lukbanc.codegolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
