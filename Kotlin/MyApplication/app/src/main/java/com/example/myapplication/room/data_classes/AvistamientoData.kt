package com.example.myapplication.room.data_classes

import androidx.room.*

@Entity(tableName = "animals")
data class AnimalSimpleData(@PrimaryKey(autoGenerate = true) var simpleId: Long = 0,
                            var transect_id: Long,
                            @ColumnInfo(name = "specie") val especie: String,
                            @ColumnInfo(name = "longitude") val longitude: Double,
                            @ColumnInfo(name = "latitude") val latitude: Double,
                            @ColumnInfo(name = "date") val date: String,
                            @ColumnInfo(name = "time") val time: String) {
    override fun toString(): String {
        val temp = listOf(especie, longitude.toString(), latitude.toString(), date, time)
        return temp.joinToString(separator = ",")
    }
}

@Entity(tableName = "muestreos")
data class Transect(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "transect_id") var uid: Long = 0,
                    @ColumnInfo(name = "nome") val name: String,
                    @ColumnInfo(name = "country") val country: String,
                    @ColumnInfo(name = "locality") val locality: String,
                    @ColumnInfo(name = "nearestPlace") val nearestPlace: String?,
                    @ColumnInfo(name = "animal_list") val aniamlList: String?,
                    @ColumnInfo(name = "Pressure_Sampling") var pressureSampling: Double?,
                    @ColumnInfo(name = "Altitude_Sampling") var altitudeSampling: Double?
) {
    var isAltitudeSamplingSet = false
    var areSampligDataSet = false
}

@Entity(tableName = "advance_data")
data class AnimalAdvanceData(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "advance_id") var uid: Long = 0,
                             var simpleId: Long = 0,
                             @ColumnInfo(name = "country") val pais: String?,
                             @ColumnInfo(name = "place") val lugar: String?,
                             @ColumnInfo(name = "humidity") val humidity: Double?,
                             @ColumnInfo(name = "temperature") val temperature: Double?,
                             @ColumnInfo(name = "altitude") val altitude: Double?,
                             @ColumnInfo(name = "uv_index") val index_uv: Int?,
                             @ColumnInfo(name = "pressure") val pressure: Double?,
                             @ColumnInfo(name = "notes") val notes: String?,
                             @ColumnInfo(name = "picture") val photoPlace: String?,
                             @ColumnInfo(name = "isAltitudeEstimated") val estimatedWithPressure: Boolean?) {
    override fun toString(): String {
        val temp = listOf(humidity.toString(), temperature.toString(), estimatedWithPressure.toString(), altitude.toString(), pressure.toString(), index_uv.toString(), lugar, pais, notes, photoPlace)
        return temp.joinToString(separator = ",")
    }
}

data class TransectAnimalRelation(
        @Embedded val transect: Transect,
        @Relation(parentColumn = "transect_id",
                entityColumn = "transect_id"
        )
        val simpleData: List<AnimalSimpleData>
)


data class SimpleAdvanceRelation(
        @Embedded val simpleData: AnimalSimpleData,
        @Relation(parentColumn = "simpleId",
                entityColumn = "simpleId", entity = AnimalAdvanceData::class
        )
        val advanceData: AnimalAdvanceData
) {
    override fun toString(): String {
        val temp = listOf(simpleData.simpleId.toString(), advanceData.uid.toString(), simpleData.toString(), advanceData.toString())
        return temp.joinToString(separator = ",", postfix = "\n")
    }
}