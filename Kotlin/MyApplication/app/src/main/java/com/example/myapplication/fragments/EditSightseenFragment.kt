package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AvistamientoData
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers

class EditSightseen : Fragment() {

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
        val uid:Int = arguments?.get("idAnimal") as Int
        val dbRepository = DatabaseRepository(view.context)
        dbRepository.retrieveAnimal(uid).observeOn(AndroidSchedulers.mainThread()).subscribe {
            view.findViewById<EditText>(R.id.etEspecie).setText(it.especie)
            view.findViewById<EditText>(R.id.etPais).setText(it.pais)
            view.findViewById<EditText>(R.id.etLugar).setText(it.lugar)
            view.findViewById<EditText>(R.id.etLongitud).setText(it.longitude.toString())
            view.findViewById<EditText>(R.id.etLatitud).setText(it.latitude.toString())
            val time = it.time.split(":")
            val date = it.date.split("/")
            view.findViewById<EditText>(R.id.etHour).setText(time.get(0))
            view.findViewById<EditText>(R.id.etMinute).setText(time.get(1))
            view.findViewById<EditText>(R.id.etDay).setText(date.get(0))
            view.findViewById<EditText>(R.id.etMonth).setText(date.get(1))
            view.findViewById<EditText>(R.id.etYear).setText(date.get(2))
        }
        view.findViewById<Button>(R.id.btnEditDatabaseAnimal).clicks().subscribe {
            dbRepository.updateAnimal(generateAnimal(view, uid))
            Toast.makeText(view.context, "Animal editado", Toast.LENGTH_LONG).show()
        }
    }

    private fun generateAnimal(view: View, uid: Int): AvistamientoData {
        val species = view.findViewById<EditText>( R.id.etEspecie).text.toString();
        val place = view.findViewById<EditText>( R.id.etLugar).text.toString();
        val country = view.findViewById<EditText>( R.id.etPais).text.toString();
        val latitude = view.findViewById<EditText>(R.id.etLatitud).text.toString();
        val longitude = view.findViewById<EditText>(R.id.etLongitud).text.toString();
        val time = view.findViewById<EditText>(R.id.etHour).text.toString() +
                ":"+view.findViewById<EditText>(R.id.etMinute).text.toString();
        val date = view.findViewById<EditText>(R.id.etDay).text.toString() + "/" +
                view.findViewById<EditText>(R.id.etMonth).text.toString() +
                "/" + view.findViewById<EditText>(R.id.etYear).text.toString();
        return AvistamientoData(uid=uid, especie=species, date = date, latitude =  latitude.toDouble(),
                longitude = longitude.toDouble(), lugar = place, pais = country, time = time);
    }
}