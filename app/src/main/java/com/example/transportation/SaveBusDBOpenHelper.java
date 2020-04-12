package com.example.transportation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SaveBusDBOpenHelper {
    private static final String DATABASE_NAME = "SaveBusDatabase(SQLite).db";
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
            db.execSQL(SaveBusDB.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+SaveBusDB.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public SaveBusDBOpenHelper(Context context){
        this.mCtx = context;
    }

    public SaveBusDBOpenHelper open() throws SQLException{
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
    public long insertColumn(String stationid, String busid, String ord, String arsid, String busnumber){
        ContentValues values = new ContentValues();
        values.put(SaveBusDB.CreateDB.STATIONID, stationid);
        values.put(SaveBusDB.CreateDB.BUSID, busid);
        values.put(SaveBusDB.CreateDB.ORD, ord);
        values.put(SaveBusDB.CreateDB.ARSID, arsid);
        values.put(SaveBusDB.CreateDB.BUSNUMBER, busnumber);
        System.out.println("Value는"+values);
        return mDB.insert(SaveBusDB.CreateDB._TABLENAME0, null, values);
    }

    // Update DB
    public boolean updateColumn(long id, String stationid, String busid, String ord, String arsid, String busnumber){
        ContentValues values = new ContentValues();
        values.put(SaveBusDB.CreateDB.STATIONID, stationid);
        values.put(SaveBusDB.CreateDB.BUSID, busid);
        values.put(SaveBusDB.CreateDB.ORD, ord);
        values.put(SaveBusDB.CreateDB.ARSID, arsid);
        values.put(SaveBusDB.CreateDB.BUSNUMBER, busnumber);
        return mDB.update(SaveBusDB.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(SaveBusDB.CreateDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(SaveBusDB.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(SaveBusDB.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(){
        Cursor c = mDB.rawQuery( "SELECT * FROM savebus ORDER BY stationid;", null);
        return c;
    }
}
