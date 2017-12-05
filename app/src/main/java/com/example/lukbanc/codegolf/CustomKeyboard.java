package com.example.lukbanc.codegolf;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lukbanc on 12/3/17.
 */

public class CustomKeyboard {

    private Keyboard kb;
    private KeyboardView kbView;
    private Activity hostActivity;

    private final static int DEF = 5500;
    private final static int PRINT = 5501;
    private final static int LAMBDA = 5502;
    private final static int RETURN = 5503;

    private final static int WHILE = 5504;
    private final static int FOR = 5505;
    private final static int IN = 5506;
    private final static int IF = 5507;

    private final static int ELSE = 5508;
    private final static int ELIF = 5509;
    private final static int TRUE = 5510;
    private final static int FALSE = 5511;

    // Functionality of the keyboard
    private KeyboardView.OnKeyboardActionListener myKeyboardActionListener =
            new KeyboardView.OnKeyboardActionListener() {
                @Override
                public void onPress(int primaryCode) {

                }

                @Override
                public void onRelease(int i) {

                }

                @Override
                public void onKey(int primaryCode, int[] keyCodes) {
                    EditText focusCurrent = (EditText)hostActivity.findViewById( R.id.puzzle_soln );

                    if( focusCurrent == null || focusCurrent.getClass() == EditText.class ) return;

                    EditText editText = (EditText)focusCurrent;
                    Editable editable = editText.getText();
                    int start = editText.getSelectionStart();

                    if( primaryCode == DEF ) {
                        editable.insert( start, " " );
                        editable.insert( start, "f" );
                        editable.insert( start, "e" );
                        editable.insert( start, "d" );

                    } else if( primaryCode == PRINT ) {
                        editable.insert( start, "t" );
                        editable.insert( start, "n" );
                        editable.insert( start, "i" );
                        editable.insert( start, "r" );
                        editable.insert( start, "p" );

                    } else if( primaryCode == LAMBDA ) {
                        editable.insert( start, " " );
                        editable.insert( start, "a" );
                        editable.insert( start, "d" );
                        editable.insert( start, "b" );
                        editable.insert( start, "m" );
                        editable.insert( start, "a" );
                        editable.insert( start, "l" );

                    } else if( primaryCode == RETURN ) {
                        editable.insert( start, " " );
                        editable.insert( start, "n" );
                        editable.insert( start, "r" );
                        editable.insert( start, "u" );
                        editable.insert( start, "t" );
                        editable.insert( start, "e" );
                        editable.insert( start, "r" );

                    } else if( primaryCode == WHILE ) {
                        editable.insert( start, " " );
                        editable.insert( start, "e" );
                        editable.insert( start, "l" );
                        editable.insert( start, "i" );
                        editable.insert( start, "h" );
                        editable.insert( start, "w" );

                    } else if( primaryCode == FOR ) {
                        editable.insert( start, " " );
                        editable.insert( start, "r" );
                        editable.insert( start, "o" );
                        editable.insert( start, "f" );

                    } else if( primaryCode == IN ) {
                        editable.insert( start, " " );
                        editable.insert( start, "n" );
                        editable.insert( start, "i" );

                    } else if( primaryCode == IF ) {
                        editable.insert( start, " " );
                        editable.insert( start, "f" );
                        editable.insert( start, "i" );

                    } else if( primaryCode == ELSE ) {
                        editable.insert(start, " ");
                        editable.insert(start, "e");
                        editable.insert(start, "s");
                        editable.insert(start, "l");
                        editable.insert(start, "e");

                    } else if( primaryCode == ELIF ) {
                        editable.insert( start, " " );
                        editable.insert( start, "f" );
                        editable.insert( start, "i" );
                        editable.insert( start, "l" );
                        editable.insert( start, "e" );

                    } else if( primaryCode == TRUE ) {
                        editable.insert( start, " " );
                        editable.insert( start, "e" );
                        editable.insert( start, "u" );
                        editable.insert( start, "r" );
                        editable.insert( start, "t" );

                    } else if( primaryCode == FALSE ) {
                        editable.insert( start, " " );
                        editable.insert( start, "e" );
                        editable.insert( start, "s" );
                        editable.insert( start, "l" );
                        editable.insert( start, "a" );
                        editable.insert( start, "f" );
                    }
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

    // onClick from SolutionEditActivity to open keyboard
    public void openKeyboard(View v) {

        if( v!= null ) {
            if( isCustomKeyboardVisible() )
                kbView.setVisibility(View.INVISIBLE);
            else
                kbView.setVisibility(View.VISIBLE);
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

    // helpers to hide/show/check custom keyboard
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
