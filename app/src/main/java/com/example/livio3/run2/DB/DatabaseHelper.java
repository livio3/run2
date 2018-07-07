package com.example.livio3.run2.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by livio3 on 06/07/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NAMEDB = "user.db";
    private static final int DATABASE_VERSION = 3;
    private static final String CREATE_DB = "create table usersystem (_id integer primary key autoincrement, " +
            "name text not null, surname text not null, username text not null unique, password text not null," +
            "sex char , birth_date text );";
    public DatabaseHelper(Context context) {
        super(context, NAMEDB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usersystem");  //TODO vedi se si può trasferire gli elementi del db
        onCreate(db);
    }
}
