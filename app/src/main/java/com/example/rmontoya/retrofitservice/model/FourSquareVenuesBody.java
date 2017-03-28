package com.example.rmontoya.retrofitservice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FourSquareVenuesBody {

    @SerializedName("response")
    @Expose
    FourSquareVenuesResponse response;

    public FourSquareVenuesResponse getResponse() {
        return response;
    }

}