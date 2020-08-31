package com.example.myapplication.bluetooth


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.polidea.rxandroidble2.NotificationSetupMode
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.*

object BluetoothManager{

    val LATITUDE_SENSOR = 0
    val LONGITUDE_SENSOR = 1
    val ALTITUDE_SENSOR = 2
    val HUMIDITY_SENSOR = 3
    val UV_SENSOR = 4
    val PRESSURE_SENSOR = 5
    val TEMPERATURE_SENSOR = 6
    val FINISH_ID = 7
    var bleDeviceMac:String= ""



}