package com.example.muestreo_fauna_salvaje.ViewModels;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<String> lugar = new MutableLiveData<String>();
    private MutableLiveData<String> pais = new MutableLiveData<String>();
    private MutableLiveData<Double> latitude = new MutableLiveData<Double>();
    private MutableLiveData<Double> longitude = new MutableLiveData<Double>();
    private final static int LOCATION_REQUEST_CODE = 20;

    public String getLugar() {
        return lugar.getValue();
    }

    public String getPais() {
        return pais.getValue();
    }

    public double getLatitude() {
        return latitude.getValue();
    }

    public double getLongitude() {
        return longitude.getValue();
    }

    public void inicializar(final Context context, Activity activity){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_DENIED  ||
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

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(5000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationCallback locationCallback = new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if(locationResult != null){
                            List<Location> locations = locationResult.getLocations();
                            for(Location location: locations )
                            {
                                if(location != null){
                                    latitude.setValue(location.getLatitude());
                                    longitude.setValue(location.getLongitude());
                                    try{
                                        Geocoder geocoder = new Geocoder(context);
                                        List<Address> direcciones = geocoder.getFromLocation(latitude.getValue(), longitude.getValue(), 3);
                                        for(Address direccion: direcciones){
                                            lugar.setValue(direccion.getAdminArea());
                                            pais.setValue(direccion.getCountryName());
                                        }

                                    }catch (IOException e){
                                        lugar.setValue("");
                                        pais.setValue("");
                                    }
                                }
                            }
                        }
                    }
                };

                FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(context);
                locationClient.requestLocationUpdates(locationRequest,locationCallback, null);
            }



        }

    }

}
