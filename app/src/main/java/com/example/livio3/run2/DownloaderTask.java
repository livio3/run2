package com.example.livio3.run2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.livio3.run2.DB.DbAdapter;

/**
 * Created by BBOSS on 07/07/2018.
 *
 * try download an image OR a json from internet repo
 * always on fail will'be returned a null
 * notificiation UI th by calling setter method (in runtime and/or serialized cache)
 *
 */
public class DownloaderTask<RETURN> extends AsyncTask<Void, Void, RETURN> {
    public static String photo= "https://drive.google.com/open?id=10pafqpuBt9lWTbLAf2lMbZwqE0hpQQZB";

    private ImageView imageViewReference;           //TODO OBSOLETE
    private Downloader downloaderIstance;
    private String url;                                 //target url to download
    private ListRace listRaceRef;
    protected static final int JSON=96;
    protected static final int IMG=69;
    private int downloadType;

    private DbAdapter dbAdapter;
    public DownloaderTask(String url, ListRace ref, int downloadType){
        this.url=url;
        listRaceRef = ref;
        if(downloadType!=JSON && downloadType!=IMG){
            throw new IllegalArgumentException("invalid download type code!");
        }
        this.downloadType=downloadType;
        this.dbAdapter = new DbAdapter(listRaceRef);
        this.downloaderIstance=new Downloader(dbAdapter);
    }
//    public DownloaderTask(ImageView imageViewReference) {
//        this.imageViewReference = imageViewReference;
//    }

    @Override
    protected RETURN doInBackground(Void... voids) {
        //download data from url, will be cached(serialized) in db cache table...

        //test if there's connection

        if (downloadType == IMG) {
            return (RETURN) downloaderIstance.downloadBitmap(url);
        }
        else {
            return (RETURN) downloaderIstance.downloadJson(url);
            }

        }


    /*@Override
    protected void onPostExecute(Bitmap bitmap) {
        //todo this set img by reference of imageview
        set imageview content from downloaded bitmap
         *//*
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference; // .get()
            if (imageView != null) {
                if (bitmap != null) {
                    *//*
                    memoryCache.put("1", bitmap);
                    brandCatogiriesItem.setUrl(url);
                    brandCatogiriesItem.setThumb(bitmap);
                    // BrandCatogiriesItem.saveLocalBrandOrCatogiries(context, brandCatogiriesItem);
                    *//*
                    imageView.setImageBitmap(bitmap);
                }
                *//* TODO ELSE SET IMAGE NOT AVAIBLE
                else {

                    Drawable placeholder = imageView.getContext().getResources().getDrawable();
                    imageView.setImageDrawable(placeholder);
                } *//*
            }

        }
    }

*/


    @Override
    protected void onPostExecute(RETURN result) {
        /*PASS TO UI by setter methods data downloaded
            TODO CHECK ASYNC !
            casting data by type gived in constructor
            NB if wasn't possible download will be returned null

         */
        super.onPostExecute(result);
        //passing downloaded data to Listagare actity by setMethods
        //them will be cached as static values

        if(downloadType==IMG)
            this.listRaceRef.addImageInChache(url, (Bitmap) result);
        else
            this.listRaceRef.setJsonRaces((String) result);
    }


}