package com.lamasumas.myapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lamasumas.myapplication.room.data_classes.AnimalAdvanceData
import com.lamasumas.myapplication.room.data_classes.AnimalSimpleData
import com.lamasumas.myapplication.room.data_classes.Transect

@Database(entities = arrayOf(AnimalSimpleData::class, AnimalAdvanceData::class, Transect::class), version = 22)
abstract class AppDatabase:RoomDatabase(){
    abstract fun avistamientoDao():AvistamientoDAO;
}