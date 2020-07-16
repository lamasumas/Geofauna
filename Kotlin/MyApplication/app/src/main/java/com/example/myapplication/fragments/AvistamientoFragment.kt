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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.myapplication.LocationManager
import com.example.myapplication.R
import com.example.myapplication.room.data_classes.AvistamientoData
import com.example.myapplication.room.DatabaseRepository
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class AvistamientoFragment : Fragment() {

    private var disposable:Disposable? = null;

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
        data class  MylocationObject(var longitude:Double, var latitude:Double, var country: String, var place:String);

        disposable = LocationManager.getLocationObservable(view.context).observeOn(Schedulers.io()).flatMap {
            val mylocationObject = MylocationObject(it.longitude, it.latitude, "Not Found", "Not Found")
            try {
                geocoder.getFromLocation(it.latitude, it.longitude, 3).forEach { address ->
                    mylocationObject.country = address.countryName;
                    mylocationObject.place = address.adminArea;
                }
            } catch (e: Exception) {
                    //Log.e("Geocoder", "Geocoder didn't found anything");
            }
            return@flatMap Observable.just(mylocationObject)

            }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                view.findViewById<EditText>(R.id.etLatitud).setText(it.latitude.toString());
                view.findViewById<EditText>(R.id.etLongitud).setText(it.longitude.toString());
                view.findViewById<EditText>(R.id.etLugar).setText(it.place);
                view.findViewById<EditText>(R.id.etPais).setText(it.country)
            }



        view.findViewById<Button>(R.id.btnAñadirAvistamiento).clicks().subscribe {
            val newDatabaseEntry = createAvistamientoObject(view)
            val databaseRepository =  DatabaseRepository(view.context);
            databaseRepository.insertNewAnimalToDB(newDatabaseEntry)
            view.findNavController().navigate(AvistamientoFragmentDirections.actionAvistamiento2ToMainFragment2())
        }
    }

    private fun createAvistamientoObject(view:View):AvistamientoData{
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
        return AvistamientoData(especie=species, date = date, latitude =  latitude.toDouble(),
             longitude = longitude.toDouble(), lugar = place, pais = country, time = time);


    }
    override fun onDestroy() {
        disposable?.dispose();
        super.onDestroy();

    }
}