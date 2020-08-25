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
    fun insertAnimalSimple(vararg avistamiento: AnimalSimpleData): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimalAdvance(vararg avistamiento: AnimalAdvanceData): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRelation(vararg simpleAdvanceRelation: SimpleAdvanceRelation)

    @Query("SELECT * FROM animals")
    fun getAnimalSimple(): Observable<List<AnimalSimpleData>>

    @Query("SELECT * FROM animals WHERE simple_id=:uid")
    fun getAnimalSimpleById(vararg uid:Int): Observable<AnimalSimpleData>

    @Update
    fun updateAnimalSimple(vararg avistamientoData: AnimalSimpleData):Completable

    @Delete
    fun deleteAnimalSimple(vararg avistamientoData: AnimalSimpleData):Completable

    @Transaction
    @Query("SELECT * FROM animals")
        fun getAllData(): List<SimpleAdvanceRelation>




}