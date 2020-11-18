package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.Transect
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class TransectViewModel(application: Application) : AndroidViewModel(application),
    GeneralViewModel {
    private val dbRepository = DatabaseRepository(application)
    val transectList: MutableLiveData<ArrayList<Transect>> = MutableLiveData()
    val selectedTransect = MutableLiveData<Transect>()
    val selectedId = MutableLiveData<Long>()
    var prestarted = false

    /**
     * Precarga los transectos y observa futuros cambios con programacion reactiva
     */
    init {
        transectList.value = ArrayList()
        dbRepository.retrieveTransects().observeOn(AndroidSchedulers.mainThread()).subscribe {
            transectList.value?.clear()
            transectList.value?.addAll(it)
            transectList.value = transectList.value
        }
    }

    /**
     * Cambia el transecto selecionado almacenado en este viewmodel, para luego poder saber sobre
     * que transecto se estra trabajando
     * @param transectId, id del transecto a guardar
     * @return disposable de la operacion reactiva
     */
    fun choosenTransect(transectId: Long): Disposable? {
        selectedId.value = transectId
        return dbRepository.retrieveTransectById(transectId)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                selectedTransect.value = it
            }
    }

    /**
     * Añade un nuevo transecto a la vase de datos
     * @param transect, transecto a añadir
     */
    fun addTransect(transect: Transect) {
        dbRepository.insertNewTransect(transect)
    }

    /**
     * Borra un transecto de la base de datos
     * @param idTransect, id del transecto a borrar
     * @return disposable devuelto por la operacion reactiva
     */
    fun deleteTransect(idTransect: Long): Disposable {
        return dbRepository.deleteTransect(idTransect)
    }

    /**
     * Funcion utilizada para lanzar los viewmodel en la pantalla splash
     */
    override fun preStart() {
        Log.d("Initialization", "TransectViewModel startted")
    }

    /**
     *  Asigna la relacioón altura-presión con la que se calculara más la altura
     *  @param pressure, presión como string
     *  @param altitude, altitud como string
     */
    fun samplingValues(pressure: String?, altitude: String?) {
        selectedTransect.value?.pressureSampling = pressure?.toDouble()
        selectedTransect.value?.altitudeSampling = altitude?.toDouble()
        dbRepository.updateTransect(selectedTransect.value)
    }

    /**
     * Recupera un transecto de la base de datos por el id
     * @param id, id del transecto a buscar
     * @return retorna un observable del transecto
     */
    fun retrieveTransect(id: Long): Observable<Transect> {
        return dbRepository.retrieveTransectById(id)
    }

    /**
     * Actualiza un transecto almacenado en la base de datos
     * @param transect, transecto a acutualizar
     */
    fun updateTransect(transect: Transect) {
        dbRepository.updateTransect(transect)
    }


}