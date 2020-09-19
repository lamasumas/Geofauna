package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.room.data_classes.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface AvistamientoDAO{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimal(muestreo: Transect): Single<Long>

    @Query("SELECT * FROM muestreos")
    fun getMuestreos():Observable<List<Transect>>

    @Delete
    fun deleteMuestreos(muestreo: Transect):Completable

    @Query("SELECT * FROM muestreos")
    fun getAllTransect(): Observable<List<Transect>>

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

    @Transaction
    @Query("Select * From muestreos WHERE transect_id=:transectId")
    fun getRelationAnimalsTransect(transectId:Long):Observable<TransectAnimalRelation>

}