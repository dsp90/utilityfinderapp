package com.pawardushyant.foodfinderapp.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SearchResultsResponse implements Parcelable {

    private List<ResultHits> results;

    private String status;

    public SearchResultsResponse(){}

    private SearchResultsResponse(Parcel in) {
        results = in.createTypedArrayList(ResultHits.CREATOR);
    }

    public static final Creator<SearchResultsResponse> CREATOR = new Creator<SearchResultsResponse>() {
        @Override
        public SearchResultsResponse createFromParcel(Parcel in) {
            return new SearchResultsResponse(in);
        }

        @Override
        public SearchResultsResponse[] newArray(int size) {
            return new SearchResultsResponse[size];
        }
    };

    public List<ResultHits> getResults() {
        return results;
    }

    public void setResults(List<ResultHits> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(results);
        parcel.writeString(status);
    }
}
