package com.lamasumas.myapplication.fragments.animals_database.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lamasumas.myapplication.R
import com.lamasumas.myapplication.utils.BluetoothManager
import com.lamasumas.myapplication.utils.InputValidator
import com.lamasumas.myapplication.viewmodels.TransectViewModel
import com.lamasumas.myapplication.viewmodels.controllers.BleControllerViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable

class ChangeSamplePressureDialog : DialogFragment() {

    val transectViewModel: TransectViewModel by activityViewModels()
    val bleControllerViewModel: BleControllerViewModel by activityViewModels()
    val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theView = inflater.inflate(R.layout.change_pressure_dialog, container)
        val etAltitude = theView.findViewById<EditText>(R.id.etAltitudeNow)
        val etPressure = theView.findViewById<EditText>(R.id.etPRessureNow)
        val validator = InputValidator()

        etAltitude.setText(transectViewModel.selectedTransect.value?.altitudeSampling.toString())

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