package com.example.livio3.run2;

import android.database.SQLException;
import android.os.AsyncTask;

import com.example.livio3.run2.DB.DbAdapter;

/**
 * SIMULATION of rest server,
 *  handled extra booking costraint(max num of runner 4 specified race)
 *  if all correct race booking added to db and UI notified by callback method
 */

public class CheckAvaibilityTask extends AsyncTask<Void, Void, String> {

    private DetailedRace detailedRaceActivity;
//    private static final String MSX_DOUBLE_PRENOTATION = "Sei già prenotato per questa gara";
//    private static final String MSX_NMAX = "Numero di partecipanti raggiunto massimo";
//    private static final String ERROR_CONNECTION = "Errore di connessione" ; //TODO STRING HAS TO STAY IN R.STRINGS...


    private Race race;
    private int idMember;
    DbAdapter dbAdapter;

    public CheckAvaibilityTask(Race race, String idMember, DetailedRace context) {
        this.detailedRaceActivity = context;
        this.race = race;
        this.idMember = Integer.valueOf(idMember);
        dbAdapter = new DbAdapter(context);
    }

    @Override
    protected String doInBackground(Void... voids) {
        /*
        TODO SIMULATION SERVER POST
        client:
          <-POST BOOKING REQUEST FOR race--->                          server
                            server: checks....
          <-confirm book || unavaibility space || other errors...<---- server
        local simulation: dbraces check avaibility...if  there's space 4 a new runner 4 the race
                            book race in db and notify UI ...
         */
        String out = null;
        try {
            dbAdapter.open();
            if(dbAdapter.avaibilityPrenotazion(this.race.getN_max_runner(), this.race.getId_race()))
                out = detailedRaceActivity.getString(R.string.reatchedMaxNumRunner);

            else    //if there's space enought 4 a new runner booke the race writing in db...
                dbAdapter.addPrenotation(idMember,race.getId_race());   //add book if all is ok!
        }
        catch (SQLException e) {
            e.printStackTrace();
            out = detailedRaceActivity.getString(R.string.connectionError);

        }
        finally {
            dbAdapter.close();

        }


        return out ; //no err string setted=>booking correctly written!
    }

    @Override
    protected void onPostExecute(String errresoult) {
        //async notify UI by bookResult callback method...

        if(errresoult!=null)    //errstr setted=>something has gone wrong...
            detailedRaceActivity.bookResult(false,errresoult);
        else //correctly inserted book for the race
            detailedRaceActivity.bookResult(true,detailedRaceActivity.getString(R.string.confirmedBody));
    }
}

