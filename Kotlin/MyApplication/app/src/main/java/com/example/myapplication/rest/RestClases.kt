package com.example.myapplication.rest

import com.google.gson.annotations.SerializedName


data class GeoNameResponse(var geonames: List<GeoName>)


data class GeoName (var name: String)
