package com.example.myapplication.fragments

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.myapplication.LocationManager
import com.example.myapplication.R
import io.reactivex.rxjava3.disposables.Disposable
import java.io.IOException
import java.lang.Error
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

        view.findViewById<EditText>(R.id.etHour).setText(calendar.get(Calendar.HOUR_OF_DAY).toString())
        view.findViewById<EditText>(R.id.etMinute).setText(calendar.get(Calendar.MINUTE).toString())
        view.findViewById<EditText>(R.id.etDay).setText(calendar.get(Calendar.DATE).toString())
        view.findViewById<EditText>(R.id.etMonth).setText(calendar.get(Calendar.MONTH).toString())
        view.findViewById<EditText>(R.id.etYear).setText(calendar.get(Calendar.YEAR).toString())

         disposable = LocationManager.getLocationObservable(view.context)?.subscribe{
                onNext->  run  {
            view.findViewById<EditText>(R.id.etLatitud).setText(onNext.latitude.toString());
            view.findViewById<EditText>(R.id.etLongitud).setText( onNext.longitude.toString());
             try {
                 geocoder.getFromLocation(onNext.latitude, onNext.longitude, 3).forEach { address ->
                     view.findViewById<EditText>(R.id.etLugar).setText(address.adminArea);
                     view.findViewById<EditText>(R.id.etPais).setText(address.countryName)
                 }
             }catch (e: Exception){
                    Log.e("Geocoder", "Geocoder didn't found anything");

             }
        }
        }


    }

    override fun onDestroy() {
        disposable?.dispose();
        super.onDestroy();

    }
}