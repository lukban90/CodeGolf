package com.example.lukbanc.codegolf;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.GetChars;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Kyle on 10/27/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "code_golf_db";
    private static final int DB_VERSION = 1;

    // entity tables
    public static final String TBL_PUZZLE = "puzzle";
    public static final String TBL_SOLUTION = "solution";
    public static final String TBL_USER = "user";
    public static final String TBL_PROG_LANGUAGE = "prog_language";

    // these additional tables represent entity relationships
    // and consist of only foreign keys
    // puzzle -- score -- user
    public static final String TBL_PUZZLE_SOLUTION = "puzzle_solution";
    public static final String TBL_USER_SOLUTION = "user_score";
    public static final String TBL_PUZZLE_PROG_LANG = "puzzle_prog_lang";
    // programming language / puzzle

    // columns for TBL_PUZZLE
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_EMAIL = "email";

    // columns for TBL_PUZZLE
    public static final String COL_PUZZLE_ID = "puzzle_id";
    public static final String COL_PUZZLE_TITLE = "puzzle_title";
    public static final String COL_PUZZLE_DESC = "puzzle_desc";
    public static final String COL_PUZZLE_ADDED = "date_added";

    // TBL_SOLUTION
    public static final String COL_SOLUTION_ID = "solution_id";
    public static final String COL_SOLUTION_TEXT = "solution_text";
    public static final String COL_SOLUTION_CHAR_COUNT = "char_count";
    public static final String COL_SOLVE_DATE = "solve_date";

    // TBL_PROG_LANGUAGE
    public static final String COL_PROG_LANG_ID = "prog_lang_id";
    public static final String COL_PROG_LANG_NAME = "prog_lang_name";
    public static final String COL_PROG_LANG_EXEC_PATH = "prog_lang_exec_path";

    public static final String SERVER_PYTHON_URL = "http://67.171.28.34/py";


    public DatabaseManager( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );}

    private String getSqlCreateTablePuzzle(){
        String sql = "CREATE TABLE "+TBL_PUZZLE+" ( ";
        sql += COL_PUZZLE_ID + " INTEGER PRIMARY KEY, ";
        sql += COL_PUZZLE_TITLE + " TEXT NOT NULL, ";
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


    @Override
    public void onCreate(SQLiteDatabase db){
        String sqlCreateTableUser = getSqlCreateTablePuzzle();

        String sqlCreateTableUserSol = getSqlCreateTableUserSolution();
        try{
            db.execSQL(sqlCreateTableUser);
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

    // OBJECT INSERTION AND SELECTION

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
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlInsert = "insert into " + TBL_USER +
                " (" + COL_USER_NAME + "," + COL_EMAIL + ")";
        sqlInsert += " values ('" + user.getUserName() + "', '" + user.getEmail() + "')";
        db.execSQL(sqlInsert);
        // now get the insert id
        String sqlGetId = "select last_insert_rowid()";
        Cursor cursor = db.rawQuery(sqlGetId, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        db.close();
        user.setUserId(id);
        return id;
    }

    public Solution selectSolutionById( int solutionId ){
        String sqlQuery= "select * from " + TBL_SOLUTION;
        sqlQuery += String.format(" where %s = %d", COL_SOLUTION_ID, solutionId);
        SQLiteDatabase db= this.getWritableDatabase( );
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String text = cursor.getString(1);
            int charCt = cursor.getInt(2);
            String solveDateStr = cursor.getString(3);
            Date solveDate = solveDateStr == null ? null : new Date(solveDateStr);
            return new Solution(id, text, charCt, solveDate);
        }
        return null;
    }

    public int insertSolution(Solution solution) {
        Date solveDate = solution.getSolveDate();
        if(solveDate == null)
            solveDate = new Date();
        // convert Date to string for database storage
        String solveDateStr = solveDate.toString();

        SQLiteDatabase db= this.getWritableDatabase( );
        String sqlInsert= "insert into "+TBL_SOLUTION +
                " ("+COL_SOLUTION_TEXT+","+COL_SOLUTION_CHAR_COUNT+","+COL_SOLVE_DATE+")";
        sqlInsert+= " values ('" + solution.getSolutionText() +  "', "+
                solution.getSolutionCharCt() +", '"+solveDateStr+"')";
        db.execSQL(sqlInsert);
        // now get the insert id
        String sqlGetId = "select last_insert_rowid()";
        Cursor cursor = db.rawQuery(sqlGetId, null);
        int id = -1;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);

        }
        db.close( );
        solution.setSolutionId(id);
        return id;
    }

    public String getSelectPuzzleByIdUrl(int puzzleId){
        return String.format(Locale.ENGLISH,
                "%s/%s?%s=%d",SERVER_PYTHON_URL,"get_puzzle_by_id.py", COL_PUZZLE_ID, puzzleId);
    }

    public static void fetchJson(String url, JsonAsyncTask.OnTaskCompleted onTaskCompleted){
        JsonAsyncTask task = new JsonAsyncTask();
        task.setTaskCompletedListener(onTaskCompleted);

        task.execute(url);
    }

    public void testREST(){

        //selectPuzzleByIdTest(1);

        fetchJson(getSelectPuzzleByIdUrl(1), new JsonAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject result) {
                if(result != null){
                    Log.d("YAY","do something with the result.");
                    Puzzle p = new Puzzle();
                    try {
                        p.setPuzzleId(result.getInt(COL_PUZZLE_ID));
                        p.setPuzzleTitle(result.getString(COL_PUZZLE_TITLE));
                        p.setDescription(result.getString(COL_PUZZLE_DESC));
                    }
                    catch (Exception e) {
                    }
                    String pInfo = String.format(Locale.ENGLISH,
                            "(%d, %s, %s)", p.getPuzzleId(), p.getPuzzleTitle(), p.getDescription());
                    Log.d("PUZZLE INFO", pInfo);
                }
            }
            @Override
            public void onTaskCompleted(JSONArray result) {
                if(result != null){
                    return;
                }
            }
        });
    }

    public int insertPuzzle(Puzzle puzzle) {
        Date added = puzzle.getCreatedDate();
        if(added == null)
            added = new Date();
        // convert Date to string for database storage
        String addedStr = added.toString();

        SQLiteDatabase db= this.getWritableDatabase( );
        String sqlInsert= "insert into "+TBL_PUZZLE +
                " ("+COL_PUZZLE_TITLE+","+COL_PUZZLE_DESC+","+COL_PUZZLE_ADDED+")";
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

    public ProgrammingLanguage selectProgLangById( int progLangId ){
        String sqlQuery= "select * from " + TBL_PROG_LANGUAGE;
        sqlQuery += String.format(" where %s = %d", COL_PROG_LANG_ID, progLangId);
        SQLiteDatabase db= this.getWritableDatabase( );
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String execPath = cursor.getString(2);
            String addedStr = cursor.getString(3);
            Date added = addedStr == null ? null : new Date(addedStr);
            return new ProgrammingLanguage(id, name, execPath);
        }
        return null;
    }

    public int insertProgLang(ProgrammingLanguage progLang) {
        SQLiteDatabase db= this.getWritableDatabase( );
        String sqlInsert= "insert into "+TBL_PUZZLE +
                " ("+COL_PROG_LANG_NAME+","+COL_PROG_LANG_EXEC_PATH+")";
        sqlInsert+= " values ('" + progLang.getProgLangName() +  "', '"+
                progLang.getProgLangExecPath()+"')";
        db.execSQL(sqlInsert);
        // now get the insert id
        String sqlGetId = "select last_insert_rowid()";
        Cursor cursor = db.rawQuery(sqlGetId, null);
        int id = -1;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);

        }
        db.close( );
        progLang.setProgLangId(id);
        return id;
    }

}
