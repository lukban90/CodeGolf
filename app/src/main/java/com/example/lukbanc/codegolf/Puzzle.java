package com.example.lukbanc.codegolf;

import java.util.Date;

/**
 * Created by Kyle on 11/2/2017.
 */

public class Puzzle {
    private int puzzleId;
    private String puzzleTitle;
    private String description;
    // design question: should all puzzles require the user to define one method?
    // that is how they do it at code fights .com or whatever that site is called.
    private String methodName;
    private ProgrammingLanguage programmingLanguage;
    private Date createdDate;

    public Puzzle(){

    }
    public Puzzle(int puzzleId, String puzzleTitle, String desc, Date createdDate){
        this.puzzleId = puzzleId;
        this.description = desc;
        this.puzzleTitle = puzzleTitle;
        this.createdDate = createdDate;
    }

    public int getPuzzleId(){return puzzleId;}
    public void setPuzzleId(int id){puzzleId = id;}
    public String getDescription(){return description;}
    public void setDescription(String desc){description=desc;}
    public Date getCreatedDate(){return createdDate;}
    public String getPuzzleTitle(){return puzzleTitle;}
    public void setPuzzleTitle(String title){puzzleTitle = title;}
}
