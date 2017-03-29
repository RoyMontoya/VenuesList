package com.example.rmontoya.retrofitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FourSquareVenuesResponse {

    @SerializedName("venues")
    @Expose
    private List<FourSquareVenue> venues;

    public FourSquareVenuesResponse(List<FourSquareVenue> venues) {
        this.venues = venues;
    }

    public List<FourSquareVenue> getVenues() {
        return venues;
    }

}