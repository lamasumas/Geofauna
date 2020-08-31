package com.example.myapplication.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class DatabaseRepository(context: Context) {
    val db = Room.databaseBuilder(context, AppDatabase::class.java, "application_db")
            .fallbackToDestructiveMigration().build();


    fun insertNewAnimalToDB(animalSimpleData: AnimalSimpleData, animalAdvanceData: AnimalAdvanceData) : Disposable{
        return db.avistamientoDao().insertAnimal(animalSimpleData).subscribeOn(Schedulers.io()).subscribe { returnedRowId ->
            Log.d("Database repository", "Animnal simple added")
            animalAdvanceData.simpleId = returnedRowId
            db.avistamientoDao().insertAnimal(animalAdvanceData).subscribeOn(Schedulers.io()).subscribe {

            }
        }

    }


    fun retrieveSightseeing(): Observable<List<SimpleAdvanceRelation>> {
        return db.avistamientoDao().getAllData()
    }

    fun retrieveSimpleAnimal(uid: Int): Observable<AnimalSimpleData> {
        return db.avistamientoDao().getAnimalSimpleById(uid)
    }

    fun retrieveFullAnimalData(simpleId: Long): Observable<SimpleAdvanceRelation> {
        return db.avistamientoDao().getAnimalFullData(simpleId)
    }

    fun updateAnimal(simple: AnimalSimpleData, advance: AnimalAdvanceData) : Disposable{
        return db.avistamientoDao().updateAnimal(simple).subscribeOn(Schedulers.io()).subscribe {
            db.avistamientoDao().updateAnimal(advance).subscribeOn(Schedulers.io()).subscribe {
                Log.e("Database repository", "Anmimal updated")
            }
        }
    }

    fun deleteAnimal(simple: AnimalSimpleData, advance: AnimalAdvanceData): Disposable {
        return db.avistamientoDao().deleteAnimal(simple).subscribeOn(Schedulers.io()).subscribe {
            db.avistamientoDao().deleteAnimal(advance).subscribe {
                Log.e("Database repository", "Animnal deleted")
            }
        }
    }

}