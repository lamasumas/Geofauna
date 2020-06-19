package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomappbar.BottomAppBar
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    companion object{
        const val GPS_PERMISION_CODE = 55;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermissions()

    }



    private fun getPermissions(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_DENIED )
            ActivityCompat.requestPermissions(this, arrayOf<String>(android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), GPS_PERMISION_CODE);

        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            GPS_PERMISION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    val builder = AlertDialog.Builder(this);
                    builder.setTitle(R.string.permisosDenegadosTitulo);
                    builder.setMessage(R.string.permisosDenegadosDescripcion);
                    builder.setPositiveButton(R.string.cerrarAlertBoton) { dialog, _ ->
                        getPermissions()
                        dialog.dismiss()
                    };
                    builder.create().show();
                }
            }
        }

    }
}