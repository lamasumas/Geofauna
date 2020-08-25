package com.example.myapplication.bluetooth.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bluetooth.BleController
import com.example.myapplication.bluetooth.BluetoothManager
import com.jakewharton.rxbinding2.view.clicks
import com.polidea.rxandroidble2.scan.ScanResult


class BluetoothRvAdapter(val dialog: BluetoothScanDialog): RecyclerView.Adapter<BluetoothViewHolder>() {

    private val devices = mutableListOf<ScanResult>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothViewHolder {
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_card_scheme, parent,false)
        return BluetoothViewHolder(theView)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: BluetoothViewHolder, position: Int) {
        holder.name.text = devices[position].bleDevice.name
        holder.mac.text = devices[position].bleDevice.macAddress
        holder.ScanResult = devices[position]

        holder.cv.findViewById<Button>(R.id.btnConnect).clicks().subscribe {
            BluetoothManager.bleDeviceMac = holder.mac.text.toString()
            dialog.dismiss()

        }
    }

    fun addScanResult(scanResult: ScanResult){
        if(scanResult.bleDevice.macAddress != null) {
            devices.withIndex().firstOrNull { it.value.bleDevice == scanResult.bleDevice }?.let {
                devices[it.index] = scanResult
            } ?: run {
                devices.add(scanResult)
            }
            notifyDataSetChanged()
        }

    }

}