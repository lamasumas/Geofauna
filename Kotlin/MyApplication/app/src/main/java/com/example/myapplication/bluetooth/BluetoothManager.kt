package com.example.myapplication.bluetooth


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.polidea.rxandroidble2.NotificationSetupMode
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

object BluetoothManager{

    val START_ID = 0
    val LATITUDE_SENSOR = 0
    val LONGITUDE_SENSOR = 1
    val ALTITUDE_SENSOR = 2
    val HUMIDITY_SENSOR = 3
    val UV_SENSOR = 4
    val PRESSURE_SENSOR = 5
    val TEMPERATURE_SENSOR = 6
    val FINISH_ID = 7

    var current_sensor_id=   START_ID
    var isConnectedToDevice = false
    private var  rxBleClient:RxBleClient? = null
    private var bleDevice: RxBleDevice? = null
    private val HM10_CUSTOMCHARACTERISITCS =  UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
    private var disposable:Disposable? = null


    private fun getClient(context: Context): RxBleClient{
        if(rxBleClient == null)
            rxBleClient = RxBleClient.create(context)
        return rxBleClient!!;
    }

    fun scanDevices(context: Context): Observable<ScanResult> {
         return getClient(context).scanBleDevices(ScanSettings.Builder().build() )
    }

    fun startTalking(macAddress: String, context:Context):Observable<String>{

        return Observable.create<String> { emiter ->
            getClient(context).getBleDevice(macAddress).establishConnection(false)
                        .subscribe {
                            connection ->
                            connection.setupNotification(HM10_CUSTOMCHARACTERISITCS, NotificationSetupMode.QUICK_SETUP)
                                    .flatMap {
                                        current_sensor_id = START_ID
                                        connection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(current_sensor_id.toChar().toByte())).subscribe {
                                        onSuccess -> Log.d("Bluetooth communication", "Starting bluetooth communication ")
                                    }
                                        it }.subscribe {
                                        emiter.onNext(String(it))
                                        current_sensor_id++
                                        connection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(current_sensor_id.toChar().toByte())).subscribe {
                                            onSuccess -> Log.d("Bluetooth communication", "Asking HM-10 for more data")
                                        }
                                    }
                            }

                        }

                    }












}