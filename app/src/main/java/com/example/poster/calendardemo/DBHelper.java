package com.example.poster.calendardemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by POSTER on 05.04.2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context){
        super(context, "calendarDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table events (" +
                "id integer primary key autoincrement," +
                "description text," +
                "is_notify integer," +
                "year integer," +
                "month integer," +
                "day_of_month integer," +
                "event_time integer" + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
