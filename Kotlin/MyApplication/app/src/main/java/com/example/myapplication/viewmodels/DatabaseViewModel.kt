package com.example.myapplication.viewmodels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data_classes.AvistamientoData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.DatabaseInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseViewModel(val db:AppDatabase): ViewModel() {
    var sightings = MutableLiveData<List<AvistamientoData>>();
    fun insertNewAnimalToDB(animal:AvistamientoData) {
        db.avistamientoDao().insertAvistamiento(animal)
                .subscribeOn(Schedulers.io());
    }

    fun retrieveSightsings(){
        db.avistamientoDao().getAlllAvistamiento().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    onNext -> sightings.value = onNext;
                }
    }

}