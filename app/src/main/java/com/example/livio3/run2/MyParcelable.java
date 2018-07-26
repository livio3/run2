package com.example.livio3.run2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by livio3 on 25/07/18.
 */

public class MyParcelable implements Parcelable {

    private Race myRace;

    public MyParcelable() {}

    public MyParcelable(Parcel parcel) {
        myRace = ((MyBinder)parcel.readStrongBinder()).getObject();
    }

    public void setObject(Race myRace) {
        this.myRace = myRace;
    }

    public Race getObject() {
        return myRace;
    }

    public void writeToParcel (Parcel parcel, int flags) {
        parcel.writeStrongBinder(new MyBinder(myRace));
    }

    public int describeContents() {
        return myRace == null ? 0 : 1;
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>()
    {

        public MyParcelable createFromParcel(Parcel parcel) {
            return new MyParcelable(parcel);
        }

        public MyParcelable[] newArray(int lenght) {
            return new MyParcelable[lenght];
        }
    };
}
