package com.example.myapplication.fragments.transects

import android.content.Context
import com.example.myapplication.fragments.abstracts.AbstractRecyclerViewData
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.Transect
import io.reactivex.schedulers.Schedulers

class TransectRecyclerViewData( context: Context) : AbstractRecyclerViewData<Transect>() {


    private val dbRepository = DatabaseRepository(context)
    init {
         checkForNews()
    }
    override fun checkForNews() {
        if (disposable != null)
            disposable?.dispose()
        disposable = dbRepository.retrieveTransects()
                .subscribeOn(Schedulers.io()).subscribe {
                    dataList.value?.clear()
                    dataList.value?.addAll(it)
                }
    }


}