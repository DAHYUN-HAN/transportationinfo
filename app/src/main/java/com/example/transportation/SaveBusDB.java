package com.example.transportation;

import android.provider.BaseColumns;

public final class SaveBusDB {
    public static final class CreateDB implements BaseColumns{
        public static final String STATIONID = "stationid";
        public static final String BUSID = "busid";
        public static final String ORD = "ord";
        public static final String ARSID = "arsid";
        public static final String BUSNUMBER = "busnumber";
        public static final String STATIONNAME = "stationname";
        public static final String _TABLENAME0 = "savebus";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                + _ID + " integer primary key autoincrement, "
                + STATIONID + " text not null , "
                + BUSID + " text not null , "
                + ORD + " text not null  , "
                + ARSID + " text not null  , "
                + STATIONNAME + " text not null  , "
                + BUSNUMBER + " text not null );";
    }
}
