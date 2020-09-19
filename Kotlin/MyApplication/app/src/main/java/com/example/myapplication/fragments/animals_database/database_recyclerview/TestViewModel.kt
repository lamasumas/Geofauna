package com.example.myapplication.fragments.animals_database.database_recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation


class TestViewModel: ViewModel(){
    val dataList: MutableLiveData<ArrayList<SimpleAdvanceRelation>> = MutableLiveData()
    init {
        dataList.value = ArrayList<SimpleAdvanceRelation>()
    }

    fun clear(){
        dataList.value?.clear()
    }
}
