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

    // entity tables
    private static final String TBL_PUZZLE = "puzzle";
    private static final String TBL_SOLUTION = "solution";
    private static final String TBL_USER = "user";
    private static final String TBL_PROG_LANGUAGE = "prog_language";

    // these additional tables represent entity relationships
    // and consist of only foreign keys
    // puzzle -- score -- user
    private static final String TBL_PUZZLE_SOLUTION = "puzzle_solution";
    private static final String TBL_USER_SOLUTION = "user_score";
    private static final String TBL_PUZZLE_PROG_LANG = "puzzle_prog_lang";
    // programming language / puzzle

    // columns for TBL_PUZZLE
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USER_NAME = "user_name";
    private static final String COL_EMAIL = "email";

    // columns for TBL_PUZZLE
    private static final String COL_PUZZLE_ID = "puzzle_id";
    private static final String COL_PUZZLE_NAME = "puzzle_name";
    private static final String COL_PUZZLE_DESC = "puzzle_desc";
    private static final String COL_PUZZLE_ADDED = "date_added";

    // TBL_SOLUTION
    private static final String COL_SOLUTION_ID = "solution_id";
    private static final String COL_SOLUTION_TEXT = "solution_text";
    private static final String COL_SOLUTION_CHAR_COUNT = "char_count";
    private static final String COL_SOLVE_DATE = "solve_date";

    // TBL_PROG_LANGUAGE
    private static final String COL_PROG_LANG_ID = "prog_lang_id";
    private static final String COL_PROG_LANG_NAME = "prog_lang_name";
    private static final String COL_PROG_LANG_EXEC_PATH = "prog_lang_exec_path";

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

    private String getSqlCreateTableSolution(){
        String sql = "CREATE TABLE " + TBL_SOLUTION + " ( ";
        sql += COL_SOLUTION_ID + " INTEGER PRIMARY KEY, ";
        sql += COL_SOLUTION_TEXT + " TEXT NOT NULL";
        sql += COL_SOLUTION_CHAR_COUNT + " INTEGER NOT NULL, ";
        sql += COL_SOLVE_DATE + " TEXT NOT NULL);";
        return sql;
    }

    private String getSqlCreateTableProgLanguage(){
        String sql = "CREATE TABLE " + TBL_PROG_LANGUAGE + " ( ";
        sql += COL_PROG_LANG_ID + " INTEGER PRIMARY KEY, ";
        sql += COL_PROG_LANG_NAME + " TEXT NOT NULL";
        sql += COL_PROG_LANG_EXEC_PATH + " TEXT NULL)";
        return sql;
    }

    // relationship tables:
    private String getSqlCreateTableUserSolution(){
        String sql = "CREATE TABLE " + TBL_USER_SOLUTION + " ( ";
        sql += String.format("%s INTEGER REFERENCES %s(%s), ",
            COL_USER_ID, TBL_USER, COL_USER_ID);
        sql += String.format("%s INTEGER REFERENCES %s(%s), ",
            COL_SOLUTION_ID, TBL_SOLUTION, COL_SOLUTION_ID);
        sql += String.format("PRIMARY KEY (%s,%s)", COL_USER_ID, COL_SOLUTION_ID);
        sql += ");";
        return sql;
    }

    private String getSqlCreateTablePuzzleSolution(){
        String sql = "CREATE TABLE " + TBL_PUZZLE_SOLUTION + " ( ";
        sql += String.format("%s INTEGER REFERENCES %s(%s), ",
                COL_PUZZLE_ID, TBL_PUZZLE, COL_PUZZLE_ID);
        sql += String.format("%s INTEGER REFERENCES %s(%s), ",
                COL_SOLUTION_ID, TBL_SOLUTION, COL_SOLUTION_ID);
        sql += String.format("PRIMARY KEY (%s,%s)", COL_PUZZLE_ID, COL_SOLUTION_ID);
        sql += ");";
        return sql;
    }

    private String getSqlCreateTablePuzzleProgLang(){
        String sql = "CREATE TABLE " + TBL_PUZZLE_PROG_LANG + " ( ";
        sql += String.format("%s INTEGER REFERENCES %s(%s), ",
                COL_PUZZLE_ID, TBL_PUZZLE, COL_PUZZLE_ID);
        sql += String.format("%s INTEGER REFERENCES %s(%s), ",
                COL_PROG_LANG_ID, TBL_PROG_LANGUAGE, COL_PROG_LANG_ID);
        sql += String.format("PRIMARY KEY (%s,%s)", COL_PUZZLE_ID, COL_PROG_LANG_ID);
        sql += ");";
        return sql;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // create all the tables
        String sqlCreateTablePuzzle = getSqlCreateTablePuzzle();
        String sqlCreateTableUser = getSqlCreateTablePuzzle();
        String sqlCreateTableScore = getSqlCreateTablePuzzle();
        String sqlCreateTableProgLang = getSqlCreateTableProgLanguage();

        String sqlCreateTablePuzzleSol = getSqlCreateTablePuzzleSolution();
        String sqlCreateTablePuzzleProgLang = getSqlCreateTablePuzzleProgLang();
        String sqlCreateTableUserSol = getSqlCreateTableUserSolution();
        try{
            db.execSQL(sqlCreateTablePuzzle);
            db.execSQL(sqlCreateTableUser);
            db.execSQL(sqlCreateTableScore);
            db.execSQL(sqlCreateTableProgLang);

            db.execSQL(sqlCreateTablePuzzleSol);
            db.execSQL(sqlCreateTablePuzzleProgLang);
            db.execSQL(sqlCreateTableUserSol);
        }
        catch (Exception e){
            Log.e("table create", e.toString());
        }
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ){
        // drop relationship tables first to respect foreign key restraints
        db.execSQL( "drop table if exists " + TBL_PUZZLE_PROG_LANG );
        db.execSQL( "drop table if exists " + TBL_PUZZLE_SOLUTION );
        db.execSQL( "drop table if exists " + TBL_USER_SOLUTION );
        // then drop main entity tables
        db.execSQL( "drop table if exists " + TBL_PUZZLE );
        db.execSQL( "drop table if exists " + TBL_SOLUTION );
        db.execSQL( "drop table if exists " + TBL_USER );
        db.execSQL( "drop table if exists " + TBL_PROG_LANGUAGE );
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
