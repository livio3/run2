package com.example.livio3.run2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by BBOSS on 07/07/2018.
 *
 * try download an image in internet
 * always on fail will'be returned a null and not a bitmap
 * set imageview by reference passed in constructor
 */
public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageViewReference;

    public ImageDownloaderTask(ImageView imageViewReference) {
        this.imageViewReference = imageViewReference;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        /*
        set imageview content from downloaded bitmap
         */
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference; // .get()
            if (imageView != null) {
                if (bitmap != null) {
                    /*
                    memoryCache.put("1", bitmap);
                    brandCatogiriesItem.setUrl(url);
                    brandCatogiriesItem.setThumb(bitmap);
                    // BrandCatogiriesItem.saveLocalBrandOrCatogiries(context, brandCatogiriesItem);
                    */
                    imageView.setImageBitmap(bitmap);
                }
                /* TODO ELSE SET IMAGE NOT AVAIBLE
                else {

                    Drawable placeholder = imageView.getContext().getResources().getDrawable();
                    imageView.setImageDrawable(placeholder);
                } */
            }

        }
    }

    private Bitmap downloadBitmap(String url) {
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
            urlConnection.setRequestMethod("HEAD");
            int imagesize=urlConnection.getContentLength();
            System.out.println("imagesize + "+imagesize);

            inputStream = urlConnection.getInputStream();


            if ( imagesize<ListaGare.MAXIMAGEBYTES && inputStream != null) {

                //bitmap = BitmapFactory.decodeStream(inputStream,null,null); //TODO NO WORK WITH GIF
                byte[] rawBytesImage= readWrap(inputStream,imagesize);
                bitmap = BitmapFactory.decodeByteArray(rawBytesImage,0,imagesize);
            }
        } catch (Exception e) {
            //TODO SIMPLER LOG ALTERNATIVE?
            Log.d("URLCONNECTIONERROR", e.toString());
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
            return bitmap;

            }
        }
        return bitmap;

    }

    protected byte[] readWrap(InputStream is,int size) throws IOException {
        byte[] imageByte= new byte[size];
        int i=0;
        int readed=0;
        while(is.available()>0){ //try download not more MAXIMAGEBYTES
            i=is.read(imageByte,readed,ListaGare.MAXIMAGEBYTES-readed);
            if(i==-1){
                System.err.println("error in read...");
                return null;                                //fail read => null image return
            }
            readed+=i;
            }
        return imageByte;
    }
    public static Bitmap tryaa(){
        //https://atleticauispabruzzo.altervista.org/wp-content/uploads/2017/08/borrellocop-260x146.jpg //borrello
        String uri="https://event.howei.com/sites/default/files/events/2017/SkyhawkNatureRun2018/Icon%20SNR2018.jpg"; //"https://www.freepnglogos.com/uploads/playstation-png-logo/file-playstation-1-logo-4.png";
        ImageView imageView =null;
        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(imageView);
        imageDownloaderTask.execute(uri);
        Bitmap bitmap=null;
        try {
            bitmap = imageDownloaderTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.print(bitmap.toString());
        return bitmap;
    }
}