package com.example.myapplication.fragments

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.example.myapplication.Controller
import com.example.myapplication.LocationController
import com.example.myapplication.R
import com.example.myapplication.bluetooth.BleController
import com.example.myapplication.bluetooth.BluetoothManager
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.util.*

class AvistamientoFragment : Fragment() {

    private var disposables = CompositeDisposable()
    private val bleController = BleController()
    private lateinit var  locationController:LocationController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxJavaPlugins.setErrorHandler {
            if( it is  UndeliverableException){
              Log.d("Error", "Undelivered error")
                return@setErrorHandler
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avistamiento, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = GregorianCalendar()
        locationController = LocationController(view.context)
        val checkBluetooth = BluetoothAdapter.getDefaultAdapter()
        val locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager;

        if(BluetoothManager.bleDeviceMac != "" && checkBluetooth.isEnabled &&  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            bleController.startTalking(view.context)
            setCommonObserver(BluetoothManager.HUMIDITY_SENSOR, R.id.etHumidity,bleController)
            setCommonObserver(BluetoothManager.ALTITUDE_SENSOR, R.id.etAltitude,bleController)
            setCommonObserver(BluetoothManager.TEMPERATURE_SENSOR, R.id.etTemperature,bleController)
            setCommonObserver(BluetoothManager.UV_SENSOR, R.id.etIndexUV,bleController)
            setCommonObserver(BluetoothManager.PRESSURE_SENSOR, R.id.etPressure,bleController)
            setArduinoLocationObserver(BluetoothManager.LATITUDE_SENSOR, view)
            setArduinoLocationObserver(BluetoothManager.LONGITUDE_SENSOR, view)

        }
        else {
            if( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                locationController.startGettingInfinitePositions()
                setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController)
                setCommonObserver(locationController.LATITUDE_ID, R.id.etLatitud, locationController)
                setCommonObserver(locationController.COUNTRY_ID, R.id.etPais, locationController)
                setCommonObserver(locationController.PLACE_ID, R.id.etLugar, locationController)
            }
        }

        view.findViewById<EditText>(R.id.etHour).setText(calendar.get(Calendar.HOUR_OF_DAY).toString())
        view.findViewById<EditText>(R.id.etMinute).setText(calendar.get(Calendar.MINUTE).toString())
        view.findViewById<EditText>(R.id.etDay).setText(calendar.get(Calendar.DATE).toString())
        view.findViewById<EditText>(R.id.etMonth).setText(calendar.get(Calendar.MONTH).toString())
        view.findViewById<EditText>(R.id.etYear).setText(calendar.get(Calendar.YEAR).toString())




        view.findViewById<Button>(R.id.btnAñadirAvistamiento).clicks().subscribe {
            val newDatabaseEntry = createAvistamientoObject(view)
            val databaseRepository =  DatabaseRepository(view.context);
            databaseRepository.insertNewAnimalToDB(newDatabaseEntry)
            view.findNavController().navigate(AvistamientoFragmentDirections.actionAvistamiento2ToMainFragment2())
            if(BluetoothManager.bleDeviceMac != ""){
                bleController.stopTalking();
            }
            locationController.stopGettingPositions()
        }
    }


    private fun setCommonObserver(index:Int, editTextId:Int, controller: Controller){
        controller.myData[index]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != "e\r\n")
                view?.findViewById<EditText>(editTextId)!!.setText(it)
            else
                view?.findViewById<EditText>(editTextId)!!.setText("No Data") })


    }

    private fun setArduinoLocationObserver(sensorID:Int, view: View) {
        bleController.myData[sensorID]?.observe(viewLifecycleOwner,  androidx.lifecycle.Observer{
        if(bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value != "e\r\n" &&
                bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value != "e\r\n" &&
                bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value != "" &&
                bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value != ""){
            locationController.stopGettingPositions()
            var tempLocation = locationController.translateGPS2Place(bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value?.toDouble()!!,
                    bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value?.toDouble()!!)
            view.findViewById<EditText>(R.id.etLatitud).setText(tempLocation.latitude.toString())
            view.findViewById<EditText>(R.id.etLongitud).setText(tempLocation.longitude.toString())
            view.findViewById<EditText>(R.id.etLugar).setText(tempLocation.place)
            view.findViewById<EditText>(R.id.etPais).setText(tempLocation.country)
        }
        else{
            locationController.startGettingInfinitePositions()
            setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController)
            setCommonObserver(locationController.LATITUDE_ID,R.id.etLatitud, locationController)
            setCommonObserver(locationController.COUNTRY_ID, R.id.etPais, locationController)
            setCommonObserver(locationController.PLACE_ID,R.id.etLugar, locationController)
        }})
    }

    private fun createAvistamientoObject(view:View):AnimalSimpleData{
        val species = view.findViewById<EditText>( R.id.etEspecie).text.toString()
        val place = view.findViewById<EditText>( R.id.etLugar).text.toString()
        val country = view.findViewById<EditText>( R.id.etPais).text.toString()
        val latitude = view.findViewById<EditText>(R.id.etLatitud).text.toString()
        val longitude = view.findViewById<EditText>(R.id.etLongitud).text.toString()
        val time = view.findViewById<EditText>(R.id.etHour).text.toString() +
                ":"+view.findViewById<EditText>(R.id.etMinute).text.toString()
        val date = view.findViewById<EditText>(R.id.etDay).text.toString() + "/" +
                view.findViewById<EditText>(R.id.etMonth).text.toString() +
                "/" + view.findViewById<EditText>(R.id.etYear).text.toString()
        val humidity = view.findViewById<EditText>(R.id.etHumidity).toString()
        val altitude = view.findViewById<EditText>(R.id.etAltitude).toString()
        val uv = view.findViewById<EditText>(R.id.etIndexUV).toString()
        val temperature = view.findViewById<EditText>(R.id.etTemperature).toString()
        val pressure = view.findViewById<EditText>(R.id.etPressure).toString()


        return AnimalSimpleData(especie=species, date = date, latitude =  latitude.toDouble(),
             longitude = longitude.toDouble(), time = time)


    }
    override fun onDestroy() {
        disposables.dispose()
        bleController.stopTalking()
        locationController.stopGettingPositions()
        super.onDestroy()

    }
}