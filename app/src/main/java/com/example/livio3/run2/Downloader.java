package com.example.livio3.run2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {

    /*
        handle downloading from internet images & json
        general rule: downloadproblem=> null return
         */
    private int BUFSIZE=1024 * 5;                       //buffer size for bUFFERD IS
    //public static String gareUrlJson="https://drive.google.com/open?id=1YcYMujtYkEea12-X0TX3O42TUahF4DiR";
    //public static String gareUrlJson="https://raw.githubusercontent.com/andysnake96/PRJBD2/master/src/DAO/Connection.java";
    //TODO UPDATE WITH JSON REPO :)

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
            bitmap = BitmapFactory.decodeByteArray(rawBytesImage,0,rawBytesImage.length);//TODO TEST
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