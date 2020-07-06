package com.example.myapplication.fragments

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.LocationManager
import com.example.myapplication.R
import com.example.myapplication.data_classes.AvistamientoData
import com.example.myapplication.database.DatabaseInstance
import com.example.myapplication.viewmodels.DatabaseViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class Avistamiento : Fragment() {

    var disposable:Disposable? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avistamiento, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        val calendar = GregorianCalendar();
        val geocoder = Geocoder(context);
        val vmDatabase = ViewModelProviders.of(this).get(DatabaseViewModel::class.java)


        view.findViewById<EditText>(R.id.etHour).setText(calendar.get(Calendar.HOUR_OF_DAY).toString())
        view.findViewById<EditText>(R.id.etMinute).setText(calendar.get(Calendar.MINUTE).toString())
        view.findViewById<EditText>(R.id.etDay).setText(calendar.get(Calendar.DATE).toString())
        view.findViewById<EditText>(R.id.etMonth).setText(calendar.get(Calendar.MONTH).toString())
        view.findViewById<EditText>(R.id.etYear).setText(calendar.get(Calendar.YEAR).toString())

        disposable = LocationManager.getLocationObservable(view.context).subscribe { onNext ->
            run {
                view.findViewById<EditText>(R.id.etLatitud).setText(onNext.latitude.toString());
                view.findViewById<EditText>(R.id.etLongitud).setText(onNext.longitude.toString());
                try {
                    geocoder.getFromLocation(onNext.latitude, onNext.longitude, 3).forEach { address ->
                        view.findViewById<EditText>(R.id.etLugar).setText(address.adminArea);
                        view.findViewById<EditText>(R.id.etPais).setText(address.countryName)
                    }
                } catch (e: Exception) {
                    Log.e("Geocoder", "Geocoder didn't found anything");

                }
            }
        }

        view.findViewById<Button>(R.id.btnAñadirAvistamiento).clicks().subscribe { onNext ->
            vmDatabase.insertNewAnimalToDB(createAvistamientoObject(view))
            Toast.makeText(view.context, "Animal added", Toast.LENGTH_LONG).show();
        }
    }

    private fun createAvistamientoObject(view:View):AvistamientoData{
        val species = view.findViewById<EditText>( R.id.tvEspecie).text.toString();
        val place = view.findViewById<EditText>( R.id.etLugar).text.toString();
        val country = view.findViewById<EditText>( R.id.etPais).text.toString();
        val latitude = view.findViewById<EditText>(R.id.etLatitud).text.toString();
        val longitude = view.findViewById<EditText>(R.id.etLongitud).text.toString();
        val time = view.findViewById<EditText>(R.id.etHour).text.toString() +
                ":"+view.findViewById<EditText>(R.id.etMinute).text.toString();
        val date = view.findViewById<EditText>(R.id.etDay).text.toString() + "/" +
                view.findViewById<EditText>(R.id.etMonth).text.toString() +
                "/" + view.findViewById<EditText>(R.id.etYear).text.toString();
        return AvistamientoData(especie=species, date = date, latitude =  latitude.toDouble(),
             longitude = longitude.toDouble(), lugar = place, pais = country, time = time);


    }
    override fun onDestroy() {
        disposable?.dispose();
        super.onDestroy();

    }
}