package com.example.livio3.run2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListaGare extends AppCompatActivity {
    private ListView lvGare;
    private Button btnBack;
    private List<Race> races;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        races=new ArrayList<>();
        //random generation
        for(int i=0;i<10;i++)
            races.add(new Race());
        setContentView(R.layout.activity_lista_gare);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        btnBack = findViewById(R.id.btnBack);

        lvGare = findViewById(R.id.lvGare);
        RaceAdapter raceAdapter = new RaceAdapter(this, R.layout.item_race, races);
        lvGare.setAdapter(raceAdapter);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }



    @Override
    protected void onStart() {
        super.onStart();
        //gsonTry();

    }
    private void gsonTry(){
        String json="[{\"idrace\": 1,\"description\": \"MEZZA MARATONA DI ROMA\",\"locality\": \"ROMA (RM)\",\"distance\": 42195," +
                "\"dateRace\": \"2018/06/16 21:00\",\"prenExspire\": \"2018/06/10\",\"urlRace\": \"https://www.mezzamaratonadiroma.it/\",\"note\": \"Mezza Maratona di Roma in notturna. Occhio alle buche!\"," +
                "\"urlImage\": \"https://www.mezzamaratonadiroma.it/PWA_uploads/logo-.png\"}]";
        /*Gson gson= new Gson();
        Race race=new Race();
        String outJ=gson.toJson(race);
        race=null;
        Race[] races= gson.fromJson(json,Race[].class);*/
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(json);
            JSONObject jsonObject= (JSONObject) jsonArray.get(0); //i ersimo obj(json wrapped) in json array string
            Iterator<String> i= jsonObject.keys();  //iterabile "lista " di chiavi relativi a valori in elemento i esimo
            String idrace = jsonObject.getString("idrace"); //cosi riepiro i campi...
            System.out.print("adnrstudnafo");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Race race= new Race();
        System.out.println();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            JsonHandler.try1();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
