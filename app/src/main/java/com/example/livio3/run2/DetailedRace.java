package com.example.livio3.run2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livio3.run2.DB.DbAdapter;

public class DetailedRace extends AppCompatActivity implements View.OnClickListener {
    /*
    activity per visualizzare tutte le info su una gara
        DISABLED BUTTON PRENOTATION <=== EXPIRED PRENOTATION;ALREADY PRENOTATED (?)

    PRENOTATION BTN PRESSED=>confirm=>CHECK AVAIBILITY
                                         (YES) => INSERTED IN DB
                     <=CONFIRM TEXT IN DIALOG
                     by callback method bookResult


       */

    private TextView localityTv;
    private TextView bookingRes;
    private TextView descriptionTv;
    private TextView timeRaceTv;
    private TextView dateRaceTv;
    private TextView nameRaceTv;
    private TextView distanceTv;
    private TextView prenExpireTv;
    private TextView maxNumRannerTv;
    private TextView urlSite;
    private TextView noteTv;
    private ImageView largeImg;
    private Button btnBack;
    private Button btnConfirm;

    private String idMember;

    private DbAdapter dbAdapter;
    protected static final String INTENT_SWAP_STR = "race";
    private Race clickedRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_race);
        descriptionTv = findViewById(R.id.descriptionTv);
        timeRaceTv = findViewById(R.id.timeRaceTv);
        bookingRes = findViewById(R.id.bookingRes);
        dateRaceTv = findViewById(R.id.dateRaceTv);
        nameRaceTv = findViewById(R.id.nameRaceTv);
        distanceTv = findViewById(R.id.distanceTv);
        prenExpireTv = findViewById(R.id.prenExpireTv);
        urlSite= findViewById(R.id.urlSite);
        maxNumRannerTv = findViewById(R.id.maxNumRunnerTv);
        noteTv = findViewById(R.id.noteTv);
        largeImg = findViewById(R.id.raceImgVBig);
        btnBack = findViewById(R.id.back2Races);
        btnConfirm = findViewById(R.id.btnConfirm);
        localityTv = findViewById(R.id.localityTv);
        Intent intent = getIntent();
        MyParcelable myParcelable = (MyParcelable) intent.getExtras().getParcelable(INTENT_SWAP_STR);
        clickedRace = myParcelable.getObject();



        distanceTv.setText(String.valueOf(clickedRace.getDistance()));
        prenExpireTv.setText(clickedRace.getPrenExpire().toStringWellPrinted());
        maxNumRannerTv.setText(String.valueOf(clickedRace.getN_max_runner()));
        dateRaceTv.setText((clickedRace.getDateRace().toStringDate()));
        localityTv.setText(clickedRace.getLocality());
        descriptionTv.setText(clickedRace.getDescription());
        timeRaceTv.setText(clickedRace.getDateRace().toStringTime());
        nameRaceTv.setText(clickedRace.getName());
        noteTv.setText(clickedRace.getNote());
        urlSite.setText(clickedRace.getUrlRace());
        localityTv.setOnClickListener(new View.OnClickListener() {
            //on click open maps
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+clickedRace.getLocality());

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        });
        urlSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = clickedRace.getUrlRace();
                if(url.length() > 0 && url != null) {
                    // Make the Intent explicit by setting the Browser
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    // Attempt to start an activity that can handle the Intent
                    startActivity(browserIntent);
                }
            }
        });
        Bitmap downloadedBitmap = ListRace.imgBuffer.get(clickedRace.getUrlImage());

        if (downloadedBitmap != null)                  //set downloaded img not scaled
            largeImg.setImageBitmap(downloadedBitmap);
        else
            largeImg.setImageDrawable(getDrawable(R.drawable.corsa));
        largeImg.setScaleType(ImageView.ScaleType.FIT_XY);
        //todo check unside effect on resizing in ListviewAdapter :D
        distanceTv.setText(String.valueOf(clickedRace.getDistance()));
        //setting listeners
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        //recupero l'id del socio dall'intent
        Bundle data = getIntent().getExtras();
        idMember = data.getString(LoginActivity.KEY_ID);
        System.out.println("myid:" + idMember);
        prenotatabilityCheck();                         //basic controls before prenotate
    }

    private boolean prenotatabilityCheck() {
        /*check if basic costrains are violated before ask to prenote...
            expired date prenotation or already prenotated....
            set UI concordly with prenotability
         */
        boolean prenotability = true;                 //rappresent possibility to book the race
        //check expireddate and double prenotation costraints
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        boolean expiredBookTerm, alreadyBooked;
        expiredBookTerm = controlExpiredPrenotation();
        alreadyBooked = dbAdapter.checkDoublePrenotation(Integer.parseInt(idMember), clickedRace.getId_race());
        //expired book date or already booked the race=>not possible to book this race..
        if (expiredBookTerm || alreadyBooked)
            prenotability = false;
        dbAdapter.close();
        System.out.println("prenotability:\t" + prenotability + clickedRace.toString());
        //set UI related to prenotability

        if (!prenotability)   //false=>disable button todo and set explination in textbox
        {
            this.btnConfirm.setClickable(false);    //disable btm confirm
            btnConfirm.setVisibility(View.INVISIBLE);   //hide btn if race not bookable
            if(expiredBookTerm)
                this.bookingRes.setText(R.string.expiredBookTerm);
            else if(alreadyBooked)
                this.bookingRes.setText(R.string.alreadyBooked);
            //todo set textbox with reason becaouse isn't prenotable this race... see boolean with reasons of not possible book
            System.out.println("\n\nalready booked:" + alreadyBooked + "\nexpired date:" + expiredBookTerm);
        }
        return prenotability;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back2Races:
                Intent intent = new Intent(this, ListRace.class);
                intent.putExtra(LoginActivity.KEY_ID, idMember);
                startActivity(intent);
                break;
            case R.id.btnConfirm:
                // booking logic & checks avaibility in dialog onclick method
                this.confirmDialogBasic();
                break;

        }
    }

    private boolean controlExpiredPrenotation() {
        //return true if is expired booking date... false otherwise
        DateRace dateExpired = clickedRace.getPrenExpire();
        DateRace today = DateRace.now();

        //to handle date in devices with old os
        if (DateRace.compareDateRace(today, dateExpired) > 0) {
            System.out.println(today.toString());
            return true; //expired...
        }
        return false;
    }


    public void confirmDialogBasic() {
        /*
        allert dialog creation for confirmation of prenotation
         */
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle(R.string.confirmPrenotation);
        String body = (this.clickedRace.getName());         //todo more infos..?
        dialog.setCancelable(false);
        dialog.setMessage(body);
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {   //yes pressed
                        System.out.println("confirmed!");
                        btnConfirm.setClickable(false);
                        CheckAvaibilityTask checkAvaibilityTask = new CheckAvaibilityTask(clickedRace, idMember, DetailedRace.this);
                        checkAvaibilityTask.execute();

                    }
                });
        dialog.setNegativeButton("No", null);
        dialog.show();

    }

    public void allertDialogComunication(String title, String bodyStr) {
        /*
        allert dialog to comunicate important info
        example:=>not possible to book this race...
         */

        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle(title);
        String body = (bodyStr);
        dialog.setCancelable(false);
        dialog.setMessage(body);

        dialog.setNegativeButton("OK", null);
        dialog.show();

    }

    public void bookResult(boolean bookRes, String body) {
        /* get booking result async (from simulated rest server)
            bookRes: true if race has been accepted and written in db
         */
        //TODO UI VIEW UPDATE WITH THIS RESOULT(EXAMPLE CONFIRMED=>FLAG ALREADY BOOKED...
        String title;
        if (bookRes) {//confirmed booking!
            title = getString(R.string.confirmedTitle);
            btnConfirm.setVisibility(View.INVISIBLE);
            bookingRes.setText(R.string.alreadyBooked);
        } else {
            title = getString(R.string.negativeBookResoult);

        }
        allertDialogComunication(title, body);
    }
}
