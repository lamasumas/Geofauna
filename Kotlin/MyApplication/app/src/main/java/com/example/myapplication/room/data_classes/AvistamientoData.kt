package com.example.myapplication.room.data_classes

import androidx.room.*

@Entity(tableName = "animals")
data class AnimalSimpleData(@PrimaryKey(autoGenerate = true)var simpleId:Long = 0,
                            @ColumnInfo(name = "specie") val especie: String,
                            @ColumnInfo(name = "longitude") val longitude: Double,
                            @ColumnInfo(name = "latitude")val latitude: Double,
                            @ColumnInfo(name = "date")val date: String,
                            @ColumnInfo(name = "time") val time: String)

@Entity(tableName = "advance_data")
data class AnimalAdvanceData(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "advance_id") var uid:Long = 0,
                             var simpleId: Long = 0,
                             @ColumnInfo(name = "country")val pais: String?,
                             @ColumnInfo(name = "place")val  lugar: String?,
                             @ColumnInfo(name = "humidity") val humidity: Double?,
                             @ColumnInfo(name = "temperature") val temperature:Double?,
                             @ColumnInfo(name = "altitude") val altitude:Double?,
                             @ColumnInfo(name = "uv_index") val index_uv: Int?,
                             @ColumnInfo(name = "pressure") val pressure: Double?)


data class SimpleAdvanceRelation(
        @Embedded val simpleData:AnimalSimpleData,
        @Relation(parentColumn = "simpleId",
                entityColumn = "simpleId", entity = AnimalAdvanceData::class
        )
        val advanceData: AnimalAdvanceData
)