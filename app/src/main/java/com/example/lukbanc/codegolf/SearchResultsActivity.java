package com.example.lukbanc.codegolf;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by lukbanc on 11/7/17.
 */

public class SearchResultsActivity extends AppCompatActivity {
    private Keyboard keyboard;
    private KeyboardView keyboardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add layout here
        setContentView(R.layout.activity_solution_edit);
        handleIntent(getIntent());

        // Create the keyboard
        keyboard = new Keyboard(this, R.xml.keyboard);
        keyboardView = (KeyboardView)findViewById(R.id.keyboardview);
        keyboardView.setKeyboard( keyboard );

        //registerEditText(R.id.puzzle_soln);

        //keyboardView.setOnKeyboardActionListener(myKeyboardActionListener);
    }

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
