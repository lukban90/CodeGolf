package com.example.lukbanc.codegolf;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by lukbanc on 12/3/17.
 */

public class CustomKeyboard {

    private Keyboard kb;
    private KeyboardView kbView;
    private Activity hostActivity;

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

    // onClick to open keyboard
    public void openKeyboard(View v) {
        kbView.setVisibility(View.VISIBLE);
        kbView.setEnabled(true);
        if(v != null) {
            ((InputMethodManager) hostActivity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow( v.getWindowToken(), 0 );
        }
    }

    public CustomKeyboard( Activity host, int viewId, int layoutId ) {
        hostActivity = host;
        kb = new Keyboard( host, layoutId );
        kbView = (KeyboardView) host.findViewById( viewId );
        kbView.setKeyboard( kb );

        kbView.setOnKeyboardActionListener( myKeyboardActionListener );
    }

    public void registerEditText(int resId) {
        EditText editText = (EditText) hostActivity.findViewById(resId);

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
        kbView.setVisibility(View.GONE);
        kbView.setEnabled(false);
    }

    public void showCustomKeyboard( View v ) {
        kbView.setVisibility(View.VISIBLE);
        kbView.setEnabled(true);
        if( v!=null ) ((InputMethodManager)hostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public boolean isCustomKeyboardVisible() {
        return kbView.getVisibility() == View.VISIBLE;
    }


}
