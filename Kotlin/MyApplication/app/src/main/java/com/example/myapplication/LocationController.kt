package com.example.myapplication;

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception


@SuppressLint("MissingPermission")
class LocationController :Controller {

    var locationObservable: Observable<Location>
    val LATITUDE_ID = 0
    val LONGITUDE_ID = 1
    val COUNTRY_ID = 2
    val PLACE_ID = 3

    private val geocoder:Geocoder
    private val disposables = CompositeDisposable()
    data class  MylocationObject(var longitude:Double, var latitude:Double, var country: String, var place:String)


    constructor(context: Context){

        geocoder = Geocoder(context)

        myData[LATITUDE_ID] = MutableLiveData("")
        myData[LONGITUDE_ID] = MutableLiveData("")
        myData[COUNTRY_ID] = MutableLiveData("")
        myData[PLACE_ID] = MutableLiveData("")


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
                            emitter.onNext(location)
                        }
                    }
                }
            }
        }
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient.requestLocationUpdates(locationRequest,locationCallback, null)
    }
    }

    fun startGettingInfinitePositions(){
        disposables.add(
                locationObservable.observeOn(Schedulers.io())
                        .flatMap {Observable.just(translateGPS2Place(it.longitude, it.latitude))}
                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                            myData[LATITUDE_ID]?.value =  it.latitude.toString()
                            myData[LONGITUDE_ID]?.value = it.longitude.toString()
                            myData[PLACE_ID]?.value = it.place
                            myData[COUNTRY_ID]?.value = it.country }
        )

    }

    fun translateGPS2Place(lon:Double, lat: Double):MylocationObject{
        val mylocationObject = MylocationObject(lon, lat, "", "")
        try {
        geocoder.getFromLocation(lon, lat,3).forEach { address ->
            mylocationObject.country = address.countryName;
            mylocationObject.place = address.adminArea;
        }}catch (e:Exception){
            Log.e("Geocoder", "Geocoder didn't found anything");
        }
        return mylocationObject
    }

    fun getOneGPSPosition(){
        disposables.add(locationObservable.singleOrError().observeOn(Schedulers.io())
                .flatMap { Single.just(translateGPS2Place(it.longitude, it.latitude))}
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    singleLocation ->
                    myData[LATITUDE_ID]?.value =  singleLocation.latitude.toString()
                    myData[LONGITUDE_ID]?.value = singleLocation.longitude.toString()
                    myData[PLACE_ID]?.value = singleLocation.place
                    myData[COUNTRY_ID]?.value = singleLocation.country })
    }

    fun stopGettingPositions(){
        disposables.dispose()
    }

}
