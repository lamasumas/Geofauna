package com.lamasumas.myapplication.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoNameInterface {
    //http://api.geonames.org/findNearbyJSON?lat=47.3&lng=9&username=demo

    @GET("/findNearbyJSON")
    fun getNearbyPlaceName(
            @Query("lat") latitude: String,
            @Query("lng") longitude: String,
            @Query("username") username: String
    ): Call<GeoNameResponse>
}