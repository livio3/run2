package com.example.livio3.run2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.livio3.run2.DateRace;
import com.example.livio3.run2.ListaGare;

import java.util.ArrayList;
import java.util.List;
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

    //CACHE TABLE Fields
    protected static final String DB_TABLE_CACHE = "cache";
    protected static final String URL = "url";
    protected static final String DATA_CACHED = "data_cached";
    protected static final String DATE = "date";


    public  DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();              //TODO INDIFFERENTE SE NECESSARIA SOLO DESERIALIZZAZIONE?

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

    /*
    recupero un socio in caso l'username e la password siano corretti
     */

    public Cursor retrayRunner(String username, String password) throws Exception {
        String cols[] = {ID_RUNNER, NAME, SURNAME, USERNAME, PASSWORD, SEX, BIRTH_DATE};
        Cursor mCursor = database.query(true, DB_TABLE_USER, cols, USERNAME + "='" + username + "' AND " + PASSWORD+
                        "='" + password+"';", null, null, null,
                null, null);
        return mCursor;
    }

   /*
   utilizzato per l'AutoCompleteTextView, recupero tutti gli usernames presenti nel sistema
    */

    public Cursor retrayAllUsernameRunner() throws SQLException {
        String cols[] = {USERNAME};
        Cursor uCursor = database.query(DB_TABLE_USER, cols, null, null,
                null, null, null );

        return uCursor;
    }

    /*
    controllo se una certa gara non ha finito i posti disponibili per l'iscrizione
     */
    public boolean avaibilityPrenotazion(int nMax, int idRace) throws SQLException {
        String cols[] = {ID_RACE};
        Cursor cursor = database.query(DB_TABLE_PRENOTATION, cols, ID_RACE + "= " + idRace +" ;",
                null,null,null,null);
        boolean ret = cursor.getCount() == nMax;  //tonra vero se ha raggiunto il numero massimo di partecipanti
        cursor.close();
        return ret;

    }

    /*
    questo metodo serve per controllare il caso di un socio che è gia prenotato per una determinata gara
     */
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


    public List<List<String>> takeAllCached(){
        /*return cached data table as string list
            <<url,data,date>...>
        * or null if empty cache*/
        List<List<String>> result=new ArrayList<>();
        String sqlGetAll="select * from "+DB_TABLE_CACHE+";";
        Cursor cursor= database.rawQuery(sqlGetAll,null);
        boolean hasNextRow=cursor.moveToFirst();
        if(hasNextRow==false)
            return null;
        int urlCol=cursor.getColumnIndex(URL);
        int dataCol=cursor.getColumnIndex(DATA_CACHED);
        int dateCol=cursor.getColumnIndex(DATE);
        int records=cursor.getCount();
        while (hasNextRow){
            List<String> row=new ArrayList<>();
            row.add(cursor.getString(urlCol));
            row.add(cursor.getString(dataCol));
            row.add(cursor.getString(dateCol));
            result.add(row);
            hasNextRow = cursor.moveToNext();
        }
        return result;
    }
    public List<String> takeCacheUrls(){
        List<String> result=new ArrayList<>();
        String sqlGetAll="select * from "+DB_TABLE_CACHE+";";
        Cursor cursor= database.rawQuery(sqlGetAll,null);
        boolean hasNextRow=cursor.moveToFirst();
        if(hasNextRow==false)
            return null;
        int urlCol=cursor.getColumnIndex(URL);
//        int dataCol=cursor.getColumnIndex(DATA_CACHED);
//        int dateCol=cursor.getColumnIndex(DATE);
        do{
            result.add(cursor.getString(urlCol));
        }
        while (cursor.moveToNext());
        return result;
    }
    public String takeCachedData(String url) throws SQLException{
        /*
    qui prendo i dti del file jason delle prenotazioni se questi dati sono stati scaricati nel giorno
    corrente, altrimenti elimino perchè le informazioni potrebbero essere non valide, funziona coe cache.
     return null se nn è presente in cache il json richiesto ( o è stato invalidato
     */
        String cols[] = {DATA_CACHED, DATE};
        //System.err.println(DatabaseHelper.CREATE_CACHE);

        Cursor cursor = database.query(DB_TABLE_CACHE, cols, URL +"= '" + url+"';", null, null, null,
                null);
        String jason = null;
        if(cursor.getCount()!=1)
            return null;
        cursor.moveToFirst();                //move to first(and only)
        String date = cursor.getString(1);
        DateRace dateTimestap = new DateRace(date);
        DateRace dateNow = DateRace.now();
        //se il file è stato scaricato nello stesso giorno non lo riscarico.
        if(dateTimestap.getYear() == dateNow.getYear() && dateTimestap.getDay() == dateNow.getDay()
                && dateTimestap.getMonth() == dateNow.getMonth())
            jason =cursor.getString(0);
        else
            deleteJason(url); //altrimenti lo elimino perchè potrebbe nn essere più valido
        cursor.close();
        return jason;
    }



    private int deleteJason(String url) {
        return database.delete(DB_TABLE_CACHE, URL +"='"+ url+"';", null );
    }


    /*
    inserisco i dati del file jason scaricato con la data corrente.
     */
    public long addRawCache(String url, String jasonString) {
        ContentValues values = createContentValues(url, jasonString);
        return database.insertOrThrow(DB_TABLE_CACHE, null, values);

    }


    private ContentValues createContentValues(String url, String jasonString) {
        ContentValues values = new ContentValues();
        values.put(URL,url);
        values.put(DATA_CACHED, jasonString);
        values.put(DATE, DateRace.now().toString());

        return values;
    }


    public void invalidAllCache() {
        /*invalid(delete) all cache records
            return delleted records...
         */
        database.delete(DB_TABLE_CACHE,null,null);
    }
}
