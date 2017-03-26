package com.example.rmontoya.retrofitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FourSquareVenue {

    @SerializedName("name")
    @Expose
    private String name;

    public FourSquareVenue(String venueName) {
        this.name = venueName;
    }

    public String getName() {
        return name;
    }

}