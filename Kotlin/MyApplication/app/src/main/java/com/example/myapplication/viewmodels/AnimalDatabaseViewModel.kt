package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import com.example.myapplication.room.data_classes.Transect
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AnimalDatabaseViewModel(application: Application) : AndroidViewModel(application),
    GeneralViewModel {

    private val dbRepository = DatabaseRepository(application)
    val dataList: MutableLiveData<ArrayList<SimpleAdvanceRelation>> = MutableLiveData()
    var transectId: Long = -1

    init {
        dataList.value = ArrayList()
    }

    /**
     * Función con la que añadir un animal a la base de datos
     * @param newDatabaseAdvanceEntry, advance data del avistamiento
     * @param newDatabaseSimpleEntry, simple data del avistamiento
     * @return disposable generado en la operación reactiva
     */
    fun addNewAnimal(
        newDatabaseSimpleEntry: AnimalSimpleData,
        newDatabaseAdvanceEntry: AnimalAdvanceData
    ): Disposable {
        return dbRepository.insertNewAnimalToDB(newDatabaseSimpleEntry, newDatabaseAdvanceEntry)
    }

    /**
     * Carga lops animales avistamo en un transecto mediante el id del transecto
     * @param idTransect, id del transecto
     * @return disposable generado por la operación reactiva
     */
    fun loadData(idTransect: Long): Disposable? {
        transectId = idTransect
        return dbRepository.retrieveFullAnimalDataFromTransectID(transectId)
            .observeOn(Schedulers.io()).doOnNext {
                dataList.value?.clear()
                dataList.value?.addAll(it)
            }.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                dataList.value = dataList.value
            }
    }

    /**
     * Borra todos los avistamiento asociados a un transecto
     * @param transectId, id del transecto que se va a limpiar
     */
    fun cleanDatabase(transectId: Long) {
        dbRepository.cleanTransectAnaimals(transectId)
    }

    /**
     * Recupera de la base de datos la información completa de los datos simples asociados a un avistamiento
     * @param simpleId, id de los datos simples
     * @return observable de la relación simple/advance
     */
    fun retriveFullAnimalData(simpleId: Long): Observable<SimpleAdvanceRelation> {
        return dbRepository.retrieveFullAnimalData(simpleId)
    }

    /**
     * Edita la entrada de un animal
     * @param tempAdvance, datos avanazados de un avistamiento
     * @param tempSimple, datos simples de un avistamiento
     * @return disposable generado en la operacón reactiva
     */
    fun editAnimal(tempSimple: AnimalSimpleData, tempAdvance: AnimalAdvanceData): Disposable {

        return dbRepository.updateAnimal(tempSimple, tempAdvance)
    }

    /**
     * Borra las entradas de un avistamiento (simple y advance)
     * @param simpleData, id del información simple de un avistamiento
     */
    fun deleteAnimal(simpleData: Long): Disposable {
        return dbRepository.deleteAnimal(simpleData)
    }

    /**
     * Función que se utiliza para lanzar por primera vez este viewmodel en la ventana splash
     */
    override fun preStart() {
        Log.d("Initialization", "AnimalDatabaseViewModel startted")
    }

}
