package com.example.myapplication.rest

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestManager {


    fun getGeoNameLocation(lat: String, lon: String): Observable<List<GeoName>?>? {


        return Observable.just(getGeoNameLocationCall(lat, lon)).observeOn(Schedulers.io())
            .map { it.execute().body()?.geonames }.observeOn(AndroidSchedulers.mainThread())
    }

    private fun getGeoNameLocationCall(lat: String, lon: String): Call<GeoNameResponse> {
        val retroFit = Retrofit.Builder().baseUrl("http://api.geonames.org")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val geonameService = retroFit.create(GeoNameInterface::class.java)
        return geonameService.getNearbyPlaceName(lat, lon, "uo257611").clone()
    }


}