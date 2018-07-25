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
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaGare extends AppCompatActivity {
/*
    activity che consente di visualizzare le gare disponibili
        ->scaricate(json & imaggini)
        ====>json : prenotazioni & gare, controllato timestamp in cache
                       ->se troppo vecchi riscaricate

 */
    private String idMember;
    private ListView lvGare;
    private Button btnBack;
    private List<Race> races;
    protected static Map<String,Bitmap> imgBuffer=new HashMap<>();  //imgs cache as static map
    //protected static Map<String,String> jsonBuf=new HashMap<>();  //imgs cache as static map
    protected static String jsonRaces;
    protected static int MAXIMAGEBYTES= 500;
    protected static final int MAXWIDTH=100;                        //max dim for img in listview
    protected static final int MAXHEGHT=100;
    protected static ProgressBar pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        races = new ArrayList<>();
        setContentView(R.layout.activity_lista_gare);
        //getting runner id from intent
        try {
            Bundle data = getIntent().getExtras();
            idMember = data.getString(LoginActivity.KEY_ID);
            System.out.println(idMember);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaGare.this, MenuActivity.class);
                intent.putExtra(LoginActivity.KEY_ID, idMember);
                startActivity(intent);
            }
        });

        lvGare = findViewById(R.id.lvGare);
        lvGare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Race raceClicked;
                raceClicked = races.get(position);

                Intent intent = new Intent(ListaGare.this, DetailedRace.class);
                intent.putExtra(LoginActivity.KEY_ID, idMember);
                intent.putExtra(DetailedRace.INTENT_SWAP_STR, raceClicked);//todo check serializeble work
                startActivity(intent);
            }
        });
         initBufs(); //start downloaders tasks buf will be filled
    }

    private void initBufs() {
        /*
        initialize buffer,
            ->TODO CHECK TIMESTAMP OF BUFFERED FILES IF TOO OLDER REDOWNLOAD
                ->IF OLD DOWNLOAD=>START DOWNLOADER TASKS AND SET DATAs..

         */

        //TODO CHECK CHACHED FILES
        //TODO IF TOO OLD (here continue)
        Toast.makeText(this,R.string.waitTxt,1).show();
        //TODO SET PROGRESS BAR TO SHOW WAITING
        //DownloaderTask<String> downloaderTask=new DownloaderTask<>(Downloader.gareUrlJson,this,DownloaderTask.JSON);
        //downloaderTask.execute();

        //todo download prenotazioni json
        //todo serialize in chached files
        try {
            races= JsonHandler.try1();  //todo tmp set race from static json
        } catch (JSONException e) {
            e.printStackTrace();
        }
        downloadImages();//start downloading images

    }


    @Override
    protected void onStart() {
        super.onStart();

    }
    protected void setJsonRaces(String jsonStr){
        //parsing json and setting data in listview
        ListaGare.jsonRaces=jsonStr;
        JsonHandler handler = new JsonHandler(jsonStr);
        races = handler.getRaces();


    }
    protected void addImageInChache(String url,Bitmap image){
        //add a downloaded image in cache
        imgBuffer.put(url,image);
        //TODO COUNT DOWNLOAD...WHEN DOWNLOADED THE LAST ONE
        RaceAdapter raceAdapter = new RaceAdapter(this, R.layout.item_race, races);
        lvGare.setAdapter(raceAdapter);
    }
    private void downloadImages(){
        //TODO DOWNLOADING IMAGES AND SAVING THEM IN CACHE (STATIC BITMAT LIST)
        //evalutate alternatives... asynctask not supposed to do long run ops...
        for (int j = 0; j < races.size(); j++) {
            Race race = races.get(j);
            if (race.getUrlImage() != "") {
//                Object o = lvGare.getAdapter().getItem(j);
//                ImageView referenceToImageOfRace = null;        //TODO LIVIO TAKE FROM LISTVIEW
                DownloaderTask<Bitmap> downloaderTask = new DownloaderTask<>(race.getUrlImage(),
                        this,DownloaderTask.IMG);
                Bitmap img = null;
                try {
                    //starting task to download image...
                    //at the end will be called async addImageInCache
                    downloaderTask.execute();
                } catch (Exception e) {
                    e.printStackTrace();    //NOT POSSIBLE DOWNLOAD IMG=>SET NULL FOR THAT URL
                }


            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
