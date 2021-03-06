package com.example.transportation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LastStationDBOpenHelper {
    private static final String DATABASE_NAME = "StationDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(LastStationDB.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+LastStationDB.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public LastStationDBOpenHelper(Context context){
        this.mCtx = context;
    }

    public LastStationDBOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn(String subwaystationid, String dailytype, String updowntype, String arrtime, String deptime){
        ContentValues values = new ContentValues();
        values.put(LastStationDB.CreateDB.SUBWAYSTATIONID, subwaystationid);
        values.put(LastStationDB.CreateDB.DAILYTYPE, dailytype);
        values.put(LastStationDB.CreateDB.UPDOWNTYPE, updowntype);
        values.put(LastStationDB.CreateDB.ARRTIME, arrtime);
        values.put(LastStationDB.CreateDB.DEPTIME, deptime);
        System.out.println("Value는"+values);
        return mDB.insert(LastStationDB.CreateDB._TABLENAME0, null, values);
    }

    // Update DB
    public boolean updateColumn(long id, String subwaystationid, String dailytype, String updowntype, String arrtime, String deptime){
        ContentValues values = new ContentValues();
        values.put(LastStationDB.CreateDB.SUBWAYSTATIONID, subwaystationid);
        values.put(LastStationDB.CreateDB.DAILYTYPE, dailytype);
        values.put(LastStationDB.CreateDB.UPDOWNTYPE, updowntype);
        values.put(LastStationDB.CreateDB.ARRTIME, arrtime);
        values.put(LastStationDB.CreateDB.DEPTIME, deptime);
        return mDB.update(LastStationDB.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(LastStationDB.CreateDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(LastStationDB.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(LastStationDB.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor aaexist(){
        Cursor c = mDB.rawQuery( "SELECT subwaystationid, dailytype, updowntype FROM laststation ORDER BY subwaystationid;", null);
        return c;
    }

    public Cursor exist(String id, String daily, String updown){
        Cursor c = mDB.rawQuery("SELECT arrtime, deptime FROM laststation WHERE subwaystationid = '" + id +"' AND dailytype = '" + daily + "' AND updowntype = '" + updown + "';", null);
        return c;
    }
}
