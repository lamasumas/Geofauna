package com.example.myapplication.room

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.room.data_classes.AvistamientoData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*


class DatabaseRepository(context: Context){
    val db =  Room.databaseBuilder(context, AppDatabase::class.java, "application_db").build();

    fun insertNewAnimalToDB(animal: AvistamientoData) {
        db.avistamientoDao().insertAvistamiento(animal).subscribeOn(Schedulers.io()).subscribe({
            Log.e("Database repository", "Animnal added")})
    }


    fun retrieveSightsings(): Observable<List<AvistamientoData>> {
        return db.avistamientoDao().getAlllAvistamiento()
    }

}