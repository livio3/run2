package com.example.livio3.run2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BBOSS on 05/07/2018.
 */

public class RaceAdapter extends ArrayAdapter<Race> {
    private final LayoutInflater mInflater;
    private Context context;
    private int resource;
    private List<Race> races;
    public RaceAdapter(@NonNull Context context, int resource, @NonNull List<Race> races) {
        super(context, resource, races);
        this.context = context;
        this.races = races;
        this.resource = resource;
        mInflater = (LayoutInflater)context
                .getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            convertView = mInflater.inflate(resource, null);
        }
        Race raceInSet=races.get(position);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDate = convertView.findViewById(R.id.tvData);
        TextView tvLocality = convertView.findViewById(R.id.tvLocality);
        TextView tvDistance = convertView.findViewById(R.id.tvDistance);
        tvName.setText(raceInSet.getName());
        tvDate.setText(raceInSet.getDateRace().toString());
        tvLocality.setText(raceInSet.getLocality());
        tvDistance.setText(String.valueOf( races.get(position).getDistance()));
        //setting image downloaded to itemrace Imgview :)
        if(ListaGare.toDownload==ListaGare.downloaded){
            ImageView imageView=convertView.findViewById(R.id.imageView);
            Bitmap imageDownloaded=ListaGare.imgBuffer.get(raceInSet.getUrlImage());
            if(imageDownloaded!=null)
                imageView.setImageBitmap(imageDownloaded);
            else
                imageView.setImageDrawable(context.getDrawable(R.drawable.corsa));
                //todo set default :image not avaible WITH @STRINGS IMGNOTAVAIBLE!


        }
        return convertView;
    }
    private Bitmap resizeBitmapForListView(Bitmap sourceBtmp){
        Bitmap outputBtmp= sourceBtmp.copy(null,true);  //TODO CHECK DOCS
        int width=sourceBtmp.getWidth();
        int height=sourceBtmp.getWidth();
//        if(width>ListaGare.MAXWIDTH)
//            outputBtmp.setWidth(ListaGare.MAXWIDTH);
//
//        if(height>ListaGare.MAXHEGHT)
//            outputBtmp.setHeight(ListaGare.MAXHEGHT);
        //TODO TEST!
        return outputBtmp;
    }
}

