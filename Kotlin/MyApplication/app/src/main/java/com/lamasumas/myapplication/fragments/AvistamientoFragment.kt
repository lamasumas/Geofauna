package com.lamasumas.myapplication.fragments


import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.core.view.doOnDetach
import androidx.fragment.app.activityViewModels
import com.lamasumas.myapplication.viewmodels.controllers.Controller
import com.lamasumas.myapplication.viewmodels.controllers.LocationControllerViewModel
import com.lamasumas.myapplication.R
import com.lamasumas.myapplication.viewmodels.controllers.BleControllerViewModel
import com.lamasumas.myapplication.utils.BluetoothManager
import com.lamasumas.myapplication.fragments.abstracts.AbstractDatabaseFragment
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.math.ln

class AvistamientoFragment : AbstractDatabaseFragment() {

    private val bleController: BleControllerViewModel by activityViewModels()
    private val locationController: LocationControllerViewModel by activityViewModels()
    lateinit var  autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxJavaPlugins.setErrorHandler {
            if (it is UndeliverableException) {
                System.out.println("Undelivered error")
               // Log.d("Error", "Undelivered error")
                return@setErrorHandler
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_avistamiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var calendar = GregorianCalendar()
        val checkBluetooth = BluetoothAdapter.getDefaultAdapter()
        val locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        val etMinutes = view.findViewById<EditText>(R.id.etMinute);
        val etHours = view.findViewById<EditText>(R.id.etHour)

        setGeneralButtonActions(view)

        setupSuggestions(view)


        if (bleController.macAddress != "" && checkBluetooth.isEnabled && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            bleController.startTalking()
            setCommonObserver(BluetoothManager.HUMIDITY_SENSOR, R.id.etHumidity, bleController, 2)
            setCommonObserver(BluetoothManager.TEMPERATURE_SENSOR, R.id.etTemperature, bleController,2)
            setCommonObserver(BluetoothManager.UV_SENSOR, R.id.etIndexUV, bleController,0)
            setCommonObserver(BluetoothManager.PRESSURE_SENSOR, R.id.etPressure, bleController,2)
            setAltitudeObserver()
            setArduinoLocationObserver(BluetoothManager.LATITUDE_SENSOR, view)
            setArduinoLocationObserver(BluetoothManager.LONGITUDE_SENSOR, view)
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationController.startGettingInfinitePositions()
                setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController, null)
                setCommonObserver(locationController.LATITUDE_ID, R.id.etLatitud, locationController, null)
            }
        }



        disposables.addAll(Observable.create<String> { emitter ->
            var minute = calendar.get(Calendar.MINUTE).toString()
            emitter.onNext(minute)
            while (true) {
                calendar = GregorianCalendar()
                if (minute != calendar.get(Calendar.MINUTE).toString()) {
                    minute = calendar.get(Calendar.MINUTE).toString()
                    emitter.onNext(minute)
                }
                Thread.sleep(1000)
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { etMinutes.setText(it) },
                Observable.create<String> { emitter ->
                    var minute = calendar.get(Calendar.HOUR_OF_DAY).toString()
                    emitter.onNext(minute)
                    while (true) {
                        calendar = GregorianCalendar()
                        if (minute != calendar.get(Calendar.HOUR_OF_DAY).toString()) {
                            minute = calendar.get(Calendar.HOUR_OF_DAY).toString()
                            emitter.onNext(minute)
                        }
                        Thread.sleep(1000)
                    }
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { etHours.setText(it) }
        )

        view.findViewById<EditText>(R.id.etDay).setText(calendar.get(Calendar.DATE).toString())
        view.findViewById<EditText>(R.id.etMonth).setText(calendar.get(Calendar.MONTH).toString())
        view.findViewById<EditText>(R.id.etYear).setText(calendar.get(Calendar.YEAR).toString())

    }

    private fun setAltitudeObserver() {
        val hypsometricEq: (Double, Double) -> Double? = { pressure, temperature ->

            val altitude = transectViewModel.selectedTransect.value?.altitudeSampling?.let {
                ((287.06 * (temperature + 273.15) / 9.8) * transectViewModel.selectedTransect.value?.pressureSampling?.div(pressure)?.let { x -> ln(x) }!!) + it
            }
           altitude
        }
        if (bleController.macAddress != "") {
            if (transectViewModel.selectedTransect.value?.areSampligDataSet == true) {
                bleController.myData[BluetoothManager.PRESSURE_SENSOR]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (it != "e\r\n" && !bleController.myData[BluetoothManager.TEMPERATURE_SENSOR]?.value.isNullOrEmpty() && bleController.myData[BluetoothManager.TEMPERATURE_SENSOR]?.value != "e\r\n") {
                        //Ecuación hipsométrica
                        val altitude = hypsometricEq(
                                it.toDouble()!!,
                                bleController.myData[BluetoothManager.TEMPERATURE_SENSOR]!!.value!!.toDouble())
                        requireView().findViewById<EditText>(R.id.etAltitude).setText(altitude?.toInt().toString())
                    }
                })

                bleController.myData[BluetoothManager.TEMPERATURE_SENSOR]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (it != "e\r\n" && !bleController.myData[BluetoothManager.PRESSURE_SENSOR]?.value.isNullOrEmpty() && bleController.myData[BluetoothManager.PRESSURE_SENSOR]?.value != "e\r\n") {
                        //Ecuación hipsométrica
                        val altitude = hypsometricEq(
                                bleController.myData[BluetoothManager.PRESSURE_SENSOR]!!.value!!.toDouble(),
                                it.toDouble()!!)
                        requireView().findViewById<EditText>(R.id.etAltitude).setText(altitude?.toInt().toString())
                    }
                })
            } else {
                if (transectViewModel.selectedTransect.value?.isAltitudeSamplingSet == true) {
                    requireView().findViewById<EditText>(R.id.etAltitude).setText(transectViewModel.selectedTransect.value?.altitudeSampling.toString())
                    disposables.add(requireView().findViewById<Button>(R.id.btnAñadirAvistamiento).clicks().subscribe {
                        transectViewModel.selectedTransect.value?.pressureSampling = requireView().findViewById<EditText>(R.id.etPressure).text.toString().toDouble()
                        transectViewModel.selectedTransect.value?.isAltitudeSamplingSet = false
                        transectViewModel.selectedTransect.value?.areSampligDataSet = true
                        super.btnAñadirAvistamientoAction()
                    })
                }
            }
        }
    }

    private fun setupSuggestions(view: View) {
        transectViewModel.selectedTransect.value?.aniamlList?.split(",")?.toMutableList()?.let {
            view.findViewById<AutoCompleteTextView>(R.id.etEspecie).also { tv ->
                autoCompleteTextView = tv
                if (it.isNotEmpty() && it[0]?.isNotBlank()) {
                    tv.threshold = 0
                   tv.setOnFocusChangeListener { _, _ ->
                       if(!isDetached && !isRemoving && isVisible && !requireActivity().isFinishing )
                           tv.showDropDown()
                        else{
                           tv.dismissDropDown()
                       }}
                    ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, it).also {
                        tv.setAdapter(it)
                    }
                } else {
                    tv.dismissDropDown()
                }

                tv.doOnDetach {  tv.dismissDropDown()}
            }
        }
    }

    private fun setCommonObserver(index: Int, editTextId: Int, controller: Controller, decimals: Int?) {
        controller.myData[index]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != "e\r\n")
                view?.findViewById<EditText>(editTextId)!!.setText(setNumberOfDecimals(it, decimals))
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
                setCommonObserver(locationController.LONGITUDE_ID, R.id.etLongitud, locationController, null)
                setCommonObserver(locationController.LATITUDE_ID, R.id.etLatitud, locationController, null)
            }
        })
    }

    private fun setNumberOfDecimals(number:String, decimals:Int?):String{
        if(decimals != null){
            var splittedNumber = number.split(".")
            if(decimals == 0)
                return splittedNumber[0]
            return if(splittedNumber.size == 1)
                splittedNumber[0]
            else{
                if(splittedNumber[1].length <= decimals)
                    splittedNumber[0]+"."+splittedNumber
                else{
                    splittedNumber[0]+"."+ splittedNumber[1].subSequence(0,decimals)
                }

            }

        }
        return number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (bleController.macAddress != "")
            bleController.stopTalking()
        locationController.stopGettingPositions()

    }

    override fun onDestroy() {
        super.onDestroy()
        autoCompleteTextView.dismissDropDown()
    }
}