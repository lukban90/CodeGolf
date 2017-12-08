package com.example.lukbanc.codegolf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager dbm = new DatabaseManager(this);
         //dbm.testREST();
        dbm.testREST_POST();
        // set button callbacks
        Button btnSelectPuzzle = (Button)findViewById(R.id.btn_puzzle_select);
    }

    public void switchToLogin(View v) {
        Intent myIntent = new Intent(this, LoginActivity.class);
        this.startActivity(myIntent);
    }

    public void switchToPuzzleSelect(View v){
        if (userId == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Please sign in first!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Intent myIntent = new Intent(this, SelectPuzzleActivity.class);
            this.startActivity(myIntent);
        }
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }
}
