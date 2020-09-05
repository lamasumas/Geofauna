package com.example.myapplication.fragments.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.fragments.AvistamientoFragmentDirections
import com.example.myapplication.fragments.EditSightseenDirections
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.NumberFormatException

abstract class AbstractDatabaseFragment : GeneralFragmentRx() {

    protected fun setGeneralButtonActions(view: View, isEdit: Boolean = false, idSimple: Long = 0, idAdvance: Long = 0) {

        val dbRepository = DatabaseRepository(view.context)
        disposables.add(view.findViewById<FloatingActionButton>(R.id.btnAñadirAvistamiento).clicks().subscribe {
            val newDatabaseSimpleEntry = createSimpleAnimalObject(view)
            val newDatabaseAdvanceEntry = createAdvanceAnimalObject(view)
            disposables.add(dbRepository.insertNewAnimalToDB(newDatabaseSimpleEntry, newDatabaseAdvanceEntry))


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
            if(hiddenView.visibility == View.VISIBLE)
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
                    tempAdvance.simpleId= idSimple
                    disposables.add(dbRepository.updateAnimal(tempSimple, tempAdvance))
                }.subscribe {
                    view.findNavController().navigate(EditSightseenDirections.actionEditSightseenToMainFragment2())
                })
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
                longitude = longitude.toDouble(), time = time)


    }

    protected fun createAdvanceAnimalObject(view: View): AnimalAdvanceData {
        val place = view.findViewById<EditText>(R.id.etLugar).text.toString()
        val country = view.findViewById<EditText>(R.id.etPais).text.toString()
        val humidity = view.findViewById<EditText>(R.id.etHumidity).text.toString()
        val altitude = view.findViewById<EditText>(R.id.etAltitude).text.toString()
        val uv = view.findViewById<EditText>(R.id.etIndexUV).text.toString()
        val temperature = view.findViewById<EditText>(R.id.etTemperature).text.toString()
        val pressure = view.findViewById<EditText>(R.id.etPressure).text.toString()

        return AnimalAdvanceData(pais = country, lugar = place, humidity = checkDataInput(humidity),
                temperature = checkDataInput(temperature), pressure = checkDataInput(pressure),
                altitude = checkDataInput(altitude), index_uv = checkDataInput(uv)?.toInt())
    }

    private fun checkDataInput(input: String): Double? {
        var temp: Double? = null
        try {
            temp = input.toDouble()
        } catch (e: NumberFormatException) {
            return null
        } finally {
            return temp
        }
    }

    fun ifNullEmptyString(input: String?): String {
        if (input == null || input.equals("null"))
            return ""
        else
            return input
    }


}