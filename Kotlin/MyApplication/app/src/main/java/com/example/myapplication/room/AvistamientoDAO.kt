package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface AvistamientoDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimal(avistamiento: AnimalSimpleData): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimal(avistamiento: AnimalAdvanceData): Completable

    @Query("SELECT * FROM animals")
    fun getAnimalSimple(): Observable<List<AnimalSimpleData>>

    @Query("SELECT * FROM animals WHERE simpleId=:uid")
    fun getAnimalSimpleById( uid:Int): Observable<AnimalSimpleData>

    @Update
    fun updateAnimal( avistamientoData: AnimalSimpleData):Completable

    @Update
    fun updateAnimal( avistamientoData: AnimalAdvanceData):Completable

    @Delete
    fun deleteAnimal( avistamientoData: AnimalSimpleData):Completable
    @Delete
    fun deleteAnimal( avistamientoData: AnimalAdvanceData):Completable

    @Query("SELECT * FROM animals WHERE simpleId=:simpleId")
    fun getAnimalFullData(simpleId: Long):  Observable<SimpleAdvanceRelation>

    @Transaction
    @Query("SELECT * FROM animals")
    fun getAllData(): Observable<List<SimpleAdvanceRelation>>


}