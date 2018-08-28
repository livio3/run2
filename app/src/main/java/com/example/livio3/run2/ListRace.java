package com.example.livio3.run2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.livio3.run2.DB.DbAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRace extends AppCompatActivity {
    private static final String IMGSKEY = "IMGS";
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
    protected static int toDownload=0;            //num of   images to download for this session TODO PROGRESS BAR
    protected static int downloaded=0;            // num of compleated(impossible to download or downloaded)imgs

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
                Intent intent = new Intent(ListRace.this, MenuActivity.class);
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

                Intent intent = new Intent(ListRace.this, DetailedRace.class);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        try{
//            dbAdapter.open();
//            dbAdapter.takeAllCached();
//        }
//        catch (SQLException e)
//        { e.printStackTrace();}
        //SAVE ISTANCE=>ALL DOWNLOADED IS SERIALIZED...
    }

    private void initBufs() {
        /*
        initialize bufs with data...
            -> CHECK TIMESTAMP OF CACHED JSON... IF OLDER THEN TODAY -> RE-DOWNLOAD
                ->IF OLD DOWNLOAD=>START DOWNLOADER TASKS AND SET DATAs..

        */
        //getting data from cache in db




        String jsonStr=null;
        try {
            dbAdapter.open();
            jsonStr = dbAdapter.takeCachedData(costants.urlRacesJson);
            if(jsonStr==null){
                //no json in cache... redownload everithing...
                //TODO NO JSON=>SERIALIZED IMGS PROBABLY OLD(IF EXIST)
                //TODO CLEAN DB cache...
                dbAdapter.invalidAllCache();

                //first check network connection

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm.getActiveNetworkInfo() == null){
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(this);
                    dialog.setTitle(getString(R.string.connectionError));
                    String body = (getString(R.string.fixConnection));
                    dialog.setCancelable(false);
                    dialog.setMessage(body);

                    dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //ON CONNECTION ERROR WILL BE RECALLED INITALIZATION
                            //HOPEFULLY USER WILL FIX THE PROBLEM OR CLOSE...
                            initBufs();
                        }
                    });
                    dialog.show();

                }


                //setJson need imgs already taken
                System.out.println("empty db cache..");
                DownloaderTask<String> downloaderTask= new DownloaderTask<>(costants.urlRacesJson,this,DownloaderTask.JSON);
                downloaderTask.execute(); //will be setted in cache too from downloadtask
            }
            else { //valid cached json string value
                System.out.println("cached json");
                //attemping to get imgs too....
                //taking urls of cached data...
                List<String> cacheUrls=dbAdapter.takeCacheUrls();
                //getting imgs from cache...
                for(int s=0;s<cacheUrls.size();s++) {
                    String url = cacheUrls.get(s);
                    //ignoring jsons...at this step already taken.
                    if (url.equals(costants.urlRacesJson))
                        continue;   //already taken before...
                    String cachedData = dbAdapter.takeCachedData(url);
                    if (cachedData != null) {    //valid img in cache...
                        byte[] deserializedData = Base64.decode(cachedData, Base64.DEFAULT);
                        Bitmap cachedImg = BitmapFactory.decodeByteArray(deserializedData, 0, deserializedData.length);
                        imgBuffer.put(url, cachedImg);
                    }
                }
                System.out.println("correctly cached imgs:"+imgBuffer.size());
                this.setJsonRaces(jsonStr);
                }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbAdapter.close();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }
//    @Override
//    protected void onResume(){
//        System.out.println("resumed"+races.size()+imgBuffer.size());
//        toDownload=0;
//        downloaded=0;
//        lvGare.setAdapter(new RaceAdapter(this, R.layout.item_race, races));
//
//    }
    protected void setJsonRaces(String jsonStr){
        //callback from download task
        //parsing json and setting data in listview;
        ListRace.jsonRaces=jsonStr;            //set local "runtime "cache
        JsonHandler handler = new JsonHandler(jsonStr);
        races = handler.getRaces();             //set parsed races from json :)
        lvGare.setAdapter(new RaceAdapter(this, R.layout.item_race, races));
        System.out.println("json setted & downloading imgs");
        downloadImages(); //download imgs not (actually valid  ) in cache
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
        //lvGare.setAdapter(raceAdapter); //todo old always reset all imgs..
        //todo better (FASTER) ALTERNATIVES TO UPDATE IMAGES... set imgs only when all completeded
        if(downloaded==toDownload)         //compleated all downloads
        {   //ENDED ALL DOWNLOAD
            lvGare.setAdapter(new RaceAdapter(this, R.layout.item_race, races));
            //reset with imgs..(updated cache)

        }

    }
    private void downloadImages(){
        /* try take img in serialized cache in db...
        SCHEDULE DOWNLOADING REMAINING IMAGES AND SAVING THEM IN local runtime cache*/
        //1 async task for eatch image, little images=>short op (hopefully )



        List<String> toDownloadUrls=new ArrayList<>();
        //getting imgs url to download=>not cached imgs...
        for(int x=0;x<races.size();x++){
            String urlImg=races.get(x).getUrlImage();
            if(urlImg!=null &&  imgBuffer.get(urlImg)==null) //not in imgs cache...
                toDownloadUrls.add(urlImg);
        }
        downloaded=0;
        toDownload=toDownloadUrls.size();        //set global var with num of download to schedule
        if(toDownload>0)    //set prss bar only if something has to be downloaded
            progressBar.setVisibility(View.VISIBLE);
        for (int j = 0; j < toDownloadUrls.size();j++) {    //starting downloads...
            DownloaderTask<Bitmap> downloaderTask = new DownloaderTask<>(toDownloadUrls.get(j),
                    this,DownloaderTask.IMG); //started download in another thread
            downloaderTask.execute(); //at the end will be called async addImageInCache
        }
        System.out.println("scheduled "+toDownloadUrls.size()+"download");
    }

}
