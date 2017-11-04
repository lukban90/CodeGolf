package com.example.lukbanc.codegolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager dbm = new DatabaseManager(this);

        // set button callbacks
        Button btnSelectPuzzle = (Button)findViewById(R.id.btn_puzzle_select);
    }

    public void switchToPuzzleSelect(View v){
        Intent myIntent = new Intent(this, SelectPuzzleActivity.class);
        this.startActivity(myIntent);
    }
}
