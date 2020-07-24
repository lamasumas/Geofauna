package com.example.myapplication.bluetooth.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bluetooth.BluetoothManager

class BluetoothScanDialog(context:Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothAdapter= BluetoothRvAdapter(this)
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