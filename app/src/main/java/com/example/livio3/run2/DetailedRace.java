package com.example.livio3.run2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livio3.run2.DB.DbAdapter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class DetailedRace extends AppCompatActivity implements View.OnClickListener {
    /*
    activity per visualizzare tutte le info su una gara
     */

    private TextView localityTv;
    private TextView descriptionTv;
    private TextView timeRaceTv;
    private TextView dateRaceTv;
    private TextView nameRaceTv;
    private TextView distanceTv;
    private TextView prenExpireTv;
    private TextView maxNumRannerTv;
    private TextView noteTv;
    private ImageView largeImg;
    private Button btnBack;
    private Button btnConfirm;

    private String idMember;

    private DbAdapter dbAdapter;
    protected static final String INTENT_SWAP_STR="race";
    private Race clickedRace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_race);
        descriptionTv = findViewById(R.id.descriptionTv);
        timeRaceTv = findViewById(R.id.timeRaceTv);
        dateRaceTv = findViewById(R.id.dateRaceTv);
        nameRaceTv = findViewById(R.id.nameRaceTv);
        distanceTv = findViewById(R.id.distanceTv);
        prenExpireTv = findViewById(R.id.prenExpireTv);
        maxNumRannerTv = findViewById(R.id.maxNumRunnerTv);
        noteTv = findViewById(R.id.noteTv);
        largeImg = findViewById(R.id.raceImgVBig);
        btnBack = findViewById(R.id.back2Races);
        btnConfirm=findViewById(R.id.btnConfirm);
        localityTv = findViewById(R.id.localityTv);
        Intent intent= getIntent();
        MyParcelable myParcelable = (MyParcelable)intent.getExtras().getParcelable(INTENT_SWAP_STR);
        clickedRace = myParcelable.getObject();


        //TODO MAP RACE ATTRIBUTE IN TEXTVIEWS OF THIS ACTIVITY
        distanceTv.append(String.valueOf(clickedRace.getDistance()));
        maxNumRannerTv.append(String.valueOf(clickedRace.getN_max_runner()));
        dateRaceTv.append((clickedRace.getDateRace().toStringDate()));
        localityTv.append( clickedRace.getLocality());
        descriptionTv.append(clickedRace.getDescription());
        timeRaceTv.append(clickedRace.getDateRace().toStringTime());
        nameRaceTv.append(clickedRace.getName());
        noteTv.append( clickedRace.getNote());
        prenExpireTv.append(clickedRace.getPrenExpire().toStringDate());
        Bitmap downloadedBitmap=ListaGare.imgBuffer.get(clickedRace.getUrlImage());
        if(downloadedBitmap!=null)
            largeImg.setImageBitmap(downloadedBitmap);

        //todo check unside effect on resizing in ListviewAdapter :D
        distanceTv.append(String.valueOf(clickedRace.getDistance()));
        //setting listeners
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener( this);


        //recupero l'id del socio dall'intent
        Bundle data = getIntent().getExtras();
        idMember = data.getString(LoginActivity.KEY_ID);
        System.out.println(idMember);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back2Races:
                Intent intent = new Intent(this,ListaGare.class);
                intent.putExtra(LoginActivity.KEY_ID, idMember);
                startActivity(intent);
                break;
            case R.id.btnConfirm:
                if(!controlExspiredPrenotation()) {
                    negativeDialogBasic();
                    return;
                }

                CheckAvaibilityTask checkAvaibilityTask = new CheckAvaibilityTask(clickedRace, idMember, this);
                checkAvaibilityTask.execute();
                try {
                    if(checkAvaibilityTask.get() == null) {
                        confirmDialogBasic();
                        System.out.println("returned from allert dialog");
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        }
    }

    private boolean controlExspiredPrenotation() {
        DateRace dataExspired = clickedRace.getPrenExpire();
        DateRace today = DateRace.now();
        if (DateRace.compareDateRace(today, dataExspired) > 0) {
            return false;
        }
        return true;
    }


    public  void confirmDialogBasic(){
        /*
        allert dialog creation for confirmation of prenotation
         */
         boolean output=false;
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("CONFIRM PRENOTATION OF!");
        String body = (this.clickedRace.getName());
        dialog.setCancelable(false);
        dialog.setMessage(body);
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {   //yes pressed
                        System.out.println("confirmed!");
                        InsertPrenotation insertPrenotation = new InsertPrenotation(idMember, clickedRace.getId_race(), DetailedRace.this);
                        insertPrenotation.execute();

                    }
                });
        dialog.setNegativeButton("No", null);
        dialog.show();

    }

    public  void negativeDialogBasic(){
        /*
        allert dialog creation for prenotation not valid
         */
        boolean output=false;
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("DATA DI PRENOTAZIONE SCADUTA");
        String body = (this.clickedRace.getName()+"\n"+ "Scadenza: "+this.clickedRace.getPrenExpire());
        dialog.setCancelable(false);
        dialog.setMessage(body);

        dialog.setNegativeButton("OK", null);
        dialog.show();

    }
}
