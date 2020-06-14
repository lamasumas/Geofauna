package com.example.muestreo_fauna_salvaje.ViewModels;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<String> lugar = new MutableLiveData<String>();
    private MutableLiveData<String> pais = new MutableLiveData<String>();
    private MutableLiveData<Double> latitude = new MutableLiveData<Double>();
    private MutableLiveData<Double> longitude = new MutableLiveData<Double>();
    private final static int LOCATION_REQUEST_CODE = 20;

    public void inicializar(Context context, Activity activity){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_DENIED  ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED )
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.INTERNET}, LOCATION_REQUEST_CODE);
        else{

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean encendido = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!encendido)
            {
                Intent intentEncender = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intentEncender);
                return;
            }
            else{
                FusedLocationProviderClient x = LocationServices.getFusedLocationProviderClient(context);
            }


        }

    }

}
