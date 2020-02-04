package com.pawardushyant.foodfinderapp.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationCoordinates implements Parcelable {

    private String lng;

    private String lat;

    public LocationCoordinates(String lng, String lat){
        this.lng = lng;
        this.lat = lat;
    }

    private LocationCoordinates(Parcel in) {
        lng = in.readString();
        lat = in.readString();
    }

    public static final Creator<LocationCoordinates> CREATOR = new Creator<LocationCoordinates>() {
        @Override
        public LocationCoordinates createFromParcel(Parcel in) {
            return new LocationCoordinates(in);
        }

        @Override
        public LocationCoordinates[] newArray(int size) {
            return new LocationCoordinates[size];
        }
    };

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lng);
        parcel.writeString(lat);
    }
}
