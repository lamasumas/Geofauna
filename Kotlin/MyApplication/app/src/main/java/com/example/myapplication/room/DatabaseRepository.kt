package com.example.myapplication.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class DatabaseRepository(context: Context){
    val db =  Room.databaseBuilder(context, AppDatabase::class.java, "application_db")
            .fallbackToDestructiveMigration().build();

    fun insertNewAnimalToDB(animalSimpleData: AnimalSimpleData) {
        db.avistamientoDao().insertAnimalSimple(animalSimpleData).subscribeOn(Schedulers.io()).subscribe{
            Log.d("Database repository", "Animnal simple added")}
    }

    fun insertNewAnimalToDB(animalSimpleData: AnimalSimpleData, animalAdvanceData: AnimalAdvanceData){
        insertNewAnimalToDB(animalSimpleData)
        db.avistamientoDao().insertAnimalAdvance(animalAdvanceData).subscribeOn(Schedulers.io()).subscribe{
            Log.d("Database repository", "Animnal Advace added")}
    }


    fun retrieveSightsings(): Observable<List<SimpleAdvanceRelation>> {
        return db.avistamientoDao().getAllData()
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