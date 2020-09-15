package com.example.myapplication.utils

import androidx.lifecycle.MutableLiveData

abstract class Controller{
    val myData: HashMap<Int, MutableLiveData<String>> = HashMap()
}