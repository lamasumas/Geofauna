package com.example.myapplication.fragments


import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.example.myapplication.viewmodels.controllers.Controller
import com.example.myapplication.viewmodels.controllers.LocationControllerViewModel
import com.example.myapplication.R
import com.example.myapplication.viewmodels.controllers.BleControllerViewModel
import com.example.myapplication.bluetooth.BluetoothManager
import com.example.myapplication.fragments.abstracts.AbstractDatabaseFragment
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.util.*

class AvistamientoFragment : AbstractDatabaseFragment() {

    private val bleController: BleControllerViewModel by activityViewModels()
    private val locationController: LocationControllerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxJavaPlugins.setErrorHandler {
            if (it is UndeliverableException) {
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
        val checkBluetooth = BluetoothAdapter.getDefaultAdapter()
        val locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        setGeneralButtonActions(view)

        setupSuggestions(view)


        if (BluetoothManager.bleDeviceMac != "" && checkBluetooth.isEnabled && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            bleController.startTalking()
            setCommonObserver(BluetoothManager.HUMIDITY_SENSOR, R.id.etHumidity, bleController)
            setCommonObserver(BluetoothManager.ALTITUDE_SENSOR, R.id.etAltitude, bleController)
            setCommonObserver(BluetoothManager.TEMPERATURE_SENSOR, R.id.etTemperature, bleController)
            setCommonObserver(BluetoothManager.UV_SENSOR, R.id.etIndexUV, bleController)
            setCommonObserver(BluetoothManager.PRESSURE_SENSOR, R.id.etPressure, bleController)
            setArduinoLocationObserver(BluetoothManager.LATITUDE_SENSOR, view)
            setArduinoLocationObserver(BluetoothManager.LONGITUDE_SENSOR, view)
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationController.startGettingInfinitePositions()
                setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController)
                setCommonObserver(locationController.LATITUDE_ID, R.id.etLatitud, locationController)
            }
        }

        view.findViewById<EditText>(R.id.etHour).setText(calendar.get(Calendar.HOUR_OF_DAY).toString())
        view.findViewById<EditText>(R.id.etMinute).setText(calendar.get(Calendar.MINUTE).toString())
        view.findViewById<EditText>(R.id.etDay).setText(calendar.get(Calendar.DATE).toString())
        view.findViewById<EditText>(R.id.etMonth).setText(calendar.get(Calendar.MONTH).toString())
        view.findViewById<EditText>(R.id.etYear).setText(calendar.get(Calendar.YEAR).toString())


    }

    private fun setupSuggestions(view: View) {
        transectViewModel.selectedTransect.value?.aniamlList?.split(",")?.toMutableList()?.let {
            view.findViewById<AutoCompleteTextView>(R.id.etEspecie).also { tv ->
                if (it.isNotEmpty() && it[0]?.isNotBlank()) {
                    tv.threshold = 0
                    tv.setOnFocusChangeListener { _,_-> tv.showDropDown()  }
                    ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, it).also {
                        tv.setAdapter(it)
                    }
                } else {
                    tv.dismissDropDown()
                }
            }
        }
    }

    private fun setCommonObserver(index: Int, editTextId: Int, controller: Controller) {
        controller.myData[index]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != "e\r\n")
                view?.findViewById<EditText>(editTextId)!!.setText(it)
            else
                view?.findViewById<EditText>(editTextId)!!.setText("No Data")
        })
    }

    private fun setArduinoLocationObserver(sensorID: Int, view: View) {
        bleController.myData[sensorID]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value != "e" &&
                    bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value != "e" &&
                    bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value != "" &&
                    bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value != "") {
                locationController.stopGettingPositions()
                var tempLocation = locationController.translateGPS2Place(bleController.myData[BluetoothManager.LATITUDE_SENSOR]?.value?.toDouble()!!,
                        bleController.myData[BluetoothManager.LONGITUDE_SENSOR]?.value?.toDouble()!!)
                view.findViewById<EditText>(R.id.etLatitud).setText(tempLocation.latitude.toString())
                view.findViewById<EditText>(R.id.etLongitud).setText(tempLocation.longitude.toString())
            } else {
                locationController.startGettingInfinitePositions()
                setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController)
                setCommonObserver(locationController.LATITUDE_ID, R.id.etLatitud, locationController)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (BluetoothManager.bleDeviceMac != "")
            bleController.stopTalking()
        locationController.stopGettingPositions()
    }
}