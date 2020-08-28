package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers

class EditSightseen : AbstractDatabaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_avistamiento, container, false)
        view.findViewById<Button>(R.id.btnAñadirAvistamiento).visibility = View.GONE
        view.findViewById<Button>(R.id.btnEditDatabaseAnimal).visibility = View.VISIBLE
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idSimple: Long = arguments?.get("idSimple") as Long
        val idAdvance:Long = arguments?.get("idAdvance") as Long
        val dbRepository = DatabaseRepository(view.context)
        dbRepository.retrieveFullAnimalData(idSimple).observeOn(AndroidSchedulers.mainThread()).subscribe {
            view.findViewById<EditText>(R.id.etEspecie).setText(it.simpleData.especie)
            view.findViewById<EditText>(R.id.etLongitud).setText(it.simpleData.longitude.toString())
            view.findViewById<EditText>(R.id.etLatitud).setText(it.simpleData.latitude.toString())
            val time = it.simpleData.time.split(":")
            val date = it.simpleData.date.split("/")
            view.findViewById<EditText>(R.id.etHour).setText(time.get(0))
            view.findViewById<EditText>(R.id.etMinute).setText(time.get(1))
            view.findViewById<EditText>(R.id.etDay).setText(date.get(0))
            view.findViewById<EditText>(R.id.etMonth).setText(date.get(1))
            view.findViewById<EditText>(R.id.etYear).setText(date.get(2))

            view.findViewById<EditText>(R.id.etHumidity).setText(ifNullEmptyString(it.advanceData.humidity.toString()))
            view.findViewById<EditText>(R.id.etPais).setText(ifNullEmptyString(it.advanceData.pais))
            view.findViewById<EditText>(R.id.etLugar).setText(ifNullEmptyString(it.advanceData.lugar))
            view.findViewById<EditText>(R.id.etTemperature).setText(ifNullEmptyString(it.advanceData.temperature.toString()))
            view.findViewById<EditText>(R.id.etIndexUV).setText(ifNullEmptyString(it.advanceData.index_uv.toString()))
            view.findViewById<EditText>(R.id.etAltitude).setText(ifNullEmptyString(it.advanceData.altitude.toString()))
            view.findViewById<EditText>(R.id.etPressure).setText(ifNullEmptyString(it.advanceData.pressure.toString()))

        }
        view.findViewById<Button>(R.id.btnEditDatabaseAnimal).clicks().subscribe {
            dbRepository.updateAnimal(createSimpleAnimalObject(view))
            view.findNavController().navigate(EditSightseenDirections.actionEditSightseenToMainFragment2())
        }
    }


}