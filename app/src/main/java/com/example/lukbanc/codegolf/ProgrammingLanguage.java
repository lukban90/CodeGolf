package com.example.lukbanc.codegolf;

/**
 * Created by Kyle on 11/2/2017.
 */

public class ProgrammingLanguage {
    private int progLangId = -1;
    private String langName = null;
    private String execPath = null;
    public ProgrammingLanguage(){

    }
    public ProgrammingLanguage(int progLangId, String langName, String execPath){
        this.progLangId =progLangId;
        this.langName = langName;
        this.execPath = execPath;
    }
}
