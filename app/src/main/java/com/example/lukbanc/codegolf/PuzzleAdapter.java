package com.example.lukbanc.codegolf;

import android.widget.ArrayAdapter;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Kyle on 11/28/2017.
 */

public class PuzzleAdapter extends ArrayAdapter<Puzzle> {
    ArrayList<Puzzle> puzzles = null;
    public PuzzleAdapter(Context context, int textViewResourceId, ArrayList<Puzzle> puzzles){
        super(context, textViewResourceId, puzzles);
        this.puzzles = puzzles;

    }

}
