package com.example.myapplication.fragments

import android.os.Bundle
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
import com.example.myapplication.room.data_classes.AvistamientoData
import com.example.myapplication.room.DatabaseRepository
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AvistamientoFragment : Fragment() {

    private var disposables = CompositeDisposable()
    private val bleController = BleController()
    private lateinit var  locationController:LocationController


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
        super.onViewCreated(view, savedInstanceState)
        val calendar = GregorianCalendar()
        locationController = LocationController(view.context)


        if(BluetoothManager.bleDeviceMac != "") {
            bleController.startTalking(view.context)
            setCommonObserver(BluetoothManager.HUMIDITY_SENSOR, R.id.etHumidity,bleController)
            setCommonObserver(BluetoothManager.ALTITUDE_SENSOR, R.id.etAltitude,bleController)
            setCommonObserver(BluetoothManager.TEMPERATURE_SENSOR, R.id.etTemperature,bleController)
            setCommonObserver(BluetoothManager.UV_SENSOR, R.id.etIndexUV,bleController)
            setCommonObserver(BluetoothManager.PRESSURE_SENSOR, R.id.etPressure,bleController)
            setArduinoLocationObserver(BluetoothManager.LATITUDE_SENSOR, view)
            setArduinoLocationObserver(BluetoothManager.LONGITUDE_SENSOR, view)
            checkPositionData(view)

        }
        else {
            locationController.startGettingInfinitePositions()
            setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController)
            setCommonObserver(locationController.LATITUDE_ID,R.id.etLatitud, locationController)
            setCommonObserver(locationController.COUNTRY_ID, R.id.etPais, locationController)
            setCommonObserver(locationController.PLACE_ID,R.id.etLugar, locationController)
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

    /**
     * If no satellite is found by the arduino, the phone gps will be use
     */
    private fun checkPositionData(view: View){
        if( view.findViewById<EditText>(R.id.etLatitud).text().toString()  == "No Data" ||
                view.findViewById<EditText>(R.id.etLongitud).text().toString() == "No Data"){
            locationController.getOneGPSPosition()
            setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController)
            setCommonObserver(locationController.LATITUDE_ID,R.id.etLatitud, locationController)
            setCommonObserver(locationController.COUNTRY_ID, R.id.etPais, locationController)
            setCommonObserver(locationController.PLACE_ID,R.id.etLugar, locationController)

        }
    }

    private fun setArduinoLocationObserver(sensorID:Int, view: View) {
        bleController.myData[sensorID]?.observe(viewLifecycleOwner,  androidx.lifecycle.Observer{
        if(bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value != "e\r\n" &&
                bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value != "e\r\n" &&
                bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value != "" &&
                bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value != ""){
            var tempLocation = locationController.translateGPS2Place(bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value?.toDouble()!!,
                    bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value?.toDouble()!!)
            view.findViewById<EditText>(R.id.etLatitud).setText(tempLocation.latitude.toString())
            view.findViewById<EditText>(R.id.etLongitud).setText(tempLocation.longitude.toString())
            view.findViewById<EditText>(R.id.etLugar).setText(tempLocation.place)
            view.findViewById<EditText>(R.id.etPais).setText(tempLocation.country)
        }
        else{
            view.findViewById<EditText>(R.id.etLatitud).setText("No Data")
            view.findViewById<EditText>(R.id.etLongitud).setText("No Data")
            view.findViewById<EditText>(R.id.etLugar).setText("No Data")
            view.findViewById<EditText>(R.id.etPais).setText("No Data")
        }})
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
        disposables.dispose()
        bleController.stopTalking()
        locationController.stopGettingPositions()
        super.onDestroy()

    }
}