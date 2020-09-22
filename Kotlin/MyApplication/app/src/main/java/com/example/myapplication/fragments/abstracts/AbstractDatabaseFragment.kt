package com.example.myapplication.fragments.abstracts

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.myapplication.utils.InputValidator
import com.example.myapplication.R
import com.example.myapplication.fragments.AvistamientoFragmentDirections
import com.example.myapplication.fragments.EditSightseenDirections
import com.example.myapplication.viewmodels.AnimalDatabaseViewModel
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.example.myapplication.viewmodels.TransectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.schedulers.Schedulers

abstract class AbstractDatabaseFragment() : GeneralFragmentRx() {


    val animalDatabaseViewModel: AnimalDatabaseViewModel by activityViewModels()
    val transectViewModel: TransectViewModel by activityViewModels()



    protected fun setGeneralButtonActions(view: View, isEdit: Boolean = false, idSimple: Long = 0, idAdvance: Long = 0) {

        disposables.add(view.findViewById<FloatingActionButton>(R.id.btnAñadirAvistamiento).clicks().subscribe {
            if (checkValidSimpleData(view)) {

                var builder = AlertDialog.Builder(view.context, R.style.alertDialog)
                val newDatabaseSimpleEntry = createSimpleAnimalObject(view)
                val newDatabaseAdvanceEntry = createAdvanceAnimalObject(view)
                transectViewModel.transectList.value
                disposables.add( animalDatabaseViewModel.addNewAnimal(newDatabaseSimpleEntry, newDatabaseAdvanceEntry))

                val alertView = LayoutInflater.from(view.context).inflate(R.layout.animal_added_dialog, null)
                builder.setView(alertView).create().also { dialog ->
                    dialog.setCanceledOnTouchOutside(true)
                    disposables.add(alertView.findViewById<Button>(R.id.btnAdded).clicks().subscribe {
                        dialog.dismiss()
                    })
                    dialog.show()
                }
            } else
                AlertDialog.Builder(view.context).setMessage(R.string.wrongInputMessage)
                        .setTitle(R.string.wrongInputTitulo)
                        .setNeutralButton(R.string.cerrarAlertBoton) { dialog, id -> dialog.dismiss() }
                        .create().show()

        })

        disposables.add(view.findViewById<FloatingActionButton>(R.id.btnBack).clicks().subscribe {
            if (isEdit)
                view.findNavController().navigate(EditSightseenDirections.actionEditSightseenToMainFragment2())
            else
                view.findNavController().navigate(AvistamientoFragmentDirections.actionAvistamiento2ToMainFragment2())
        })

        disposables.add(view.findViewById<TextView>(R.id.btnExpand).clicks().subscribe {

            val btn = view.findViewById<Button>(R.id.btnExpand)
            val hiddenView = view.findViewById<LinearLayout>(R.id.lhidden)
            if (hiddenView.visibility == View.VISIBLE)
                hiddenView.visibility = View.GONE
            else
                hiddenView.visibility = View.VISIBLE


        })

        disposables.add(view.findViewById<Button>(R.id.btnEditDatabaseAnimal).clicks().observeOn(Schedulers.io())
                .doOnNext {
                    val tempSimple = createSimpleAnimalObject(view)
                    val tempAdvance = createAdvanceAnimalObject(view)
                    tempSimple.simpleId = idSimple
                    tempAdvance.uid = idAdvance
                    tempAdvance.simpleId = idSimple
                    disposables.add(animalDatabaseViewModel.editAnimal(tempSimple, tempAdvance))
                }.subscribe {
                    view.findNavController().navigate(EditSightseenDirections.actionEditSightseenToMainFragment2())
                })
    }

    private fun checkValidSimpleData(view: View): Boolean {
        return checkValidEditText(R.id.etEspecie, view) &&
                checkValidEditText(R.id.etLatitud, view) &&
                checkValidEditText(R.id.etLongitud, view) &&
                checkValidEditText(R.id.etHour, view) &&
                checkValidEditText(R.id.etMinute, view) &&
                checkValidEditText(R.id.etDay, view) &&
                checkValidEditText(R.id.etMonth, view) &&
                checkValidEditText(R.id.etYear, view)

    }

    private fun checkValidEditText(id: Int, view: View): Boolean {
        return view.findViewById<EditText>(id).text.isNotEmpty() && view.findViewById<EditText>(id).text.isNotBlank()
    }

    protected fun createSimpleAnimalObject(view: View): AnimalSimpleData {
        val species = view.findViewById<EditText>(R.id.etEspecie).text.toString()
        val latitude = view.findViewById<EditText>(R.id.etLatitud).text.toString()
        val longitude = view.findViewById<EditText>(R.id.etLongitud).text.toString()
        val time = view.findViewById<EditText>(R.id.etHour).text.toString() +
                ":" + view.findViewById<EditText>(R.id.etMinute).text.toString()
        val date = view.findViewById<EditText>(R.id.etDay).text.toString() + "/" +
                view.findViewById<EditText>(R.id.etMonth).text.toString() +
                "/" + view.findViewById<EditText>(R.id.etYear).text.toString()

        return AnimalSimpleData(especie = species, date = date, latitude = latitude.toDouble(),
                longitude = longitude.toDouble(), time = time, transect_id = transectViewModel.selectedId.value!! )


    }

    protected fun createAdvanceAnimalObject(view: View): AnimalAdvanceData {
        val humidity = view.findViewById<EditText>(R.id.etHumidity).text.toString()
        val altitude = view.findViewById<EditText>(R.id.etAltitude).text.toString()
        val uv = view.findViewById<EditText>(R.id.etIndexUV).text.toString()
        val temperature = view.findViewById<EditText>(R.id.etTemperature).text.toString()
        val pressure = view.findViewById<EditText>(R.id.etPressure).text.toString()
        val notes = view.findViewById<EditText>(R.id.etNotes).text.toString()
        val validator = InputValidator()
        return AnimalAdvanceData(
                pais = validator.nullOrEmpty(transectViewModel.selectedTransect.value?.country),
                lugar = validator.nullOrEmpty(transectViewModel.selectedTransect.value?.locality),
                humidity = validator.doubleOrNull(humidity),
                temperature = validator.doubleOrNull(temperature),
                pressure = validator.doubleOrNull(pressure),
                altitude = validator.doubleOrNull(altitude),
                index_uv = validator.doubleOrNull(uv)?.toInt(),
                notes =  validator.nullOrEmpty(notes))
    }






}