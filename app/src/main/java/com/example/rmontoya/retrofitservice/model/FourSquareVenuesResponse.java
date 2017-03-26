package com.example.rmontoya.retrofitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FourSquareVenuesResponse {

    @SerializedName("venues")
    @Expose
    List<FourSquareVenue> venues;

    public List<FourSquareVenue> getVenues() {
        return venues;
    }
}