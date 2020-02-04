package com.pawardushyant.foodfinderapp.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacePhoto implements Parcelable {

    private String photo_reference;

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    protected PlacePhoto(Parcel in) {
        photo_reference = in.readString();
    }

    public static final Creator<PlacePhoto> CREATOR = new Creator<PlacePhoto>() {
        @Override
        public PlacePhoto createFromParcel(Parcel in) {
            return new PlacePhoto(in);
        }

        @Override
        public PlacePhoto[] newArray(int size) {
            return new PlacePhoto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photo_reference);
    }
}
