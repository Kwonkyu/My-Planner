package com.example.teamproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "calendar_02.db";
    private static final int DBVer = 1;

    public DBHelper(Context context) {
        super(context, DBName, null, DBVer);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE calendar ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, work TEXT, startday INTEGER, starttime INTEGER, endday INTEGER, endtime INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS calendar");
        onCreate(db);
    }
}
