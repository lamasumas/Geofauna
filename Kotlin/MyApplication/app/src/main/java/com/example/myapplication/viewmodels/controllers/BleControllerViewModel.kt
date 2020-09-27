package com.example.myapplication.viewmodels.controllers

import android.app.Application
import android.os.ParcelUuid
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.bluetooth.BluetoothManager
import com.example.myapplication.viewmodels.GeneralViewModel
import com.polidea.rxandroidble2.NotificationSetupMode
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class BleControllerViewModel(application: Application) : Controller(application), GeneralViewModel {

    private var disposables: CompositeDisposable = CompositeDisposable()
    //Todas las UUIDs necesarias
    private val SERVICE_UUID = ParcelUuid.fromString("c953d4c6-5270-4bd9-83f4-b4d1b6a5b0da")
    private val HUMIDITY_UUID = UUID.fromString("be478561-30c0-46db-85fc-cde641b51553")
    private val TEMPERATURE_UUID = UUID.fromString("be478562-30c0-46db-85fc-cde641b51553")
    private val PRESSURE_UUID = UUID.fromString("be478563-30c0-46db-85fc-cde641b51553")
    private val ALTITUDE_UUID = UUID.fromString("be478564-30c0-46db-85fc-cde641b51553")
    private val LONGITUDE_UUID = UUID.fromString("be478565-30c0-46db-85fc-cde641b51553")
    private val LATITUDE_UUID = UUID.fromString("be478566-30c0-46db-85fc-cde641b51553")
    private val UV_UUID = UUID.fromString("be478560-30c7-46db-85fc-cde641b51553")


    var stopConnection = false
    private var disposable: Disposable? = null

    /**
     *
     * Incializacion del hasmap con los valores mutables de los sensores
     */
    init {
        myData[BluetoothManager.LATITUDE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.LONGITUDE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.HUMIDITY_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.PRESSURE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.UV_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.ALTITUDE_SENSOR] = MutableLiveData("")
        myData[BluetoothManager.TEMPERATURE_SENSOR] = MutableLiveData("")
    }

    /**
     * Crea un observable que escanea los alredores buscando un dispositivo bluetooth con una uuid de
     * servicio concreta, es decir, la que hemos creado nosotros.
     * @return Observable<ScanResult>, Un observable que emite un ScanResul
     */
    fun scanDevices(): Observable<ScanResult> {

        return RxBleClient.create(getApplication()).scanBleDevices(ScanSettings.Builder().build(),
                ScanFilter.Builder()
                        .setServiceUuid(SERVICE_UUID)
                        .build()).take(1)
    }

    /**
     * Esta función da comencio a la comunicación entre el dispositivo android y el del ble,
     * aqui se configura todas las caracteristivas del dispositivo ble.
     */
    fun startTalking() {
        disposable = RxBleClient.create(getApplication()).getBleDevice(BluetoothManager.bleDeviceMac).establishConnection(false).observeOn(Schedulers.io()).subscribe { bleConnection ->
            setupCharacteristic(TEMPERATURE_UUID, BluetoothManager.TEMPERATURE_SENSOR, bleConnection)
            setupCharacteristic(HUMIDITY_UUID, BluetoothManager.HUMIDITY_SENSOR, bleConnection)
            setupCharacteristic(UV_UUID, BluetoothManager.UV_SENSOR, bleConnection)
            setupCharacteristic(LATITUDE_UUID, BluetoothManager.LATITUDE_SENSOR, bleConnection)
            setupCharacteristic(LONGITUDE_UUID, BluetoothManager.LONGITUDE_SENSOR, bleConnection)
            setupCharacteristic(ALTITUDE_UUID, BluetoothManager.ALTITUDE_SENSOR, bleConnection)
            setupCharacteristic(PRESSURE_UUID, BluetoothManager.PRESSURE_SENSOR, bleConnection)
        }

    }

    /**
     * Esta función configura una caracteristica concreta del microcontrolador
     * @param bleConnection, conection con el dispositivio ble
     * @param characteristicUUID, UUID de la caracteristica
     * @param sensorPosition, key del hasmap donde estan todos los valores de los sensores
     */
    private fun setupCharacteristic(characteristicUUID: UUID, sensorPosition: Int, bleConnection: RxBleConnection) {
        disposables.add(bleConnection.readCharacteristic(characteristicUUID).observeOn(AndroidSchedulers.mainThread()).subscribe { byteArray ->
            myData[sensorPosition]?.value = String(byteArray)
            bleConnection.setupNotification(characteristicUUID, NotificationSetupMode.COMPAT).flatMap { it }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                myData[sensorPosition]?.value = String(it)
            }
        }
        )
    }

    /**
     * Funcion que termina la conexión
     */
    fun stopTalking() {
        disposables.dispose()
        disposable?.dispose()
    }
    override fun preStart(){
        Log.d("Initialization", "BleController startted")
    }


}