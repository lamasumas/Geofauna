package com.example.myapplication.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.room.data_classes.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class DatabaseRepository(context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "application_db")
        .fallbackToDestructiveMigration().build();

    /**
     * Función que inserta un nuevo transecto en la base de datos
     * @param transect, transecto a insertar
     * @return disposable generado en la operación reactiva
     */
    fun insertNewTransect(transect: Transect): Disposable {
        return db.avistamientoDao().insertAnimal(transect).subscribeOn(Schedulers.io())
            .subscribe { x ->
                Log.d("Database Repository", "Transect added")
            }
    }

    /**
     * Función que obtiene de la base de datos todos los transectos
     * @return observable de la lista de transectos
     */
    fun retrieveTransects(): Observable<List<Transect>> {
        return db.avistamientoDao().getAllTransect()

    }

    /**
     * Función que inserta en la base de datos un nuevo animal
     * @param animalAdvanceData, datos avanzados del animal
     * @param animalSimpleData, datos simples del animal
     * @return disposable generado en la operación reactiva
     */
    fun insertNewAnimalToDB(
        animalSimpleData: AnimalSimpleData,
        animalAdvanceData: AnimalAdvanceData
    ): Disposable {
        return db.avistamientoDao().insertAnimal(animalSimpleData).subscribeOn(Schedulers.io())
            .subscribe { returnedRowId ->
                animalAdvanceData.simpleId = returnedRowId
                db.avistamientoDao().insertAnimal(animalAdvanceData).subscribeOn(Schedulers.io())
                    .subscribe {

                    }
            }

    }

    /**
     * Recupera un transecto por el id
     * @param transectId, id del transecto a recuperar
     * @return observable del transecto
     */
    fun retrieveTransectById(transectId: Long): Observable<Transect> {
        return db.avistamientoDao().getTransectById(transectId)
    }

    /**
     * Recupera toda los animales asignados a un transecto
     * @param transectId, id del transecto
     *@return observable con toda la información completa de todos los animal
     */
    fun retrieveAllAnimalDataFromATransect(transectId: Long): Observable<TransectAnimalRelation> {
        return db.avistamientoDao().getRelationAnimalsTransect(transectId)
    }

    /**
     * Función para recuperar de la base de datos una lista con la relación simple/advance de los avistamientos
     * @return observable de esta lista
     */
    fun retrieveSightseeing(): Observable<List<SimpleAdvanceRelation>> {
        return db.avistamientoDao().getAllData()
    }

    /**
     * Recupera la información simple de un animal
     * @param uid, id de la información simple del animal
     * @return observable de la información simple del avistamiento
     */
    fun retrieveSimpleAnimal(uid: Int): Observable<AnimalSimpleData> {
        return db.avistamientoDao().getAnimalSimpleById(uid)
    }

    /**
     * Recupera toda la inforamción que hay en la base de datos de un avistamiento
     * @param simpleId, id de la información simple del animal
     * @return la relación de la información simple y avanazada
     */
    fun retrieveFullAnimalData(simpleId: Long): Observable<SimpleAdvanceRelation> {
        return db.avistamientoDao().getAnimalFullData(simpleId)
    }

    /**
     * Actualiza un animal en la base de datos
     * @param simple, inforamción simple del animal
     * @param advance, información avanzada del animal
     * @return disposable generado  en la operación reactiva
     */
    fun updateAnimal(simple: AnimalSimpleData, advance: AnimalAdvanceData): Disposable {
        return db.avistamientoDao().updateAnimal(simple).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe {
                db.avistamientoDao().updateAnimal(advance).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io()).subscribe {
                        Log.e("Database repository", "Anmimal updated")
                    }
            }
    }

    /**
     * Borra un animal de la base de datos
     * @param simple, información simple de un avistamiento
     * @param advance, información avanzada de un avistamiento
     * @return disposable generado en la operación reactiva
     */
    private fun deleteAnimal(simple: AnimalSimpleData, advance: AnimalAdvanceData): Disposable {
        return db.avistamientoDao().deleteAnimal(simple).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe {
                db.avistamientoDao().deleteAnimal(advance).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io()).subscribe {
                        Log.d("Database repository", "Animnal deleted")
                    }
            }
    }

    /**
     * Recupera todos los animales asociados a un transecto
     * @param transectId, id del transecto
     * @return observable  de la lista de animales solicitada
     */
    fun retrieveFullAnimalDataFromTransectID(transectId: Long): Observable<List<SimpleAdvanceRelation>> {
        return db.avistamientoDao().getAnimalFullDataOfTransectTransaction(transectId)
    }

    /**
     * Función que elimina todos los animales asociados a un transecto
     * @param transectId, tranecto a limpiar
     */
    fun cleanTransectAnaimals(transectId: Long) {
        db.avistamientoDao().deleteAnimalsByTransectId(transectId).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe {
                Log.d("Database repository", "Animnal deleted by transect id")
            }
    }

    /**
     * Borra un transecto de la base de datos
     * @param idTransect, id del transecto a borrar
     * @return disposable generado en la operación reactiva
     */
    fun deleteTransect(idTransect: Long): Disposable {
        return retrieveFullAnimalDataFromTransectID(idTransect).subscribeOn(Schedulers.io())
            .subscribe {
                it.forEach { deleteAnimal(it.simpleData, it.advanceData) }
                db.avistamientoDao().deleteTransectById(idTransect).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io()).subscribe {
                        Log.d("Database repository", "Deleted transect")
                    }
            }
    }

    /**
     * Actualiza un transecto de la base de datos
     * @param transect, transecto a actualizar
     */
    fun updateTransect(transect: Transect?) {
        transect?.let {
            db.avistamientoDao().updateTransect(it).subscribeOn(Schedulers.io()).subscribe {
                Log.d("Database repository", "Updated transect with id " + it.uid.toString())
            }
        }

    }

    /**
     * Borra un animal de la base de datos
     * @param simpleData, id del animal a borar
     * @return disposable generado en la operación reactiva
     */
    fun deleteAnimal(simpleData: Long): Disposable {
        return db.avistamientoDao().getAnimalFullData(simpleData).observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io()).subscribe {
                deleteAnimal(it.simpleData, it.advanceData)
            }
    }

}