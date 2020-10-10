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
import com.example.myapplication.fragments.transects.recyclerview.TransectViewHolder
import com.example.myapplication.utils.InputValidator
import com.example.myapplication.viewmodels.TransectViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable

class ChangeSeaPressureDialog : DialogFragment() {

    val transectViewModel: TransectViewModel by activityViewModels()
    val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val theView = inflater.inflate(R.layout.change_pressure_dialog, container)
        val etSeaPressure = theView.findViewById<EditText>(R.id.etPressureSeaLevel)
        etSeaPressure.setText(transectViewModel.selectedTransect.value?.pressureSeaLevel.toString())
        val validator = InputValidator()
        disposables.add(theView.findViewById<Button>(R.id.btnChangePressure).clicks().subscribe {
            if (validator.isEditTextEmpty(etSeaPressure)) {
                dismiss()
            } else {
                transectViewModel.changePressure(etSeaPressure.text.toString())
                dismiss()
            }
        })


        return theView
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}