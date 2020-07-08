package com.example.myapplication.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.room.data_classes.AvistamientoData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*


class DatabaseRepository(context: Context){
    val db =  Room.databaseBuilder(context, AppDatabase::class.java, "applicaiton_db").build();

    fun insertNewAnimalToDB(animal: AvistamientoData) {
        db.avistamientoDao().insertAvistamiento(animal)
    }


    fun retrieveSightsings(): LiveData<List<AvistamientoData>> {
        return db.avistamientoDao().getAlllAvistamiento()
    }

}