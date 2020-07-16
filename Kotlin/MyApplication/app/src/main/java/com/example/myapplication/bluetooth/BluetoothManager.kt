package com.example.myapplication.bluetooth


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

object BluetoothManager{

    private  var  rxBleClient:RxBleClient? = null
    private fun getClient(context: Context): RxBleClient{
        if(rxBleClient == null)
            rxBleClient = RxBleClient.create(context)
        return rxBleClient!!;
    }

    fun scanDevices(context: Context): Observable<ScanResult> {
         return getClient(context).scanBleDevices(ScanSettings.Builder().build())


    }


}