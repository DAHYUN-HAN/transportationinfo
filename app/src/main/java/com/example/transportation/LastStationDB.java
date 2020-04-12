package com.example.transportation;

import android.provider.BaseColumns;

public final class LastStationDB {

    public static final class CreateDB implements BaseColumns{
        public static final String SUBWAYSTATIONID = "subwaystationid";
        public static final String DAILYTYPE = "dailytype";
        public static final String UPDOWNTYPE = "updowntype";
        public static final String ARRTIME = "arrtime";
        public static final String DEPTIME = "deptime";
        public static final String _TABLENAME0 = "laststation";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                + _ID + " integer primary key autoincrement, "
                + SUBWAYSTATIONID + " text not null , "
                + DAILYTYPE + " text not null , "
                + UPDOWNTYPE + " integer not null , "
                + ARRTIME + " integer not null , "
                + DEPTIME + " text not null );";
    }
}
