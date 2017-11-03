package com.example.lukbanc.codegolf;

import java.util.Date;

/**
 * Created by Kyle on 11/2/2017.
 */

public class Solution {
    private int solutionId = -1;
    private String solutionText = "";
    private int solutionCharCt = 0;
    private Date solveDate = null;

    public Solution(){

    }
    public Solution(int solutionId, String solutionText, int charCount, Date solveDate){
        this.solutionId = solutionId;
        this.solutionText = solutionText;
        this.solutionCharCt = charCount;
        this.solveDate = solveDate;
    }
    public int getSolutionId(){return solutionId;}
    public String getSolutionText(){return solutionText;}
    public int getSolutionCharCt(){return solutionCharCt;}
    public Date getSolveDate(){return solveDate;}

    public void setSolutionId(int id){solutionId = id;}
}
