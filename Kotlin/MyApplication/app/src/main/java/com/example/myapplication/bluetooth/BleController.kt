package com.example.myapplication.bluetooth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polidea.rxandroidble2.NotificationSetupMode
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import kotlin.collections.HashMap

class BleController{

    private var disposables:CompositeDisposable = CompositeDisposable()
    private val HM10_CUSTOMCHARACTERISITCS =  UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
    private var currentSensorId= BluetoothManager.START_ID
    val sensorData:HashMap<Int, MutableLiveData<String>> = HashMap()
    var stopConnection = false
    private var disposable:Disposable? = null

    constructor(){
        sensorData[BluetoothManager.LATITUDE_SENSOR] = MutableLiveData("")
        sensorData[BluetoothManager.LONGITUDE_SENSOR] = MutableLiveData("")
        sensorData[BluetoothManager.HUMIDITY_SENSOR] = MutableLiveData("")
        sensorData[BluetoothManager.PRESSURE_SENSOR] = MutableLiveData("")
        sensorData[BluetoothManager.UV_SENSOR] = MutableLiveData("")
        sensorData[BluetoothManager.ALTITUDE_SENSOR] = MutableLiveData("")
        sensorData[BluetoothManager.TEMPERATURE_SENSOR] = MutableLiveData("")

    }
    fun scanDevices(context: Context):Observable<ScanResult>{
        return RxBleClient.create(context).scanBleDevices(ScanSettings.Builder().build())
    }

    fun startTalking(context: Context) {
        var bleDevice = RxBleClient.create(context).getBleDevice(BluetoothManager.bleDeviceMac)

        disposable =  bleDevice.establishConnection(false).subscribe { bleConnection ->

                    bleConnection.setupNotification(HM10_CUSTOMCHARACTERISITCS, NotificationSetupMode.QUICK_SETUP)
                    .flatMap {
                        bleConnection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(currentSensorId.toChar().toByte())).subscribe { onSuccess ->
                            Log.d("Bluetooth communication", "Starting bluetooth communication ")
                        }
                        it
                    }.observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                if(!stopConnection ) {
                                    val receivedSensorValue = String(it)
                                    if (sensorData[currentSensorId]?.value != receivedSensorValue)
                                        sensorData[currentSensorId]?.value = receivedSensorValue
                                    currentSensorId++
                                    if (currentSensorId == BluetoothManager.FINISH_ID)
                                        currentSensorId = BluetoothManager.START_ID

                                    disposables.add(bleConnection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(currentSensorId.toChar().toByte())).subscribe { onSuccess ->
                                        Log.d("Bluetooth communication", "Asking HM-10 for more data")
                                    })
                                }else{
                                    disposables.add(bleConnection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(BluetoothManager.FINISH_ID.toChar().toByte())).subscribe { onSuccess ->
                                       disposable?.dispose()
                                    })
                                }

                    }, {
                                if (it is BleDisconnectedException)
                                {

                                }
                                else{
                                    throw it 
                                }
                            })
        }

    }

    fun stopTalking(){
        stopConnection = true
    }



}