package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.room.data_classes.AvistamientoData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface AvistamientoDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvistamiento(vararg avistamiento: AvistamientoData): Completable

    @Query("SELECT * FROM animals")
    fun getAlllAvistamiento(): Observable<List<AvistamientoData>>

    @Query("SELECT * FROM animals WHERE uid=:uid")
    fun getAvistamientoById(vararg uid:Int): Observable<AvistamientoData>

    @Update
    fun updateAvistamiento(vararg avistamientoData: AvistamientoData):Completable

    @Delete
    fun deleteAvistamiento(vararg avistamientoData: AvistamientoData):Completable


}