package com.example.lukbanc.codegolf;

/**
 * Created by Kyle on 11/2/2017.
 */

public class User {
    private int userId;
    private String userName;
    private String email;
    public User(){

    }
    public User(int id, String userName, String email){
        this.userId = id;
        this.userName = userName;
    }
    public int getUserId(){return userId;}
    public void setUserId(int id){userId = id;}
    public String getUserName(){return userName;}

    public String getEmail(){return email;}
}