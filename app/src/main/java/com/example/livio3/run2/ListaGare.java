package com.example.livio3.run2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListaGare extends AppCompatActivity {
    private ListView lvGare;
    private Button btnBack;
    private List<Race> races;
    protected static List<Bitmap> imgBuffer;

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
/*
        for (int j = 0; j < races.size(); j++) {
            Race race = races.get(j);
            if (race.getUrlImage() != "") {
                ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask();
                imageDownloaderTask.doInBackground(race.getUrlImage());
                Bitmap img = null;
                try {
                    img = imageDownloaderTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (img != null) {
                    Object o = lvGare.getAdapter().getItem(j);
                }
            }
        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
