package com.example.livio3.run2;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;

import com.example.livio3.run2.DB.DbAdapter;

/**
 * Created by livio3 on 25/07/18.
 */

public class CheckAvaibilityTask extends AsyncTask<Void, Void, String> {

    private static final String MSX_DOUBLE_PRENOTATION = "Sei già prenotato per questa gara";
    private Context context;
    private static final String MSX_NMAX = "Numero di partecipanti raggiunto massimo";
    private static final String ERROR_CONNECTION = "Errore di connessione" ;

    private Race race;
    private int idMember;
    DbAdapter dbAdapter;

    public CheckAvaibilityTask(Race race, String idMember, Context context) {
        this.context = context;
        this.race = race;
        this.idMember = Integer.valueOf(idMember);
        dbAdapter = new DbAdapter(context);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String out = null;
        try {
            dbAdapter.open();
            if(dbAdapter.avaibilityPrenotazion(this.race.getN_max_runner(), this.race.getId_race()))
                out = MSX_NMAX;
            if(dbAdapter.checkDoublePrenotation(idMember, race.getId_race()))
                out = MSX_DOUBLE_PRENOTATION;
        }
        catch (SQLException e) {
            e.printStackTrace();
            out = ERROR_CONNECTION;

        }
        finally {
            dbAdapter.close();
            return out;
        }
    }

    @Override
    protected void onPostExecute(String b) {
        if (b != null) {
            AlertDialog.Builder dialog =
                    new AlertDialog.Builder(context);
            dialog.setTitle("PRENOTAZIONE NON POSSIBILE");
            String body = (b);
            dialog.setCancelable(false);
            dialog.setMessage(body);

            dialog.setNegativeButton("OK", null);
            dialog.show();

        }
    }
}

