package com.example.livio3.run2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by BBOSS
 */

public class JsonHandler {
    protected static final String tryCostant="[{\"dateRace\":\"2018-7-8-16:0\",\"description\":\"maratona\",\"distance\":44.0,\"formattingDate\":\"yyyy-MM-dd\",\"id_race\":0,\"locality\":\"Terminillo (RI)\",\"n_max_runner\":150,\"name\":\"K42 Italia \",\"note\":\"KSprint\\n\",\"prenExpire\":\"2018-7-5-24:0\",\"urlImage\":\"http://www.k42italia.org/images/logos/k21_TerLeo_logo.gif\",\"urlRace\":\"http://www.k42italia.org/\"},{\"dateRace\":\"2018-7-8-10:0\",\"description\":\"maratona\",\"distance\":44.0,\"formattingDate\":\"yyyy-MM-dd\",\"id_race\":1,\"locality\":\"S.Martino D\\u0027Ocre \",\"n_max_runner\":150,\"name\":\" S.Martino Walk \\u0026 Cross Country \",\"note\":\"seconda edizione\\n\",\"prenExpire\":\"2018-7-4-24:0\",\"urlImage\":\"https://farm2.staticflickr.com/1735/29046389218_878ff22d69_z.jpg\",\"urlRace\":\"http://www.polisportivasanmartino.it/\"},{\"dateRace\":\"2018-7-21-5:30\",\"description\":\"corri all\\u0027alba\",\"distance\":14.0,\"formattingDate\":\"yyyy-MM-dd\",\"id_race\":2,\"locality\":\"Latina\",\"n_max_runner\":100,\"name\":\"Alba Run\",\"note\":\"  \",\"prenExpire\":\"2018-7-15-24:0\",\"urlImage\":\" \",\"urlRace\":\"\"},{\"dateRace\":\"2018-7-29-18:0\",\"description\":\"trail\",\"distance\":11.0,\"formattingDate\":\"yyyy-MM-dd\",\"id_race\":3,\"locality\":\"Velletri (RM)\",\"n_max_runner\":50,\"name\":\"trofeo madonna del colle\",\"note\":\"28^esima edizione\\n\",\"prenExpire\":\"2018-7-28-24:0\",\"urlImage\":\"http://www.uisp.it/latina2/newsImg/news348_big.jpg\",\"urlRace\":\"http://www.uisp.it/latina2/\"}]";
    private String jsonStr;
    private List<Race> races;
    private Race race;
    public JsonHandler(String json){ //4 deserilize basic
        this.jsonStr=json;
    }

    //deserialization code...
    public List<Race> getRaces(){
        //iterate among element in json array,delegate parsing to next method..
        JSONArray jsonArray=null;
        List<Race> racesOut= new ArrayList<>();
        try{
            jsonArray= new JSONArray(this.jsonStr);
            for( int w=0;w<jsonArray.length();w++){
                JSONObject jsonObject=jsonArray.getJSONObject(w);
                racesOut.add(getRaceFromJsonObj(jsonObject));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return racesOut;
    }

    public Race getRaceFromJsonObj(JSONObject jsonObject){
        /*
        generate Race Entity istance from json obj
        achived by getting all fields as raw object
        and next Entity Race generated by matching keys(of json) with entity fields
            (TODO upgrade Race infos=> change (only) Race mapping code @Race.setAttributeFromMapping)
         */
        Race race=null;
        try {

        Iterator<String> iterator= jsonObject.keys();
        List<String> keys=new ArrayList<>(); // todo move in constructor /deserilize one
        List<Object> values= new ArrayList<>();
        while (iterator.hasNext())
            keys.add(iterator.next());

        for(int j=0;j<keys.size();j++){
            values.add(jsonObject.get(keys.get(j)));
            }

            //NB at this point there's same corrispondence key->value of json in keys,values
            //todo check
            race= new Race(keys,values);
        }

    catch (Exception e){
        e.printStackTrace();}

        return race;
    }

    //serialization code
    public JsonHandler(List<Race> races ) {
        this.races = races;

    }
    public String exportJson() throws JSONException, NoSuchFieldException { //serialization (debug) return jsonStr for array gived

        //Gson only
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL).create();
        String out=gson.toJson(races);
        return out;

    }

    public static void try1() throws JSONException {

        //SERIALIZATION SAMPLE
        List<Race> racess= new ArrayList<>();
        Race race=new Race(0,"K42 Italia ","maratona","Terminillo (RI)",
                new DateRace("2018-07-08-16:00"),new DateRace("2018-07-05-24:00"),
                "http://www.k42italia.org/","http://www.k42italia.org/images/logos/k21_TerLeo_logo.gif",
                "KSprint\n" ,150,44.00);
        racess.add(race);
        racess.add(new Race(1," S.Martino Walk & Cross Country ","maratona","S.Martino D'Ocre ",
                new DateRace("2018-07-08-10:00"),new DateRace("2018-07-04-24:00"),
                "http://www.polisportivasanmartino.it/","https://farm2.staticflickr.com/1735/29046389218_878ff22d69_z.jpg",
                "seconda edizione\n" ,150,44.00));
        racess.add(new Race(2,"Alba Run","corri all'alba","Latina",
                new DateRace("2018-07-21-05:30"),new DateRace("2018-07-15-24:00"),
                ""," ",
                "  " ,100,14.00));
        racess.add(new Race(3,"trofeo madonna del colle","trail","Velletri (RM)",
                new DateRace("2018-07-29-18:00"),new DateRace("2018-07-28-24:00"),
                "http://www.uisp.it/latina2/","http://www.uisp.it/latina2/newsImg/news348_big.jpg",
                "28^esima edizione\n" , 50,11.00));

        for (int x=0;x<racess.size();x++){
            racess.get(x).fillExport();
        }
        JsonHandler jsonHandler= new JsonHandler(racess);
        String out= null;
        try {
            out = jsonHandler.exportJson();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.print(out);

        //DESERIALIZATION

        JsonHandler handler=new JsonHandler(JsonHandler.tryCostant);
        List<Race> r= handler.getRaces();
        System.out.print("fuck");
    }
}
