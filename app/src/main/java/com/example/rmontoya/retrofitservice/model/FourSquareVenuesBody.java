package com.example.rmontoya.retrofitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FourSquareVenuesBody {

    @SerializedName("response")
    @Expose
    private FourSquareVenuesResponse response;

    public FourSquareVenuesBody(FourSquareVenuesResponse response) {
        this.response = response;
    }

    public FourSquareVenuesResponse getResponse() {
        return response;
    }

}