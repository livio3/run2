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

    //USER TABLE Fields
    private static final String DB_TABLE_USER = "usersystem";
    private static final String ID_RUNNER = "_id";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String SEX = "sex";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String BIRTH_DATE = "birth_date";

    //PRENOTATION TABLE Fields
    private static final String DB_TABLE_PRENOTATION = "prenotation";
    private static final String ID_MEMBER = "idmember";
    private static final String ID_RACE = "idrace";


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
        return database.insertOrThrow(DB_TABLE_USER, null, values);
    }

    public int deleteRunner(int runnerId) {
        return database.delete(DB_TABLE_USER, ID_RUNNER +"="+ runnerId, null );
    }

    public Cursor retrayRunner(String username, String password) throws Exception {
        String cols[] = {ID_RUNNER, NAME, SURNAME, USERNAME, PASSWORD, SEX, BIRTH_DATE};
        Cursor mCursor = database.query(true, DB_TABLE_USER, cols, USERNAME + "='" + username + "' AND " + PASSWORD+
                                        "='" + password+"';", null, null, null,
                                        null, null);
        return mCursor;
    }

    public Cursor retrayAllUsernameRunner() throws SQLException {
        String cols[] = {USERNAME};
        Cursor uCursor = database.query(DB_TABLE_USER, cols, null, null,
                                        null, null, null );

        return uCursor;
    }


    public boolean unavaibilityPrenotazion(int nMax, int idRace) throws SQLException {
        //torna vero se ha raggiunto il numero massimo di partecipanti
        String cols[] = {ID_RACE};
        Cursor cursor = database.query(DB_TABLE_PRENOTATION, cols, ID_RACE + "= " + idRace +" ;",
                                    null,null,null,null);
        boolean ret = cursor.getCount() == nMax;
        cursor.close();
        return ret;

    }

    public boolean checkDoublePrenotation(int idMember, int idRace) {
        String cols[] = {ID_RACE};
        Cursor cursor = database.query(DB_TABLE_PRENOTATION, cols, ID_RACE + "= " + idRace +
                                    " AND " + ID_MEMBER +"="+idMember+";",
                null,null,null,null);
        boolean ret = cursor.getCount() == 1;  //tonra vero se già è prenotato
        cursor.close();
        return ret;
    }

    private ContentValues createContentValues(int idMember, int idRace) {
        ContentValues values = new ContentValues();
        values.put(ID_MEMBER,idMember);
        values.put(ID_RACE, idRace);


        return values;
    }

    public long addPrenotation(int idMember, int idRace) {
        ContentValues values = createContentValues(idMember, idRace);
        return database.insertOrThrow(DB_TABLE_PRENOTATION, null, values);
    }

}
