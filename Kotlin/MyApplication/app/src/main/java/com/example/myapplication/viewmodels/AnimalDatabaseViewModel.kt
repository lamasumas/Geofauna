package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import com.example.myapplication.room.data_classes.Transect
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AnimalDatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val dbRepository = DatabaseRepository(application)
    val dataList : MutableLiveData<ArrayList<SimpleAdvanceRelation>> = MutableLiveData()
    var transectId: Long = -1

    init {
        dataList.value = ArrayList()
    }

    fun addNewAnimal(newDatabaseSimpleEntry: AnimalSimpleData, newDatabaseAdvanceEntry: AnimalAdvanceData): Disposable {
        return dbRepository.insertNewAnimalToDB(newDatabaseSimpleEntry, newDatabaseAdvanceEntry)
    }

    fun loadData(idTransect: Long) {
        if (transectId != idTransect) {
            transectId = idTransect
            dbRepository.retrieveFullAnimalDataFromTransectID(transectId!!)?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                dataList.value?.clear()
                dataList.value?.addAll(it)
                dataList.value = dataList.value

            }
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

}
