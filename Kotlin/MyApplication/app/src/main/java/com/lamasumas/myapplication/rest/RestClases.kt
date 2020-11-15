package com.lamasumas.myapplication.rest


data class GeoNameResponse(var geonames: List<GeoName>)


data class GeoName (var name: String)
