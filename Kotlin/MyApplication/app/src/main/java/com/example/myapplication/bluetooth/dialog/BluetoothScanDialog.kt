package com.example.myapplication.bluetooth.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.BluetoothRvAdapter
import com.example.myapplication.adapters.DatabaseRvAdapter
import com.example.myapplication.bluetooth.BluetoothManager
import com.example.myapplication.room.DatabaseRepository
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BluetoothScanDialog(context:Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothAdapter=  BluetoothRvAdapter( this)
        setContentView(R.layout.bluetooth_dialog)
        val recyclerView = this.findViewById<RecyclerView>(R.id.rvBluetoothDevices).apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this.context)
            adapter =bluetoothAdapter
            visibility = View.VISIBLE
             BluetoothManager.scanDevices(context).subscribe{
                    bluetoothAdapter.addScanResult(it)
            }
        }

    }
}