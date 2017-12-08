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

    private final static int ENTER = 5512; // newline
    private final static int INDENT = 5513; // 4-spaces
    private final static int BACKSPACE = 5514; // -- 4-spaces

    private final static int NAV_RIGHT = 5515;
    private final static int NAV_LEFT = 5516;
    private final static int SPACE = 5517;
    private final static int K_HASHTAG = 5518;
    private final static int K_QUESTION = 5519;
    private final static int K_PAREN_OPEN = 5520;
    private final static int K_PAREN_CLOSE = 5521;
    private final static int K_BRACKET_OPEN = 5522;
    private final static int K_BRACKET_CLOSE = 5523;
    private final static int K_COLON = 5524;
    private final static int K_PERIOD = 5525;
    private final static int K_QUOTE = 5526;
    private final static int K_APOSTROPHE = 5527;
    private final static int K_UNDERSCORE = 5528;
    private final static int K_EQUALS = 5529;
    private final static int K_LESS = 5530;
    private final static int K_GREATER = 5531;

    // Functionality of the keyboard
    private KeyboardView.OnKeyboardActionListener myKeyboardActionListener =
            new KeyboardView.OnKeyboardActionListener() {
                @Override
                public void onPress(int primaryCode) {

                }

                @Override
                public void onRelease(int i) {

                }

                public void insertSeq(Editable ed, int pos, String seq){
                    // adds the text in reverse to editable
                    for(int i=seq.length()-1;i>=0;i--){
                        ed.insert(pos, String.valueOf(seq.charAt(i)));
                    }
                }

                @Override
                public void onKey(int primaryCode, int[] keyCodes) {
                    try {
                        EditText focusCurrent = (EditText) hostActivity.findViewById(R.id.puzzle_soln);

                        if (focusCurrent == null || focusCurrent.getClass() == EditText.class)
                            return;

                        EditText editText = (EditText) focusCurrent;
                        Editable editable = editText.getText();
                        int start = editText.getSelectionStart();

                        switch (primaryCode) {
                            case DEF:
                                insertSeq(editable, start, "def ");
                                break;
                            case PRINT:
                                insertSeq(editable, start, "print(");
                                break;
                            case LAMBDA:
                                insertSeq(editable, start, "lambda ");
                                break;
                            case RETURN:
                                insertSeq(editable, start, "return ");
                                break;
                            case WHILE:
                                insertSeq(editable, start, "while ");
                                break;
                            case FOR:
                                insertSeq(editable, start, "for ");
                                break;
                            case IN:
                                insertSeq(editable, start, "in ");
                                break;
                            case IF:
                                insertSeq(editable, start, "if ");
                                break;

                            case ELSE:
                                insertSeq(editable, start, "else");
                                break;
                            case ELIF:
                                insertSeq(editable, start, "elif ");
                                break;
                            case TRUE:
                                insertSeq(editable, start, "True");
                                break;
                            case FALSE:
                                insertSeq(editable, start, "False");
                                break;
                            case ENTER:
                                insertSeq(editable, start, System.getProperty("line.separator"));
                            case INDENT:
                                // insert 4 spaces
                                insertSeq(editable, start, "    ");
                                break;
                            case SPACE:
                                insertSeq(editable, start, " ");
                                break;
                            case BACKSPACE:
                                // remove last 4 chars (or less if not 4 to delete)
                                int toRemove = 1;
                                if(start >= toRemove)
                                    editable.replace(start - toRemove, start, "");
                                break;
                            case NAV_LEFT:
                                // move cursor left
                                if (editText.getSelectionStart() > 0) ;
                                editText.setSelection(editText.getSelectionStart() - 1);
                                break;
                            case NAV_RIGHT:
                                // remove right
                                if (editText.getSelectionStart() < editText.length() - 3) ;
                                editText.setSelection(editText.getSelectionStart() + 1);
                                break;
                            case K_APOSTROPHE:
                                insertSeq(editable, start, "'");
                                break;
                            case K_BRACKET_CLOSE:
                                insertSeq(editable, start, "]");
                                break;
                            case K_BRACKET_OPEN:
                                insertSeq(editable, start, "[");
                                break;
                            case K_COLON:
                                insertSeq(editable, start, ":");
                                break;
                            case K_HASHTAG:
                                insertSeq(editable, start, "[");
                                break;
                            case K_PAREN_OPEN:
                                insertSeq(editable, start, "(");
                                break;
                            case K_PAREN_CLOSE:
                                insertSeq(editable, start, ")");
                                break;
                            case K_PERIOD:
                                insertSeq(editable, start, ".");
                                break;
                            case K_QUESTION:
                                insertSeq(editable, start, "?");
                                break;
                            case K_QUOTE:
                                insertSeq(editable, start, "\"");
                                break;
                            case K_UNDERSCORE:
                                insertSeq(editable, start, "_");
                                break;
                            case K_EQUALS:
                                insertSeq(editable, start, "=");
                                break;
                            case K_LESS:
                                insertSeq(editable, start, "<");
                                break;
                            case K_GREATER:
                                insertSeq(editable, start, ">");
                                break;

                        }
                    }
                    catch(Exception e){
                        Log.e("onKey",e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onText(CharSequence charSequence) {

                    Log.d("onText","charSequence: "+charSequence);
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
            Log.d("openKeyboard","toggle");
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
        /*editText.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                try {
                    EditText edittext = (EditText) v;
                    int inType = edittext.getInputType();       // Backup the input type

                    Log.d("setting null input","eek?");
                    edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                    edittext.onTouchEvent(event);               // Call native handler
                    edittext.setInputType(inType);              // Restore input type
                    return true; // Consume touch event
                }
                catch(Exception e){
                    Log.e("onTouchListener", e.toString());
                    e.printStackTrace();
                }
                return true;
            }
        });
            */
        // Disable spell check
        editText.setInputType(editText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    // helpers to hide/show/check custom keyboard
    public void hideCustomKeyboard() {
        kbView.setVisibility(View.GONE);
        kbView.setEnabled(false);
    }

    public void showCustomKeyboard( View v ) {
        try {
            kbView.setVisibility(View.VISIBLE);
            kbView.setEnabled(true);
            if (v != null) {

                Log.d("forcing the input shut","eek2?");
                ((InputMethodManager) hostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        catch (Exception e){
            Log.e("showCustomKeyboard", e.toString());
            e.printStackTrace();
        }
    }

    public boolean isCustomKeyboardVisible() {
        return kbView.getVisibility() == View.VISIBLE;
    }


}
