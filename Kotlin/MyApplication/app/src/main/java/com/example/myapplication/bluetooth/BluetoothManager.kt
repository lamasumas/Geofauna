package com.example.myapplication.bluetooth


import android.content.Context
import android.util.Log
import android.widget.Toast
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

    private val START_TALKING_ID = '1'
    private val GPS_SENSOR_ID = '2'
    private val DISCONNECTED_ID = '0'

    private var current_sensor_id=   DISCONNECTED_ID
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
        /*disposable = getClient(context).getBleDevice(macAddress).establishConnection(false)
                .flatMap { it.setupNotification(UUID.fromString(HM10_CUSTOMCHARACTERISITCS)) }
                .flatMap { it }.observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {rxBleCharacteristics ->
                        Toast.makeText(context, String(rxBleCharacteristics), Toast.LENGTH_LONG).show()

                    },
                    { throwable ->

                    }
                )*/
        return Observable.create<String> { emiter ->
            getClient(context).getBleDevice(macAddress).establishConnection(false)
                        .subscribe {
                            connection ->
                            connection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf('1'.toByte())).subscribe{
                                onSuccess ->
                                    connection.setupNotification(HM10_CUSTOMCHARACTERISITCS)
                                            .flatMap {it}.subscribe{
                                                emiter.onNext(String(it))
                                            }


                        }

                    }






        }


    }


}