package com.example.myapplication.fragments.animals_database

import android.content.Context
import com.example.myapplication.fragments.abstracts.AbstractRecyclerViewData
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.schedulers.Schedulers

class AnimalDatabaseViewData(private val transectId:Long, context: Context) : AbstractRecyclerViewData<SimpleAdvanceRelation>() {

    private val dbRepository = DatabaseRepository(context)
    init {
        checkForNews()
    }
    override fun checkForNews() {
        if (disposable != null)
            disposable?.dispose()
        disposable = dbRepository.retrieveAllAnimalDataFromATransect(transectId)
                ?.subscribeOn(Schedulers.io())?.subscribe {
                    dataList.value?.add(it)}
    }

}