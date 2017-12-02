package com.example.lukbanc.codegolf;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kyle on 11/28/2017.
 */

public class PuzzleAdapter extends ArrayAdapter<Puzzle> {
    ArrayList<Puzzle> puzzles = null;
    public PuzzleAdapter(Context context, int textViewResourceId, ArrayList<Puzzle> puzzles){
        super(context, textViewResourceId, puzzles);
        this.puzzles = puzzles;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int idx, View convView, ViewGroup parent){
        try {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.list_view_puzzle_row, null);
            TextView textViewName = (TextView) v.findViewById(R.id.list_view_puzzle_name);
            TextView textViewDesc = (TextView) v.findViewById(R.id.list_view_puzzle_desc);
            TextView textViewAdded = (TextView) v.findViewById(R.id.list_view_puzzle_added);

            textViewName.setText(puzzles.get(idx).getPuzzleTitle());
            textViewDesc.setText(puzzles.get(idx).getDescription());

            Date added = puzzles.get(idx).getCreatedDate();
            textViewAdded.setText(added.toString());
            return v;
        }
        catch(Exception e){
            String msg = e.toString();
            Log.e("getView", msg);
            return null;
        }
    }

}
