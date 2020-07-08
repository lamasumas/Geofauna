package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.room.data_classes.AvistamientoData
import com.example.myapplication.room.AppDatabase

class DatabaseViewModel: ViewModel() {
    var db:AppDatabase? = null;
    var sightings = MutableLiveData<List<AvistamientoData>>();


}