package com.example.livio3.run2;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by livio3 on 07/07/18.
 */

class DateRace implements Serializable{
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

    @Override
    public String toString() {
        //date format:yyyy-MM-gg-hh:mm
        return year+"-"+month+"-"+day+"-"+hour+":"+min;
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
}
