package com.example.myapplication

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.export.ExportManager
import com.example.myapplication.viewmodels.AnimalDatabaseViewModel
import com.example.myapplication.viewmodels.TransectViewModel
import com.example.myapplication.viewmodels.controllers.BleControllerViewModel
import com.example.myapplication.viewmodels.controllers.LocationControllerViewModel
import io.reactivex.plugins.RxJavaPlugins

class MainActivity : AppCompatActivity() {

    private val animalDatabaseViewModel: AnimalDatabaseViewModel by viewModels()
    private val locationControllerViewModel: LocationControllerViewModel by viewModels()
    private val bleControllerViewModel: BleControllerViewModel by viewModels()
    private val transectViewModel: TransectViewModel by viewModels()
    private val isAlreadyPreStarted = false
    companion object {
        const val GPS_PERMISION_CODE = 55
        const val BLUETOOTH_CODE = 56
        const val EXPORT_CODE = 57

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animalDatabaseViewModel.preStart()
        transectViewModel.preStart()
        bleControllerViewModel.preStart()
        locationControllerViewModel.preStart()

        if(!transectViewModel.prestarted) {
            Thread.sleep(1500)
            transectViewModel.prestarted = true
        }
        
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        getPermissions()
        RxJavaPlugins.setErrorHandler {t ->
            Log.d("error in outer thread",null, t);
        };

    }


    private fun getPermissions() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf<String>(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE), GPS_PERMISION_CODE);

        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

       val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BLUETOOTH_CODE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            BLUETOOTH_CODE -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    val builder = AlertDialog.Builder(this);
                    builder.setTitle(R.string.bluetoothDenegadoTitulo);
                    builder.setMessage(R.string.bluetoothDenegadoDescripcion)
                    builder.setPositiveButton(R.string.cerrarAlertBoton) { dialog, _ ->
                        dialog.dismiss()
                    };
                    builder.create().show()
                }
            }

            EXPORT_CODE -> {
                if (resultCode == Activity.RESULT_OK)
                    ExportManager().exportToCSV(data?.data, this, animalDatabaseViewModel.dataList.value)
            }
        }
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GPS_PERMISION_CODE -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED
                                || grantResults[2] == PackageManager.PERMISSION_DENIED || grantResults[3] == PackageManager.PERMISSION_DENIED)) {
                    val builder = AlertDialog.Builder(this);
                    builder.setTitle(R.string.permisosDenegadosTitulo);
                    builder.setMessage(R.string.permisosDenegadosDescripcion)
                    builder.setPositiveButton(R.string.cerrarAlertBoton) { dialog, _ ->
                        getPermissions()
                        dialog.dismiss()
                    };
                    builder.create().show()
                }
            }

        }
    }


}