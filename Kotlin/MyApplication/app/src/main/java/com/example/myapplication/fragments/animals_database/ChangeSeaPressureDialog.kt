package com.example.myapplication.fragments.animals_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.utils.BluetoothManager
import com.example.myapplication.utils.InputValidator
import com.example.myapplication.viewmodels.TransectViewModel
import com.example.myapplication.viewmodels.controllers.BleControllerViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.change_pressure_dialog.*

class ChangeSeaPressureDialog : DialogFragment() {

    val transectViewModel: TransectViewModel by activityViewModels()
    val bleControllerViewModel: BleControllerViewModel by activityViewModels()
    val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theView = inflater.inflate(R.layout.change_pressure_dialog, container)
        val etAltitude = theView.findViewById<EditText>(R.id.etAltitudeNow)
        val etPressure = theView.findViewById<EditText>(R.id.etPRessureNow)
        val validator = InputValidator()
        bleControllerViewModel.startTalking()
        bleControllerViewModel.myData[BluetoothManager.PRESSURE_SENSOR]?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            etPressure.setText(it)
        })
        disposables.addAll(
                theView.findViewById<Button>(R.id.btnChangePressure).clicks().subscribe {
                    if (validator.isEditTextEmpty(etAltitude)) {
                        dismiss()
                    } else {

                        transectViewModel.samplingValues(etPressure.text.toString(), etAltitude.text.toString())
                        transectViewModel.selectedTransect.value?.isAltitudeSamplingSet = false
                        transectViewModel.selectedTransect.value?.areSampligDataSet = true
                        dismiss()
                    }
                },
                theView.findViewById<Button>(R.id.btnDeactivateEstimation).clicks().subscribe {
                    transectViewModel.selectedTransect.value?.isAltitudeSamplingSet = false
                    transectViewModel.selectedTransect.value?.areSampligDataSet = false
                    dismiss()
                }
        )
        return theView
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        bleControllerViewModel.stopTalking()
    }
}