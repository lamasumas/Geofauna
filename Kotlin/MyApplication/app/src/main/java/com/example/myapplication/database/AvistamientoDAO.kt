package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data_classes.AvistamientoData
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface AvistamientoDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvistamiento(vararg avistamiento: AvistamientoData): Completable

    @Query("SELECT * FROM AvistamientoData")
    fun getAlllAvistamiento(): Single<List<AvistamientoData>>

}