package com.example.myapplication.bluetooth.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bluetooth.BleController
import com.example.myapplication.bluetooth.BluetoothManager
import io.reactivex.disposables.Disposable

class BluetoothScanDialog(context:Context): Dialog(context) {

    private var disposable:Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothAdapter= BluetoothRvAdapter(this)
        setContentView(R.layout.bluetooth_dialog)
        setCanceledOnTouchOutside(true);
        val bleController = BleController()
        val recyclerView = this.findViewById<RecyclerView>(R.id.rvBluetoothDevices).apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this.context)
            adapter =bluetoothAdapter
            visibility = View.VISIBLE
            disposable = bleController.scanDevices(context).subscribe{
                bluetoothAdapter.addScanResult(it)
            }

        }

    }

    override fun dismiss() {
        disposable?.dispose()
        super.dismiss()
    }

}