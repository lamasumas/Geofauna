package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data_classes.AvistamientoData

@Database(entities = arrayOf(AvistamientoData::class), version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun avistamientoDao():AvistamientoDAO;
}