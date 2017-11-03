package com.example.lukbanc.codegolf;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by lukbanc on 11/2/17.
 */

public class SelectPuzzle extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_select);

        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
    }

    public void addRadioButtons(int number) {

        for(int i=0; i < number; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i + 1000);
            radioButton.setText("RadioButton" + i);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                                  ActionBar.LayoutParams.WRAP_CONTENT, 1f);
            radioButton.setLayoutParams(params);
            radioGroup.addView(radioButton);
        }
    }
}
