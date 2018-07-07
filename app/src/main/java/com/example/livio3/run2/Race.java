package com.example.livio3.run2;

import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by BBOSS on 05/07/2018.
 */

public class Race {
    private int id_race;

    private final String formattingDate="yyyy-MM-dd";
    private String name;
    private String description;
    private String locality;
    private DateRace dateRace;
    private DateRace prenExpire;     //TODO COMPATIBILITY API>26 :(
    private String dateRaceExport;   //TODO ONLY FOR CREATE JSON REMOVE
    private String prenExpireExport;
    private String urlRace;
    private String urlImage;
    private String note;
    private int n_max_runner;
    private Double distance;

    public Race(List<String> keys,List<Object> values){
        //constructor to generate Race obj from List keyValue from json parsing
        //Lists has to match order of meaning of valus
        assert (keys.size()==values.size());
        List<Race> races= new ArrayList<>();
        for(int i=0;i<keys.size();i++){
            String key=keys.get(i);
            Object value=values.get(i);
            try {
                setAttributeFromMapping(key,value);             //set value in this obj
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public void setAttributeFromMapping(String key, Object value) throws ParseException {
        switch (key){
            case "name":
                this.name=(String) value;
                break;
            case "description":
                this.description=(String) value;
                break;
            case "locality":
                this.locality=(String) value;
                break;
            case "dateRace":

                this.dateRace=new DateRace(value.toString());
                break;
            case "prenExpire":
                this.prenExpire=new DateRace(value.toString());
                break;
            case "urlRace":
                this.urlRace=(String) value;
                break;
            case "urlImage":
                this.urlImage=(String) value;
                break;
            case "note":
                this.note=(String) value;
                break;
            case "id_race":
                this.id_race= (int) value;
                break;
            case "n_max_runner":
                this.n_max_runner= (int) value;
                break;
            case "distance":
                this.distance= (Double) value;
                break;



        }
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId_race() {
        return id_race;
    }

    public void setId_race(int id_race) {
        this.id_race = id_race;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public DateRace getDateRace() {
        return dateRace;
    }

    public String getDateRaceExport() {
        return dateRaceExport;
    }

    public String getPrenExpireExport() {
        return prenExpireExport;
    }

    public void setDateRace(DateRace dateRace) {
        this.dateRace = dateRace;
    }

    public DateRace getPrenExpire() {
        return prenExpire;
    }

    public void setPrenExpire(DateRace prenExpire) {
        this.prenExpire = prenExpire;
    }

    public String getUrlRace() {
        return urlRace;
    }

    public void setUrlRace(String urlRace) {
        this.urlRace = urlRace;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getN_max_runner() {
        return n_max_runner;
    }

    public void setN_max_runner(int n_max_runner) {
        this.n_max_runner = n_max_runner;
    }

    public Race() {
        Random random = new Random();
        urlRace=String.valueOf(random.nextInt());
        urlImage=String.valueOf(random.nextInt());
        n_max_runner=random.nextInt()%100;
        this.name = String.valueOf(random.nextInt());
        this.note = String.valueOf(random.nextInt());
        this.description = String.valueOf(random.nextInt());
        name = String.valueOf(random.nextInt());
        locality = "ROMA!";
        dateRaceExport =   new SimpleDateFormat("yy-MM-dd").format(new Date());
        prenExpireExport = new SimpleDateFormat("yy-MM-dd").format(new Date());

        distance= random.nextDouble();

    }
}
