package com.example.lukbanc.codegolf;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
    int puzzleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_edit);

        puzzleId = getIntent().getIntExtra(COL_PUZZLE_ID, 0);
        dbm = new DatabaseManager(this);


        //add layout here
        setContentView(R.layout.activity_solution_edit);
        handleIntent(getIntent());

        // Create the keyboard
        keyboard = new Keyboard(this, R.xml.keyboard);
        keyboardView = (KeyboardView)findViewById(R.id.keyboardview);
        keyboardView.setKeyboard( keyboard );

        //registerEditText(R.id.puzzle_soln);

        //keyboardView.setOnKeyboardActionListener(myKeyboardActionListener);
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

    private Keyboard keyboard;
    private KeyboardView keyboardView;

    // onClick to open keyboard
    public void openKeyoard(View v) {
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setEnabled(true);
        if(v != null) {
            ((InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    v.getWindowToken(), 0);

            //registerEditText(R.id.puzzle_soln);
        }
    }

    // register EditText to the keyboard
    public void registerEditText(int resId) {
        EditText editText = (EditText)findViewById(resId);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    showCustomKeyboard(view);
                else
                    hideCustomKeyboard();
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomKeyboard(view);
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        editText.setInputType( editText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
    }

    // helpers to hide/show/check customboard
    public void hideCustomKeyboard() {
        keyboardView.setVisibility(View.GONE);
        keyboardView.setEnabled(false);
    }

    public void showCustomKeyboard( View v ) {
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setEnabled(true);
        if( v!=null ) ((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public boolean isCustomKeyboardVisible() {
        return keyboardView.getVisibility() == View.VISIBLE;
    }

    // Functionality of the keyboard
    private KeyboardView.OnKeyboardActionListener myKeyboardActionListener =
            new KeyboardView.OnKeyboardActionListener() {
                @Override
                public void onPress(int i) {

                }

                @Override
                public void onRelease(int i) {

                }

                @Override
                public void onKey(int i, int[] ints) {

                }

                @Override
                public void onText(CharSequence charSequence) {

                }

                @Override
                public void swipeLeft() {

                }

                @Override
                public void swipeRight() {

                }

                @Override
                public void swipeDown() {

                }

                @Override
                public void swipeUp() {

                }
            };

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
