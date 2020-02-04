package com.pawardushyant.foodfinderapp.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Geometry implements Parcelable {

    private LocationCoordinates locationCoordinates;

    public Geometry(){}

    protected Geometry(Parcel in) {
        locationCoordinates = in.readParcelable(LocationCoordinates.class.getClassLoader());
    }

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };

    public LocationCoordinates getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(LocationCoordinates locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(locationCoordinates, i);
    }
}
