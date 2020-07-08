package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myapplication.room.data_classes.AvistamientoData
import com.example.myapplication.room.AppDatabase

class SightseenViewModel(val db_instance:AppDatabase): ViewModel(){
    var db: AppDatabase = db_instance;

    fun insertNewAnimalToDB(animal: AvistamientoData) {
        db.avistamientoDao().insertAvistamiento(animal)
    }

}