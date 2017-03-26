package com.example.rmontoya.retrofitservice.retrofit;

import com.example.rmontoya.retrofitservice.model.FourSquareVenuesBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FourSquareService {

    @GET("venues/search")
    Call<FourSquareVenuesBody> getFourSquareVenues(
            @Query("v") String version,
            @Query("ll") String location,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret);

}