package com.example.livio3.run2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

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
        Intent intent= getIntent();
        Serializable race = intent.getSerializableExtra(INTENT_SWAP_STR);
        clickedRace = (Race) race;
        //TODO MAP RACE ATTRIBUTE IN TEXTVIEWS OF THIS ACTIVITY
        distanceTv.setText(String.valueOf(clickedRace.getDistance()));
        //setting listeners
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener( this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back2Races:
                startActivity(new Intent(this,ListaGare.class));
                break;
            case R.id.btnConfirm:
                //TODO livio CHECK IF EXPIRED PRENOTATION
                //TODO livio CHECK THERE IS AVAIBILITY
                confirmDialogBasic();
                System.out.println("returned from allert dialog");
                break;
        }
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
                        //TODO LIVIO WRITE IN DB prenotation
                    }
                });
        dialog.setNegativeButton("No", null);
        dialog.show();

    }
}
