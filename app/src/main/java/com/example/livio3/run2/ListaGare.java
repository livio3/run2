package com.example.livio3.run2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListaGare extends AppCompatActivity {

    private ListView lvGare;
    private Button btnBack;
    private List<Race> races;
    protected static List<Bitmap> imgBuffer;
    protected static int MAXIMAGEBYTES= 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        races = new ArrayList<>();
        //random generation

        setContentView(R.layout.activity_lista_gare);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        btnBack = findViewById(R.id.btnBack);

        lvGare = findViewById(R.id.lvGare);

        lvGare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Race raceClicked;
                raceClicked = races.get(position);
                Intent intent = new Intent(ListaGare.this, DetailedRace.class);
                intent.putExtra(DetailedRace.INTENT_SWAP_STR, raceClicked);//todo check serializeble work
                startActivity(intent);
            }
        });

        JsonHandler handler = new JsonHandler(JsonHandler.tryCostant);
        races = handler.getRaces();
        RaceAdapter raceAdapter = new RaceAdapter(this, R.layout.item_race, races);
        lvGare.setAdapter(raceAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void downloadImagesForList(){
        //TODO DOWNLOADING IMAGES AND SAVING THEM IN CACHE (STATIC BITMAT LIST)
        //evalutate alternatives... asynctask not supposed to do long run ops...
        for (int j = 0; j < 2; j++) {
            Race race = races.get(j);
            if (race.getUrlImage() != "") {
                Object o = lvGare.getAdapter().getItem(j);
                ImageView referenceToImageOfRace = null;        //TODO LIVIO TAKE FROM LISTVIEW
                ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(referenceToImageOfRace);
                imageDownloaderTask.doInBackground(race.getUrlImage());
                Bitmap img = null;
                try {
                    imageDownloaderTask.execute(race.getUrlImage());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;           //error download => try next image to download
                }


            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
