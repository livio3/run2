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
    private static final String tryCostant="[{\"dateRace\":\"2018-07-05T10:43:07.402\",\"description\":\"-687347726\",\"distance\":0.27151275,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":-46,\"name\":\"-199837474\",\"note\":\"314909306\",\"prenExpire\":\"2018-06-29T10:43:07.405\",\"urlImage\":\"1815575071\",\"urlRace\":\"-748333301\"},{\"dateRace\":\"2018-06-30T10:43:07.408\",\"description\":\"597471288\",\"distance\":0.38573122,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":35,\"name\":\"-276660612\",\"note\":\"-1504999936\",\"prenExpire\":\"2018-07-05T10:43:07.41\",\"urlImage\":\"693077652\",\"urlRace\":\"1017607733\"},{\"dateRace\":\"2018-07-17T10:43:07.413\",\"description\":\"-322150895\",\"distance\":0.0302248,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":-31,\"name\":\"-1945940308\",\"note\":\"346445414\",\"prenExpire\":\"2018-06-27T10:43:07.415\",\"urlImage\":\"-1184682673\",\"urlRace\":\"1010355754\"},{\"dateRace\":\"2018-06-28T10:43:07.417\",\"description\":\"2062911415\",\"distance\":0.0571813,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":32,\"name\":\"-2137901402\",\"note\":\"899865011\",\"prenExpire\":\"2018-06-27T10:43:07.419\",\"urlImage\":\"-387928869\",\"urlRace\":\"1452418930\"},{\"dateRace\":\"2018-07-07T10:43:07.422\",\"description\":\"-901125564\",\"distance\":0.25884908,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":13,\"name\":\"1326676588\",\"note\":\"1968188962\",\"prenExpire\":\"2018-07-08T10:43:07.424\",\"urlImage\":\"680765794\",\"urlRace\":\"1447337786\"},{\"dateRace\":\"2018-07-09T10:43:07.426\",\"description\":\"-1088345126\",\"distance\":0.7362692,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":87,\"name\":\"-2103682983\",\"note\":\"995866257\",\"prenExpire\":\"2018-06-29T10:43:07.429\",\"urlImage\":\"-1471480116\",\"urlRace\":\"-380535527\"},{\"dateRace\":\"2018-07-05T10:43:07.431\",\"description\":\"-2056855820\",\"distance\":0.9487671,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":-28,\"name\":\"-2086687293\",\"note\":\"-383326735\",\"prenExpire\":\"2018-07-09T10:43:07.433\",\"urlImage\":\"543729199\",\"urlRace\":\"-936690356\"},{\"dateRace\":\"2018-07-07T10:43:07.435\",\"description\":\"-1005402131\",\"distance\":0.5966099,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":-23,\"name\":\"169777741\",\"note\":\"533257577\",\"prenExpire\":\"2018-07-15T10:43:07.437\",\"urlImage\":\"-580558718\",\"urlRace\":\"522314405\"},{\"dateRace\":\"2018-07-06T10:43:07.439\",\"description\":\"930482746\",\"distance\":0.63580704,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":-50,\"name\":\"435818443\",\"note\":\"1060446342\",\"prenExpire\":\"2018-07-07T10:43:07.441\",\"urlImage\":\"840993565\",\"urlRace\":\"1170971819\"},{\"dateRace\":\"2018-06-30T10:43:07.443\",\"description\":\"1390380842\",\"distance\":0.6015908,\"id_race\":0,\"locality\":\"ROMA!\",\"n_max_runner\":-69,\"name\":\"-571215802\",\"note\":\"-1978448311\",\"prenExpire\":\"2018-07-08T10:43:07.445\",\"urlImage\":\"2063112278\",\"urlRace\":\"1185196101\"}]";
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
    public String exportJson() throws JSONException { //serialization (debug) return jsonStr for array gived

        //Gson only
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL).create();
        String out=gson.toJson(races);
        return out;

    }

    public static void try1() throws JSONException {

        //SERIALIZATION SAMPLE
        List<Race> racess= new ArrayList<>();
        for(int i=0;i<10;i++){
            racess.add(new Race());
        }
        JsonHandler jsonHandler= new JsonHandler(racess);
        String out=jsonHandler.exportJson();
        System.out.print(out);

        //DESERIALIZATION
        /*
        JsonHandler  handler=new JsonHandler(JsonHandler.tryCostant);
        List<Race> racess= handler.getRaces();
        System.out.print("fuck"); */
    }
}
