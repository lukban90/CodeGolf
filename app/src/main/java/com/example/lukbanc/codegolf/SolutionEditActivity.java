package com.example.lukbanc.codegolf;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;

import static com.example.lukbanc.codegolf.DatabaseManager.COL_PUZZLE_ID;
import static com.example.lukbanc.codegolf.DatabaseManager.COL_SOLUTION_TEXT;

public class SolutionEditActivity extends AppCompatActivity {

    DatabaseManager dbm;
    private CustomKeyboard customKeyboard;
    int puzzleId;

    public static EditText solnEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_edit);

        solnEditText = findViewById( R.id.puzzle_soln );

        puzzleId = getIntent().getIntExtra(COL_PUZZLE_ID, 0);
        dbm = new DatabaseManager(this);


        //add layout here
        setContentView(R.layout.activity_solution_edit);
        handleIntent(getIntent());

        //register customKeyboard and add functionality
        customKeyboard = new CustomKeyboard( this, R.id.keyboardview, R.xml.keyboard );
        customKeyboard.registerEditText( R.id.puzzle_soln );

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
                        p.setPuzzleId(result.getInt(COL_PUZZLE_ID));
                        p.setPuzzleTitle(result.getString(DatabaseManager.COL_PUZZLE_TITLE));
                        p.setDescription(result.getString(DatabaseManager.COL_PUZZLE_DESC));
                        p.setTargFuncName(result.getString(DatabaseManager.COL_TARG_FUNC_NAME));
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

    public void onSubmit(View view){
        sendSolution();
    }

    public void populatePuzzleText(Puzzle puzzle) {
        TextView nameTV  = (TextView) findViewById(R.id.puzzle_problem);
        TextView titleTV = (TextView) findViewById(R.id.title);
        TextView funcNameTv = (TextView) findViewById(R.id.val_target_function);
        funcNameTv.setText(puzzle.getTargFuncName());
        titleTV.setText(puzzle.getPuzzleTitle());
        nameTV.setText(puzzle.getDescription());
    }

    public void sendSolution() {
        EditText et = findViewById(R.id.puzzle_soln);

        String solText = et.getText().toString();
        String baseUrl = "http://67.171.28.34/py/submit_solution.py";

        String encoded = URLEncoder.encode(solText);
        String url = String.format("%s?%s=%s&%s=%s",baseUrl, DatabaseManager.COL_PUZZLE_ID, puzzleId, DatabaseManager.COL_SOLUTION_TEXT, encoded);

        Log.d("URL: ",url);

        dbm.fetchJson(url, new JsonAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject result) {
                if(result != null){
                    Log.d("YAY","do something with the result.");
                    try {
                        String output = result.getString("output");
                        displayResult(output);
                    }catch (Exception e){
                        Log.e("task complete", e.toString());
                    }
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

    public void displayResult(String output) {
        RelativeLayout wrapper = findViewById(R.id.wrapper);
        TextView tv = findViewById(R.id.output);

        //textView.setText(Html.fromHtml(Html.fromHtml(text).toString()))
        tv.setText(output);
    }

    public static EditText getEditText() {
        return solnEditText;
    }

    // onClick to open custom keyboard
    public void onCustomKeyboard( View v ) {
        customKeyboard.openKeyboard( v );
    }

    // onClick to open default keyboard
    public void onDefaultKeyboard( View v ) {
        if(v != null) {
            try {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            catch(Exception e){
                Log.e("onDefaultKeyboard", e.toString());
                e.printStackTrace();
            }
        }
    }

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
            Toast.makeText(SolutionEditActivity.this, query, Toast.LENGTH_LONG).show();
        }
    }

}
