package com.example.livio3.run2;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by BBOSS on 05/07/2018.
 *     todo *to export json string call fillExport ..export..>REPLACE IN LONG STRING expire->"" string generated ready to be imported
 */

public class Race implements Serializable {
    /*
    [{"dateRace":{"day":17,"hour":0,"min":6,"month":7,"year":2018},"description":"1609077159","distance":0.024090424179222025,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":43,"name":"1268486985","note":"149563508","prenExpire":{"day":14,"hour":1,"min":25,"month":9,"year":2018},"urlImage":"77387405","urlRace":"-83698696"},{"dateRace":{"day":7,"hour":18,"min":14,"month":10,"year":2018},"description":"1229755864","distance":0.07828670528993098,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":47,"name":"237989295","note":"-36138641","prenExpire":{"day":6,"hour":5,"min":17,"month":6,"year":2018},"urlImage":"-2028357836","urlRace":"2096143497"},{"dateRace":{"day":5,"hour":11,"min":9,"month":1,"year":2018},"description":"1012574097","distance":0.6251512521856593,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":58,"name":"-1341887402","note":"-1901776929","prenExpire":{"day":19,"hour":3,"min":34,"month":5,"year":2018},"urlImage":"-951748012","urlRace":"-902199451"},{"dateRace":{"day":3,"hour":12,"min":33,"month":8,"year":2018},"description":"964661503","distance":0.8419799052411877,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":-20,"name":"-117820629","note":"661868999","prenExpire":{"day":17,"hour":2,"min":49,"month":2,"year":2018},"urlImage":"1199300035","urlRace":"-932433724"},{"dateRace":{"day":2,"hour":10,"min":38,"month":4,"year":2018},"description":"-105560326","distance":0.1610439308537448,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":70,"name":"-1471124354","note":"-2020379759","prenExpire":{"day":25,"hour":8,"min":13,"month":8,"year":2018},"urlImage":"-1414133536","urlRace":"1168851384"},{"dateRace":{"day":10,"hour":8,"min":2,"month":8,"year":2018},"description":"564285602","distance":0.5385247129911805,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":80,"name":"47564970","note":"-697807360","prenExpire":{"day":4,"hour":8,"min":10,"month":2,"year":2018},"urlImage":"117492383","urlRace":"-1940331069"},{"dateRace":{"day":23,"hour":14,"min":39,"month":2,"year":2018},"description":"-1165418175","distance":0.11172659886948855,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":10,"name":"2135585114","note":"-1369731391","prenExpire":{"day":2,"hour":10,"min":4,"month":3,"year":2018},"urlImage":"-804110402","urlRace":"-2063566711"},{"dateRace":{"day":3,"hour":11,"min":21,"month":7,"year":2018},"description":"1649043716","distance":0.4444382065840152,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":-54,"name":"1837240113","note":"-391155687","prenExpire":{"day":2,"hour":8,"min":25,"month":7,"year":2018},"urlImage":"2124755519","urlRace":"-1127484595"},{"dateRace":{"day":6,"hour":4,"min":4,"month":4,"year":2018},"description":"789733655","distance":0.7668922022104521,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":-35,"name":"890574012","note":"-725405957","prenExpire":{"day":21,"hour":12,"min":47,"month":1,"year":2018},"urlImage":"-1627965086","urlRace":"-727848053"},{"dateRace":{"day":27,"hour":16,"min":19,"month":6,"year":2018},"description":"1754446927","distance":0.8353206833203877,"formattingDate":"yyyy-MM-dd","id_race":0,"locality":"ROMA!","n_max_runner":-91,"name":"283014637","note":"534239118","prenExpire":{"day":19,"hour":12,"min":6,"month":0,"year":2018},"urlImage":"-1175636287","urlRace":"-201190576"}]
     */
    private int id_race;

    private final String formattingDate="yyyy-MM-dd";
    private String name;
    private String description;
    private String locality;
    private   DateRace dateRace;
    private   DateRace prenExpire;
    private String dateRaceExport;   //TODO EXTRA FIELD TO EXPORT JSON STRING REMOVE! *
    private String prenExpireExport; //TODO EXTRA FIELDS TO EXPORT JSON STRING REMOVE *
    private String urlRace;
    private String urlImage;
    private String note;
    private int n_max_runner;
    private Double distance;

    public Race(int id_race, String name, String description, String locality, DateRace dateRace,
                DateRace prenExpire, String urlRace, String urlImage, String note,
                int n_max_runner, Double distance) {
        this.id_race = id_race;
        this.name = name;
        this.description = description;
        this.locality = locality;
        this.dateRace = dateRace;
        this.prenExpire = prenExpire;
        this.urlRace = urlRace;
        this.urlImage = urlImage;
        this.note = note;
        this.n_max_runner = n_max_runner;
        this.distance = distance;
    }

    public Race(List<String> keys, List<Object> values){
        //constructor to generate Race obj from List keyValue from json parsing
        //Lists have to match order of related values
        assert (keys.size()==values.size());
        this.urlRace="";

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
        //set an attribute for object (in costruction from json parsing)
        String valueObtained;               //parsed value from json related to key
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

    public void fillExport(){
        //todo debug only fill extra fields to export json string

        this.dateRaceExport=dateRace.toString();
        this.prenExpireExport=prenExpire.toString();
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
        this.dateRace=new DateRace();
        this.prenExpire=new DateRace();
        distance= random.nextDouble();

    }

    @Override
    public String toString() {
        return "Race{" +
                "id_race=" + id_race +
                ", formattingDate='" + formattingDate + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", locality='" + locality + '\'' +
                ", dateRace=" + dateRace +
                ", prenExpire=" + prenExpire +
                ", dateRaceExport='" + dateRaceExport + '\'' +
                ", prenExpireExport='" + prenExpireExport + '\'' +
                ", urlRace='" + urlRace + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", note='" + note + '\'' +
                ", n_max_runner=" + n_max_runner +
                ", distance=" + distance +
                '}';
    }
}
