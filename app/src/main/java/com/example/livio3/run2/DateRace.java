package com.example.livio3.run2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by livio3 on 07/07/18.
 */

class DateRace {
    //basic handler date for compatibility old android version...
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private final String separator="-";
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
        //todo parsing

    }
}
