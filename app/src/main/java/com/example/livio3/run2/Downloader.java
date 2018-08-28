package com.example.livio3.run2;

import android.content.Context;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.livio3.run2.DB.DbAdapter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Downloader {

    /*
        handle downloading from internet images & json
        general rule: downloadproblem=> null return
         */
    private int BUFSIZE=1024 * 5;                       //buffer size for bUFFERD IS
    private DbAdapter dbAdapter;                        //to serialize downloaded objs in db(as cache)

    public Downloader(DbAdapter dbAdapter) {

        this.dbAdapter = dbAdapter;                     //supposed to be closed..
    }

    public Bitmap downloadBitmap(String url) {
        //download image and return bitmap that'll be set to imageview in listagare
        Bitmap bitmap=null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode !=HttpURLConnection.HTTP_OK) {   //todo alternative?
                return null;
            }

//            urlConnection.setRequestMethod("HEAD");
//            int imagesize=urlConnection.getContentLength();  //todo most server answer with unknown
//            System.out.println("imagesize + "+imagesize);

            inputStream = urlConnection.getInputStream();

            //bitmap = BitmapFactory.decodeStream(inputStream,null,null); //TODO NO WORK WITH GIF
            byte[] rawBytesImage= readWrap(inputStream);
            // IMG STRING SERIALIZATION
            String rawImgAsStr=Base64.encodeToString(rawBytesImage,Base64.DEFAULT);
//            img bytes (group of 6 bit) encoded as a printable string :=)
//            byte[] deserializedImgStr= Base64.decode(rawImgAsStr,Base64.DEFAULT); //deserialz
            writeInCache(rawImgAsStr,url);          //save in cache data downloaded
            bitmap = BitmapFactory.decodeByteArray(rawBytesImage,0,rawBytesImage.length);//TODO TEST
            if(bitmap==null){
                System.err.println("str simple test has failed...");
            }
        }
        catch (Exception e) {
            System.err.println("download impossible");
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            //always end=> cloasing opened resources
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
                return bitmap;

            }
        }
        return bitmap;

    }

    private boolean writeInCache(String data, String url) {
        /*
        serialize in CACHE TABLE data (as string)
        boolean return for insert result....
         */
        try {
            this.dbAdapter.open();
            long insertRes = dbAdapter.addRawCache(url, data);
        }
        catch (SQLException e){
            e.printStackTrace();
            System.err.println("serializing in cache error :(");
            return false;
        }
        finally {
            dbAdapter.close();
        }
        return true;
    }

    public String downloadJson(String url) {
        //download Json from url
        String result=null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode !=HttpURLConnection.HTTP_OK) {   //todo alternative?
                return null;
            }
//            urlConnection.setRequestMethod("HEAD");       //TODO NOT WORK! server cheap infos..
//            int imagesize=urlConnection.getContentLength();   //TODO IMPORT FIND SIZE
//            System.out.println("imagesize + "+imagesize);

            inputStream = urlConnection.getInputStream();
            BufferedInputStream is=new BufferedInputStream(inputStream,BUFSIZE);

            byte[] rawBytesStr= readWrap(inputStream);
            result=new String(rawBytesStr);
            writeInCache(result,url);                       //save in cache downloaded data

        } catch (Exception e) {
            //TODO SIMPLER LOG ALTERNATIVE?
            //Log.d("URLCONNECTIONERROR", e.toString());
            System.err.println("download impossible");
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
                return result;

            }
        }
        return result;

    }


    private byte[] readWrap(InputStream is ) throws IOException {
        //read raw data from an impustream handling possibles errors :)
        //unkown size data can be readed with  ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        byte[] buffer=new byte[BUFSIZE];
        int i=0;
        int readed=0;

        while((readed=is.read(buffer))!=-1){
            byteArrayOutputStream.write(buffer,0,readed);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();

    }

     public void setBUFSIZE(int BUFSIZE) {
         this.BUFSIZE = BUFSIZE;
    }
}