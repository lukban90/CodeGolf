package com.example.lukbanc.codegolf;

import java.util.Date;

/**
 * Created by Kyle on 11/2/2017.
 */

public class Puzzle {
    private int puzzleId;
    private String puzzleTitle;
    private String description;
    private ProgrammingLanguage programmingLanguage;
    private Date createdDate;
    public Puzzle(){

    }
    public Puzzle(int puzzleId, String puzzleTitle, ProgrammingLanguage progLanguage, Date createdDate){
        this.puzzleId = puzzleId;
        this.puzzleTitle = puzzleTitle;
        this.programmingLanguage = progLanguage;
        this.createdDate = createdDate;
    }

    public int getPuzzleId(){return puzzleId;}
    public void setPuzzleId(int id){puzzleId = id;}
    public String getDescription(){return description;}
    public Date getCreatedDate(){return createdDate;}
    public String getPuzzleTitle(){return puzzleTitle;}
}
