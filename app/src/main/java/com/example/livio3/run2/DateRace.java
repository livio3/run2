package com.example.livio3.run2;

import android.content.res.Resources;
import android.icu.text.StringSearch;
import android.support.annotation.NonNull;

import com.example.livio3.run2.DB.DbAdapter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by livio3 on 07/07/18.
 */

public class DateRace implements Serializable{
    //basic handler date for compatibility old android version...
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private String stringDate;
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public DateRace(String stringDate) {
        //date format:yyyy-MM-gg-hh:mm
        //fill date parsing string
        String[] strings=stringDate.split("-");
        this.year=Integer.parseInt(strings[0]);
        this.month=Integer.parseInt(strings[1]);
        this.day=Integer.parseInt(strings[2]);
        String[] stringsTime=strings[3].split(":");
        this.hour=Integer.parseInt(stringsTime[0]);
        this.min=Integer.parseInt(stringsTime[1]);
        System.out.println("parsed :"+this.toString());

    }

    public String toStringWellPrinted(){

        return toStringDate()+"\t" //possibly add string at<->alle from strings outside context
                +toStringTime();
    }
    @Override
    public String toString() {
        //date format:yyyy-MM-gg-hh:mm
        return year+"-"+month+"-"+day+"-"+hour+":"+min;



    }
    public String toStringDate(){
        return day+"-"+month +"-"+year;
    }
    public String toStringTime(){
        String h=String.valueOf(hour);
        String m=String.valueOf(min);
        if(hour<10)
            h='0'+h;
        if(min<10)
            m+='0';
        return h+" : "+m;
    }
    protected int wrapRandomPos(int bound){
        Random random=new Random();
        return Math.abs(random.nextInt(bound));
    }

    public DateRace(){
        //random generated date
        this.year=2018+wrapRandomPos(1);
        this.month=wrapRandomPos(12);
        this.day=wrapRandomPos(28);
        this.hour=wrapRandomPos(20);
        this.min=wrapRandomPos(60);
    }

    public static DateRace now() {
        DateRace dateRace = new DateRace();
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        dateRace.setYear(c.get(Calendar.YEAR));
        dateRace.setMonth(c.get(Calendar.MONTH)+1);
        dateRace.setDay(c.get(Calendar.DAY_OF_MONTH));
        dateRace.setHour(c.get(Calendar.HOUR));
        dateRace.setMin(c.get(Calendar.MINUTE));
        System.out.println(dateRace);
        return dateRace;
    }

    /*
    -1 se dateRace_1 viene prima altrimenti 0 se sono uguali 1 se vieno dopo di dateRace_2
     */

    public  static int compareDateRace(DateRace dateRace_1, DateRace dateRace_2) {
        if(dateRace_1.getYear() < dateRace_2.getYear())
            return -1;
        if(dateRace_1.getYear() > dateRace_2.getYear())
            return 1;
        if(dateRace_1.getYear() == dateRace_2.getYear()) {
            if(dateRace_1.getMonth() <  dateRace_2.getMonth())
                return -1;

            if(dateRace_1.getMonth() > dateRace_2.getMonth() )
                return 1;
            if(dateRace_1.getMonth() == dateRace_2.getMonth()) {
                if(dateRace_1.getDay() < dateRace_2.getDay())
                    return -1;
                if(dateRace_1.getDay() > dateRace_2.getDay())
                    return 1;
                if(dateRace_1.getDay() == dateRace_2.getDay()) {
                    if(dateRace_1.getHour() < dateRace_2.getHour())
                        return -1;
                    if(dateRace_1.getHour() > dateRace_2.getHour())
                        return 1;
                    if(dateRace_1.getHour() == dateRace_2.getHour()) {
                        if(dateRace_1.getMin() < dateRace_2.getMin())
                            return -1;
                        if(dateRace_2.getMin() > dateRace_2.getMin())
                            return 1;

                    }
                }
            }

        }
        return 0;
    }


}
