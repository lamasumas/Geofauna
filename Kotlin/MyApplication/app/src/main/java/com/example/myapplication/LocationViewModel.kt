package com.example.myapplication

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationViewModel: ViewModel() {
    var isThereArduino = false;
    var latitude = MutableLiveData<Double>();
    var longitude = MutableLiveData<Double>();


    fun startGettingData(){

        if(!isThereArduino) {
            val locationRequest = LocationRequest.create();
            locationRequest.interval = 10000;
            locationRequest.fastestInterval = 5000;
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (locationResult != null) {
                        for (locaiton: Location in locationResult.locations) {
                            if (locaiton != null) {
                                latitude.value = locaiton.latitude;
                                longitude.value = locaiton.longitude;
                            }
                        }
                    }
                }
            }
        }

    }



}