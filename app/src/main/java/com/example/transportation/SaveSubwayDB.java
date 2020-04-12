package com.example.transportation;
import android.provider.BaseColumns;

public class SaveSubwayDB {
    public static final class CreateDB implements BaseColumns{
        public static final String SUBWAYLINE = "subwayline";
        public static final String SUBWAYDIRECTION = "subwaydirection";
        public static final String SUBWAYSTATION = "subwaystation";
        public static final String _TABLENAME0 = "savesubway";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                + _ID + " integer primary key autoincrement, "
                + SUBWAYLINE + " text not null , "
                + SUBWAYDIRECTION + " text not null , "
                + SUBWAYSTATION + " text not null );";
    }
}
