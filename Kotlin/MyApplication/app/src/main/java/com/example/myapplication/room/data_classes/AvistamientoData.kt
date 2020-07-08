package com.example.myapplication.room.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class AvistamientoData(@PrimaryKey(autoGenerate = true) var uid:Int = 0,
                            @ColumnInfo(name = "specie") val especie: String,
                            @ColumnInfo(name = "longitude") val longitude: Double,
                            @ColumnInfo(name = "latitude")val latitude: Double,
                            @ColumnInfo(name = "country")val pais: String,
                            @ColumnInfo(name = "place")val  lugar: String,
                            @ColumnInfo(name = "date")val date: String,
                            @ColumnInfo(name = "time") val time: String)
