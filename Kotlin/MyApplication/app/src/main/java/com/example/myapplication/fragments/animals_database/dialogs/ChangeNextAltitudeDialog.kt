package com.example.myapplication.fragments.animals_database.dialogs

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


class ChangeNextAltitudeDialog : DialogFragment() {

    val transectViewModel: TransectViewModel by activityViewModels()
    val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theView = inflater.inflate(R.layout.change_next_altitude_dialog, container)
        val etAltitude = theView.findViewById<EditText>(R.id.etAltitudeNow)
        val validator = InputValidator()
        disposables.addAll(
                theView.findViewById<Button>(R.id.btnChangePressure).clicks().subscribe {
                    if (validator.isEditTextEmpty(etAltitude)) {
                        dismiss()
                    } else {
                        transectViewModel.samplingValues(null, etAltitude.text.toString())
                        transectViewModel.selectedTransect.value?.isAltitudeSamplingSet = true
                        transectViewModel.selectedTransect.value?.areSampligDataSet = false
                        dismiss()
                    }
                },
                theView.findViewById<Button>(R.id.btnDeactivateEstimationAltura).clicks().subscribe {
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
    }
}