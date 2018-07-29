package com.example.livio3.run2;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;

import com.example.livio3.run2.DB.DbAdapter;

/**
 * Created by livio3 on 26/07/18.
 * obsolete
 */
@Deprecated
public class InsertPrenotation extends AsyncTask<Void, Void, String> {

    private static final String MSX_ERROR = "Errore nella connessione";

    private int idMember;
    private int idRace;
    private Context context;

    private DbAdapter dbAdapter;

    public InsertPrenotation(int idMember, int idRace, Context context) {
        this.idMember = idMember;
        this.idRace = idRace;
        this.context = context;
        this.dbAdapter = new DbAdapter(context);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String out = null;
        try {
            dbAdapter.open();
            dbAdapter.addPrenotation(idMember, idRace);

        }
        catch (SQLException e) {
            out = MSX_ERROR;
            e.printStackTrace();
        }
        finally {
            dbAdapter.close();
            return out;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null) {
            AlertDialog.Builder dialog =
                    new AlertDialog.Builder(context);
            dialog.setTitle("PRENOTAZIONE NON POSSIBILE");
            String body = (s);
            dialog.setCancelable(false);
            dialog.setMessage(body);

            dialog.setNegativeButton("OK", null);
            dialog.show();

        }
        }
    }

