package com.example.livio3.run2;

import android.os.Binder;

import com.example.livio3.run2.Race;

/**
 * Created by livio3 on 25/07/18.
 */

public class MyBinder extends Binder {

    private Race myRace;

    public MyBinder(Race myRace) {
        this.myRace = myRace;
    }

    public Race getObject() {
        return this.myRace;
    }
}
