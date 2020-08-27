package com.example.myapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
@Database(entities = arrayOf(AnimalSimpleData::class, AnimalAdvanceData::class), version = 7)
abstract class AppDatabase:RoomDatabase(){
    abstract fun avistamientoDao():AvistamientoDAO;
}