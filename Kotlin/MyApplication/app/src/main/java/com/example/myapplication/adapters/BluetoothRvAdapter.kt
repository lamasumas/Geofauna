package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.AnimalViewHolder
import com.example.myapplication.BluetoothViewHolder
import com.example.myapplication.R
import com.example.myapplication.bluetooth.BluetoothManager
import com.example.myapplication.bluetooth.dialog.BluetoothScanDialog
import com.example.myapplication.fragments.MainFragmentDirections
import com.example.myapplication.room.DatabaseRepository
import com.jakewharton.rxbinding2.view.clicks
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


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

        holder.cv.clicks().subscribe {

                BluetoothManager.connectToDevice(devices[position].bleDevice.macAddress, holder.itemView.context)
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