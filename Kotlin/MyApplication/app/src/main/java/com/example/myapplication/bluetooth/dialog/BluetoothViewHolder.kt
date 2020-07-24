package com.example.myapplication.bluetooth.dialog

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.polidea.rxandroidble2.scan.ScanResult


class BluetoothViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cv: CardView
    var name: TextView
    var mac: TextView
    var ScanResult: ScanResult? = null

    init {
        cv = itemView.findViewById(R.id.cvBluetooth)
        name = itemView.findViewById(R.id.tvDeviceName)
        mac = itemView.findViewById(R.id.tvMac)
    }
}


