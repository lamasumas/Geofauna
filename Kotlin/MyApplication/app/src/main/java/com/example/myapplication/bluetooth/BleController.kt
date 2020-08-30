package com.example.myapplication.bluetooth

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Controller
import com.polidea.rxandroidble2.NotificationSetupMode
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.HashMap

class BleController : Controller {

    private var disposables: CompositeDisposable = CompositeDisposable()
    private val SERVICE_UUID = UUID.fromString("c953d4c6-5270-4bd9-83f4-b4d1b6a5b0da")
    private val HUMIDITY_UUID = UUID.fromString( "be478561-30c0-46db-85fc-cde641b51553")
    private val TEMPERATURE_UUID = UUID.fromString( "be478562-30c0-46db-85fc-cde641b51553")
    private val PRESSURE_UUID = UUID.fromString("be478563-30c0-46db-85fc-cde641b51553")
    private val ALTITUDE_UUID= UUID.fromString( "be478564-30c0-46db-85fc-cde641b51553")
    private val LONGITUDE_UUID= UUID.fromString( "be478565-30c0-46db-85fc-cde641b51553")
    private val LATITUDE_UUID = UUID.fromString("be478566-30c0-46db-85fc-cde641b51553")
    private val UV_UUID= UUID.fromString( "be478560-30c7-46db-85fc-cde641b51553")



    var stopConnection = false
    private var disposable: Disposable? = null

    /**
     *
     * Incializacion del hasmap con los valores mutables de los sensores
     */
    constructor() {
        myData[BluetoothManager.LATITUDE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.LONGITUDE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.HUMIDITY_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.PRESSURE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.UV_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.ALTITUDE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.TEMPERATURE_SENSOR] = MutableLiveData("")
    }

    /**
     * Crea observable con el que se escanea los alrededores buscando dispositivos bluetooth
     * @param context, contexto necesario para crear el cliente de la libreria RxAndroidBle
     * @return Observable<ScanResult>, Un observable que emite infinitamente los dispositivos encontrados
     */
    fun scanDevices(context: Context): Observable<ScanResult> {

        return RxBleClient.create(context).scanBleDevices(ScanSettings.Builder().build())
    }

    /**
     * Funcion que comienza la comunicación con la Arduino y actualiza los valores del hasmap.
     * Esta función envia al sensor hm-10 de la arduino el id establecido para cada sensor, y recibe
     * el valor de dicho sensor.
     * Esta funcion se ejecuta en bucle, pide indices de manera circulas hasta que el valor de  "stopConnection"
     * cambia, que envia un mensaje al arduino avisand de que se cierra la comunicacion. Finalmente se termina
     * el proceso con "dispose()"
     *
     * @param context, contexto para crear el cliente de la librearía RxAndroidBle
     */
    fun startTalking(context: Context) {
        disposable = RxBleClient.create(context).getBleDevice(BluetoothManager.bleDeviceMac).establishConnection(false).observeOn(Schedulers.io()).subscribe { bleConnection ->

            bleConnection.readCharacteristic(TEMPERATURE_UUID).subscribe{ byteArray ->
                myData[BluetoothManager.TEMPERATURE_SENSOR]?.value =  String(byteArray)
            }
            bleConnection.setupNotification(TEMPERATURE_UUID, NotificationSetupMode.QUICK_SETUP).flatMap{it}.observeOn(AndroidSchedulers.mainThread()).subscribe{
                myData[BluetoothManager.TEMPERATURE_SENSOR]?.value =  ByteBuffer.wrap(it).double.toString()
            }


                /*if (!stopConnection) {
                    val receivedSensorValue = String(it)
                    if (myData[currentSensorId]?.value != receivedSensorValue)
                        myData[currentSensorId]?.value = receivedSensorValue
                    currentSensorId++
                    if (currentSensorId == BluetoothManager.FINISH_ID)
                        currentSensorId = BluetoothManager.START_ID

                    disposables.add(bleConnection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(currentSensorId.toChar().toByte())).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({
                        Log.d("Bluetooth communication", "Asking HM-10 for more data")
                    }, {
                        if (it !is BleDisconnectedException)
                            throw it
                    }))
                } else {
                    disposables.add(bleConnection.writeCharacteristic(HM10_CUSTOMCHARACTERISITCS, byteArrayOf(BluetoothManager.FINISH_ID.toChar().toByte())).subscribe({
                        disposable?.dispose()
            }, {
                if (it !is BleDisconnectedException)
                    throw it
            })
        }*/

    }

}

/**
 * Función que cambia el  valor de stopConnection
 */
fun stopTalking() {
    stopConnection = true
}


}