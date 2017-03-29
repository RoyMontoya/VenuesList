package com.example.rmontoya.retrofitservice;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_LOCATION_ENABLED = 0;
    private final String LAT_LNG_SEPARATOR = ", ";
    //To get client_id and client secret for fourSquare please visit: https://developer.foursquare.com/
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String LOCATION;
    private String VERSION;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getServiceStringConstants();
        getLocationService();
    }

    private void getLocationService() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getCurrentLocation();
        } else {
            showLocationEnabledRequiredDialog();
        }
    }

    private void showLocationEnabledRequiredDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.location_dialog_title)
                .setMessage(R.string.location_dialog_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showLocationSettings();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showErrorToast(getString(R.string.location_error_text));
                    }
                });
        builder.create()
                .show();
    }

    private void showLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_LOCATION_ENABLED);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            requestVenuesFromLocation(formatLatLngForRequest(userLocation));
        } else {
            requestLocationPermissionToUser();
        }
    }

    private void requestVenuesFromLocation(String location) {
        buildRetrofitService()
                .getFourSquareVenues(VERSION, location,
                        CLIENT_ID, CLIENT_SECRET).enqueue(this);
    }

    private String formatLatLngForRequest(Location location) {
        String latLng = LOCATION;
        if (location != null)
            latLng = location.getLatitude() + LAT_LNG_SEPARATOR + location.getLongitude();
        return latLng;
    }

    private void requestLocationPermissionToUser() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
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

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            showErrorToast(getString(R.string.permission_error_text));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION_ENABLED
                && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getCurrentLocation();
        } else {
            showErrorToast(getString(R.string.location_error_text));
        }
    }

    @Override
    public void onResponse(Call<FourSquareVenuesBody> call, Response<FourSquareVenuesBody> response) {
        setRecyclerView(response.body().getResponse().getVenues());
    }

    @Override
    public void onFailure(Call<FourSquareVenuesBody> call, Throwable t) {
        t.printStackTrace();
    }

}