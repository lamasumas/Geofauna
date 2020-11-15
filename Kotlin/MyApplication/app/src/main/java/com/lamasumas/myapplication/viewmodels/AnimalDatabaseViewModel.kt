package com.lamasumas.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lamasumas.myapplication.room.DatabaseRepository
import com.lamasumas.myapplication.room.data_classes.AnimalAdvanceData
import com.lamasumas.myapplication.room.data_classes.AnimalSimpleData
import com.lamasumas.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AnimalDatabaseViewModel(application: Application) : AndroidViewModel(application), GeneralViewModel {

    private val dbRepository = DatabaseRepository(application)
    val dataList: MutableLiveData<ArrayList<SimpleAdvanceRelation>> = MutableLiveData()
    var transectId: Long = -1

    init {
        dataList.value = ArrayList()
    }

    fun addNewAnimal(newDatabaseSimpleEntry: AnimalSimpleData, newDatabaseAdvanceEntry: AnimalAdvanceData): Disposable {
        return dbRepository.insertNewAnimalToDB(newDatabaseSimpleEntry, newDatabaseAdvanceEntry)
    }

    fun loadData(idTransect: Long): Disposable? {
        transectId = idTransect
        return dbRepository.retrieveFullAnimalDataFromTransectID(transectId).observeOn(Schedulers.io()).doOnNext {
            dataList.value?.clear()
            dataList.value?.addAll(it)
        }.observeOn(AndroidSchedulers.mainThread())?.subscribe {
            dataList.value = dataList.value
        }
    }

    fun cleanDatabase(transectId: Long) {
        dbRepository.cleanTransectAnaimals(transectId)
    }

    fun retriveFullAnimalData(simpleId: Long): Observable<SimpleAdvanceRelation> {
        return dbRepository.retrieveFullAnimalData(simpleId)
    }

    fun editAnimal(tempSimple: AnimalSimpleData, tempAdvance: AnimalAdvanceData): Disposable {

        return dbRepository.updateAnimal(tempSimple, tempAdvance)
    }

    fun deleteAnimal(simpleData: Long): Disposable {
        return dbRepository.deleteAnimal(simpleData)
    }

    override fun preStart() {
        System.out.println("AnimalDatabaseViewModel started")
        //Log.d("Initialization", "AnimalDatabaseViewModel started")
    }

}
