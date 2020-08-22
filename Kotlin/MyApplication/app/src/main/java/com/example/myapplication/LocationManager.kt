package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import io.reactivex.Observable

@SuppressLint("MissingPermission")
object LocationManager {
    var isThereArduino = false;
     var locationObservable: Observable<Location>? = null;



    fun getLocationObservable(context: Context): Observable<Location>{
        if(locationObservable!=null)

            return locationObservable!!;
        else {
            locationObservable = Observable.create { emitter ->
                val locationRequest = LocationRequest.create();
                locationRequest.interval = 10000;
                locationRequest.fastestInterval = 5000;
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        if (locationResult != null) {
                            for (location: Location in locationResult.locations) {
                                if (location != null) {
                                    emitter.onNext(location);
                                }
                            }
                        }
                    }
                }

                val locationClient = LocationServices.getFusedLocationProviderClient(context);
                locationClient.requestLocationUpdates(locationRequest,locationCallback, null);


            };
            return locationObservable!!;
        }

    }





}