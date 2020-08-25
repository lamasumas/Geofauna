package com.example.myapplication.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class DatabaseRepository(context: Context){
    val db =  Room.databaseBuilder(context, AppDatabase::class.java, "application_db")
            .fallbackToDestructiveMigration().build();

    fun insertNewAnimalToDB(animalSimpleData: AnimalSimpleData) {
        db.avistamientoDao().insertAnimalSimple(animalSimpleData).subscribeOn(Schedulers.io()).subscribe{
            Log.e("Database repository", "Animnal added")}
    }


    fun retrieveSightsings(): Observable<List<AnimalSimpleData>> {
        return db.avistamientoDao().getAnimalSimple()
    }

    fun retrieveAnimal(uid:Int):Observable<AnimalSimpleData>{
        return db.avistamientoDao().getAnimalSimpleById(uid)
    }

    fun updateAnimal(animal: AnimalSimpleData){
        db.avistamientoDao().updateAnimalSimple(animal).subscribeOn(Schedulers.io()).subscribe{
            Log.e("Database repository", "Anmimal updated")}
    }
    fun deleteAnimal(animal:AnimalSimpleData){
        db.avistamientoDao().deleteAnimalSimple(animal).subscribe {
            Log.e("Database repository", "Animnal deleted")
        }
    }

}