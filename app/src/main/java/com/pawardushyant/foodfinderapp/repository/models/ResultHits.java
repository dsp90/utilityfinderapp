package com.pawardushyant.foodfinderapp.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchresults")
public class ResultHits implements Parcelable {

    public ResultHits(){}

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "geometry")
    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "rating")
    private String rating;

    @ColumnInfo(name = "types")
    private List<String> types;

    @ColumnInfo(name = "vicinity")
    private String vicinity;

    @ColumnInfo(name = "photos")
    private List<PlacePhoto>  photos;

    @ColumnInfo(name = "imageurl")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<PlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PlacePhoto> photos) {
        this.photos = photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.geometry, flags);
        dest.writeString(this.name);
        dest.writeString(this.rating);
        dest.writeStringList(this.types);
        dest.writeString(this.vicinity);
        dest.writeTypedList(this.photos);
        dest.writeString(this.imageUrl);
    }

    protected ResultHits(Parcel in) {
        this.id = in.readString();
        this.geometry = in.readParcelable(Geometry.class.getClassLoader());
        this.name = in.readString();
        this.rating = in.readString();
        this.types = in.createStringArrayList();
        this.vicinity = in.readString();
        this.photos = in.createTypedArrayList(PlacePhoto.CREATOR);
        this.imageUrl = in.readString();
    }

    public static final Creator<ResultHits> CREATOR = new Creator<ResultHits>() {
        @Override
        public ResultHits createFromParcel(Parcel source) {
            return new ResultHits(source);
        }

        @Override
        public ResultHits[] newArray(int size) {
            return new ResultHits[size];
        }
    };
}
