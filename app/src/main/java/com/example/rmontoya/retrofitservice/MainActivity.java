package com.example.rmontoya.retrofitservice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rmontoya.retrofitservice.adapter.VenueAdapter;
import com.example.rmontoya.retrofitservice.model.FourSquareVenue;
import com.example.rmontoya.retrofitservice.model.FourSquareVenuesBody;
import com.example.rmontoya.retrofitservice.retrofit.FourSquareService;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<FourSquareVenuesBody> {
    //To get client_id and client secret for fourSquare please visit: https://developer.foursquare.com/
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String LOCATION;
    private String VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getServiceStringConstants();
        buildRetrofitService()
                .getFourSquareVenues(VERSION, LOCATION,
                        CLIENT_ID, CLIENT_SECRET).enqueue(this);
    }

    private void getServiceStringConstants() {
        LOCATION = getString(R.string.NEAR_SOFT_LAT_LNG);
        VERSION = getString(R.string.FOUR_SQUARE_VERSION);
        CLIENT_ID = getString(R.string.CLIENT_ID);
        CLIENT_SECRET = getString(R.string.CLIENT_SECRET);
    }

    private FourSquareService buildRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(buildLoggingInterceptor())
                .baseUrl("https://api.foursquare.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(FourSquareService.class);
    }

    @NonNull
    private OkHttpClient buildLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    private void setRecyclerView(List<FourSquareVenue> venueList) {
        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        VenueAdapter adapter = new VenueAdapter(venueList);
        recyclerList.setAdapter(adapter);
    }

    @Override
    public void onResponse(Call<FourSquareVenuesBody> call, Response<FourSquareVenuesBody> response) {
        setRecyclerView(response.body().getResponse().getVenues());
    }

    @Override
    public void onFailure(Call<FourSquareVenuesBody> call, Throwable t) {
        t.getCause();
    }

}