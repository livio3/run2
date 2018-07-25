package com.example.livio3.run2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by livio3 on 06/07/18.
 */

public class DbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    //DB Fields
    private static final String DB_TABLE = "usersystem";
    private static final String ID_RUNNER = "_id";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String SEX = "sex";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String BIRTH_DATE = "birth_date";

    public  DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(String name, String surname, String username, String password ) {
        ContentValues values = new ContentValues();
        values.put(NAME,name);
        values.put(SURNAME, surname);
        values.put(USERNAME, username);
        values.put(PASSWORD, password);

        //values.put(BIRTH_DATE, Strinrunner.getBirth_date());
        return values;
    }

    public long createRunner(String name, String surname, String username, String password) {
        ContentValues values = createContentValues(name,  surname,  username,  password);
        //database.insertWithOnConflict(DB_TABLE,null,values,DB) //todo add on conflict do nothing
        return database.insertOrThrow(DB_TABLE, null, values);
    }

    public int deleteRunner(int runnerId) {
        return database.delete(DB_TABLE, ID_RUNNER +"="+ runnerId, null );
    }

    public Cursor retrayRunner(String username, String password) throws Exception {
        String cols[] = {ID_RUNNER, NAME, SURNAME, USERNAME, PASSWORD, SEX, BIRTH_DATE};
        Cursor mCursor = database.query(true, DB_TABLE, cols, USERNAME + "='" + username + "' AND " + PASSWORD+
                                        "='" + password+"';", null, null, null,
                                        null, null);
        return mCursor;
    }

    public Cursor retrayAllUsernameRunner() throws SQLException {
        String cols[] = {USERNAME};
        Cursor uCursor = database.query( DB_TABLE, cols, null, null,
                                        null, null, null );
        return uCursor;
    }

}
