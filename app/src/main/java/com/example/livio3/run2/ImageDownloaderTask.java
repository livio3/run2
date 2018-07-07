package com.example.livio3.run2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by BBOSS on 07/07/2018.
 */
public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {


    @Override
    protected Bitmap doInBackground(String... params) {

        return downloadBitmap(params[0]);
    }

    /*@Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    memoryCache.put("1", bitmap);
                    brandCatogiriesItem.setUrl(url);
                    brandCatogiriesItem.setThumb(bitmap);
                    // BrandCatogiriesItem.saveLocalBrandOrCatogiries(context, brandCatogiriesItem);
                    imageView.setImageBitmap(bitmap);
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                    imageView.setImageDrawable(placeholder);
                }
            }

        }
    }*/

    private Bitmap downloadBitmap(String url) {
        Bitmap bitmap=null;
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode !=HttpURLConnection.HTTP_OK) {   //todo alternative?
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

                bitmap = BitmapFactory.decodeStream(inputStream,null,null);

            }
        } catch (Exception e) {
            //TODO SIMPLER LOG ALTERNATIVE?
            Log.d("URLCONNECTIONERROR", e.toString());
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
        return bitmap;
    }
    public static Bitmap tryaa(){
        //https://atleticauispabruzzo.altervista.org/wp-content/uploads/2017/08/borrellocop-260x146.jpg //borrello
        String uri="https://event.howei.com/sites/default/files/events/2017/SkyhawkNatureRun2018/Icon%20SNR2018.jpg"; //"https://www.freepnglogos.com/uploads/playstation-png-logo/file-playstation-1-logo-4.png";
        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask();
        imageDownloaderTask.execute(uri);
        Bitmap bitmap= null;
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