package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.Transect
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class TransectViewModel(application: Application) : AndroidViewModel(application), GeneralViewModel {
    private val dbRepository = DatabaseRepository(application)
    val transectList: MutableLiveData<ArrayList<Transect>> = MutableLiveData()
    val selectedTransect = MutableLiveData<Transect>()
    val selectedId = MutableLiveData<Long>()
    var prestarted = false


    init {
        transectList.value = ArrayList()
        dbRepository.retrieveTransects().observeOn(AndroidSchedulers.mainThread()).subscribe {
            transectList.value?.clear()
            transectList.value?.addAll(it)
            transectList.value = transectList.value
        }
    }

    fun choosenTransect(transectId: Long): Disposable? {
        selectedId.value = transectId
        return dbRepository.retrieveTransectById(transectId).observeOn(AndroidSchedulers.mainThread()).subscribe {
            selectedTransect.value = it
        }
    }

    fun addTransect(transect: Transect) {
        dbRepository.insertNewTransect(transect)
    }

    fun deleteTransect(idTransect: Long):Disposable {
        return dbRepository.deleteTransect(idTransect)
    }

    override fun preStart(){
        Log.d("Initialization", "TransectViewModel startted")
    }

    fun samplingValues(pressure: String?, altitude: String?) {
        selectedTransect.value?.pressureSampling = pressure?.toDouble()
        selectedTransect.value?.altitudeSampling = altitude?.toDouble()
        dbRepository.updateTransect(selectedTransect.value)
    }

    fun retrieveTransect(id:Long): Observable<Transect> {
        return dbRepository.retrieveTransectById(id)
    }
    fun updateTransect(transect:Transect){
        dbRepository.updateTransect(transect)
    }


}