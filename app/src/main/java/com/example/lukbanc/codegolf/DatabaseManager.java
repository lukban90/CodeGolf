package com.example.lukbanc.codegolf;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.GetChars;
import android.util.Log;

import java.util.Date;

/**
 * Created by Kyle on 10/27/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "code_golf_db";
    private static final int DB_VERSION = 1;

    private static final String TBL_PUZZLE = "puzzle";
    private static final String TBL_SCORE = "score";
    private static final String TBL_USER = "user";
    private static final String TBL_PROG_LANGUAGE = "prog_language";

    // these additional tables represent entity relationships
    // and consist of only foreign keys
    // puzzle -- score -- user
    private static final String TBL_PUZZLE_SCORE = "puzzle_score";
    private static final String TBL_USER_SCORE = "user_score";
    // programming language / puzzle
    private static final String TBL_PUZZLE_PROG_LANGUAGE = "puzzle_prog_language";
    private static final String TBL_TASK_ASSIGNMENT = "task_assignment";

    // columns for TBL_PUZZLE
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USER_NAME = "user_name";
    private static final String COL_EMAIL = "email";

    // columns for TBL_PUZZLE
    private static final String COL_PUZZLE_ID = "puzzle_id";
    private static final String COL_PUZZLE_NAME = "puzzle_name";
    private static final String COL_PUZZLE_DESC = "puzzle_desc";
    private static final String COL_PUZZLE_ADDED = "date_added";

    public DatabaseManager( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );}

    private String getSqlCreateTablePuzzle(){
        String sql = "CREATE TABLE "+TBL_PUZZLE+" ( ";
        sql += COL_PUZZLE_ID + " INTEGER PRIMARY KEY, ";
        sql += COL_PUZZLE_NAME + " TEXT NOT NULL, ";
        sql += COL_PUZZLE_DESC + " TEXT NOT NULL, ";
        sql += COL_PUZZLE_ADDED + " TEXT NULL";
        sql += ");";
        return sql;
    }
    private String getSqlCreateTableUser(){
        String sql = "CREATE TABLE " + TBL_USER + " ( ";
        sql += COL_USER_ID + " INTEGER PRIMARY KEY, ";
        sql += COL_USER_NAME + " TEXT NOT NULL, ";
        sql += COL_EMAIL + " TEXT NULL";
        sql += ");";
        return sql;
    }

    private String getSqlCreateTableScore(){
        return null;
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        // create all the tables
        String sqlCreateTablePuzzle = getSqlCreateTablePuzzle();
        String sqlCreateTableUser = getSqlCreateTablePuzzle();
        String sqlCreateTableScore = getSqlCreateTablePuzzle();
        try{
            db.execSQL(sqlCreateTablePuzzle);
            db.execSQL(sqlCreateTableUser);
            db.execSQL(sqlCreateTableScore);
        }
        catch (Exception e){
            Log.e("table create", e.toString());
        }
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ){
        // drop relationship tables first to respect foreign key restraints
        db.execSQL( "drop table if exists " + TBL_PUZZLE_PROG_LANGUAGE );
        db.execSQL( "drop table if exists " + TBL_PUZZLE_SCORE );
        db.execSQL( "drop table if exists " + TBL_USER_SCORE );
        // then drop main entity tables
        db.execSQL( "drop table if exists " + TBL_PUZZLE );
        db.execSQL( "drop table if exists " + TBL_SCORE );
        db.execSQL( "drop table if exists " + TBL_USER );
        // recreate all tables
        onCreate(db);
    }

    public User selectUserById( int userId ){
        String sqlQuery= "select * from " + TBL_USER;
        sqlQuery += String.format(" where %s = %d", COL_USER_ID, userId);
        SQLiteDatabase db= this.getWritableDatabase( );
        Cursor cursor = db.rawQuery(sqlQuery, null);
        int id;
        String name;
        String email;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
            name = cursor.getString(1);
            email = cursor.getString(2);
            return new User(id, name, email);
        }
        return null;
    }

    public int insertUser(User user) {
        SQLiteDatabase db= this.getWritableDatabase( );
        String sqlInsert= "insert into "+TBL_USER +
                " ("+COL_USER_NAME+","+COL_EMAIL+")";
        sqlInsert+= " values ('" + user.getUserName() +  "', '"+ user.getEmail()+"')";
        db.execSQL(sqlInsert);
        // now get the insert id
        String sqlGetId = "select last_insert_rowid()";
        Cursor cursor = db.rawQuery(sqlGetId, null);
        int id = -1;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        db.close( );
        user.setUserId(id);
        return id;
    }

    public int insertPuzzle(Puzzle puzzle) {
        Date added = puzzle.getCreatedDate();
        if(added == null)
            added = new Date();
        // convert Date to string for database storage
        String addedStr = added.toString();

        SQLiteDatabase db= this.getWritableDatabase( );
        String sqlInsert= "insert into "+TBL_PUZZLE +
                " ("+COL_PUZZLE_NAME+","+COL_PUZZLE_DESC+","+COL_PUZZLE_ADDED+")";
        sqlInsert+= " values ('" + puzzle.getPuzzleTitle() +  "', '"+
                puzzle.getDescription() +"', '"+addedStr+"')";
        db.execSQL(sqlInsert);
        // now get the insert id
        String sqlGetId = "select last_insert_rowid()";
        Cursor cursor = db.rawQuery(sqlGetId, null);
        int id = -1;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);

        }
        db.close( );
        puzzle.setPuzzleId(id);
        return id;
    }
}
