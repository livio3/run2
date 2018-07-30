package com.example.livio3.run2;

import android.content.Intent;
import android.database.SQLException;
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

import com.example.livio3.run2.DB.DbAdapter;

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
             =>parsato json si riscaricano le immagini

 */
    private String idMember;
    private ListView lvGare;
    private Button btnBack;
    private List<Race> races;
    protected static Map<String,Bitmap> imgBuffer=new HashMap<>();  //imgs cache as static map
//    protected static Map<String,String> jsonBuf=new HashMap<>();  //json cache as static map
    protected static String jsonRaces;
    protected static int MAXIMAGEBYTES= 500;
    protected static final int MAXWIDTH=100;                        //max dim for img in listview
    protected static final int MAXHEGHT=100;
    protected static ProgressBar progressBar;

    protected DbAdapter dbAdapter;
    private int toDownload=0;            //num of   images to download for this session TODO PROGRESS BAR
    private int downloaded=0;            // num of compleated(impossible to download or downloaded)imgs
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
        progressBar=findViewById(R.id.progressBar); //start as invisible...

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
                MyParcelable myParcelable = new MyParcelable();
                myParcelable.setObject(raceClicked);
                intent.putExtra(DetailedRace.INTENT_SWAP_STR,myParcelable); //todo check serializeble work
                startActivity(intent);
            }
        });
         dbAdapter = new DbAdapter(this);
         initBufs(); //start downloaders tasks buf will be filled
    }

    private void initBufs() {
        /*
        initialize bufs with data...
            -> CHECK TIMESTAMP OF CACHED JSON... IF OLDER THEN TODAY -> RE-DOWNLOAD
                ->IF OLD DOWNLOAD=>START DOWNLOADER TASKS AND SET DATAs..

        */
        String jsonStr=null;
        try {
            dbAdapter.open();
            jsonStr = dbAdapter.takeJasonString(costants.urlRacesJson);
            if(jsonStr==null){
                //empty cache (probably first use or resetted app
                //download
                System.out.println("empty db cache..");
                DownloaderTask<String> downloaderTask= new DownloaderTask<>(costants.urlRacesJson,this,DownloaderTask.JSON);
                downloaderTask.execute(); //will be setted in cache too from downloadtask
            }
            else { //valid cached json string value..
                this.setJsonRaces(jsonStr);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbAdapter.close();
        }
        downloadImages(); //start downloading images (data will be taken by callback method from tsk
    }


    @Override
    protected void onStart() {
        super.onStart();

    }
    protected void setJsonRaces(String jsonStr){
        //callback from download task
        //parsing json and setting data in listview;
        ListaGare.jsonRaces=jsonStr;            //set local "runtime "cache
        JsonHandler handler = new JsonHandler(jsonStr);
        races = handler.getRaces();             //set parsed races from json :)
        lvGare.setAdapter(new RaceAdapter(this, R.layout.item_race, races));
        //set listview with text info only(imgs setted later...)
    }

    protected void addImageInChache(String url,Bitmap image){
        //add a downloaded image in cache
        if(image!=null)                 //download error case...
            imgBuffer.put(url,image);
        downloaded++;
        System.out.println(downloaded+"\t"+toDownload);
        float percentDownloadDone=((float) downloaded/(float) toDownload)*100;
        System.out.println("downloaded "+url+"%:\t"+percentDownloadDone);
        progressBar.setProgress(Math.round(percentDownloadDone));
        //todo better (FASTER) ALTERNATIVES TO UPDATE IMAGES...
        //lvGare.setAdapter(raceAdapter); //todo old always reset all imgs..
        if(downloaded==toDownload)         //compleated all downloads
            lvGare.setAdapter(new RaceAdapter(this, R.layout.item_race, races));
            //reset with imgs..(updated cache)
    }
    private void downloadImages(){
        /* SCHEDULE DOWNLOADING IMAGES AND SAVING THEM IN local runtime cache*/
        //1 async task for eatch image, little images=>short op (hopefully )
        progressBar.setVisibility(View.VISIBLE);
        List<String> toDownloadUrls=new ArrayList<>();
        for(int x=0;x<races.size();x++){            //getting imgs url to download...
            String urlImg=races.get(x).getUrlImage();
            if(imgBuffer.get(urlImg)==null) //not in img runtime cache...
                toDownloadUrls.add(urlImg);
        }
        toDownload=toDownloadUrls.size();        //set global var with num of download to schedule
        for (int j = 0; j < toDownloadUrls.size();j++) {    //starting downloads...
            DownloaderTask<Bitmap> downloaderTask = new DownloaderTask<>(toDownloadUrls.get(j),
                    this,DownloaderTask.IMG); //started download in another thread
            downloaderTask.execute(); //at the end will be called async addImageInCache
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
