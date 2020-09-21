package com.example.myapplication.viewmodels.controllers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

abstract class Controller(application: Application) : AndroidViewModel(application){
    val myData: HashMap<Int, MutableLiveData<String>> = HashMap()
}