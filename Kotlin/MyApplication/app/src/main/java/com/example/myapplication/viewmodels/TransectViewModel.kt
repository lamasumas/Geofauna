package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.Transect
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class TransectViewModel(application: Application) : AndroidViewModel(application) {
    private val dbRepository = DatabaseRepository(application)
    var idTransect = MutableLiveData<Long>()
    val transectList: MutableLiveData<ArrayList<Transect>> = MutableLiveData()

    init {
        transectList.value = ArrayList()
        dbRepository.retrieveTransects().observeOn(AndroidSchedulers.mainThread()).subscribe {
            transectList.value?.clear()
            transectList.value?.addAll(it)
            transectList.value = transectList.value
        }
    }

    fun addTransect(transect: Transect) {
        dbRepository.insertNewTransect(transect)
    }

    fun deleteTransect(idTransect: Long):Disposable {
        return dbRepository.deleteTransect(idTransect)
    }

}