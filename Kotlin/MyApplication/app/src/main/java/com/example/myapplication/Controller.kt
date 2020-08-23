package com.example.myapplication

import androidx.lifecycle.MutableLiveData

abstract class Controller{
    val myData: HashMap<Int, MutableLiveData<String>> = HashMap()
}