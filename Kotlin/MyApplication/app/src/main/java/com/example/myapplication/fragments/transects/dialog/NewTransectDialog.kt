package com.example.myapplication.fragments.transects.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.Transect
import com.example.myapplication.utils.InputValidator
import com.example.myapplication.viewmodels.TransectViewModel
import com.example.myapplication.viewmodels.controllers.LocationControllerViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.new_transect_dialog.*

class NewTransectDialog() : DialogFragment() {

    val disposables = CompositeDisposable()

    private val transectViewModel: TransectViewModel by activityViewModels()
    private val locationController: LocationControllerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mainView = inflater.inflate(R.layout.new_transect_dialog, container)
        val etTransect = mainView.findViewById<EditText>(R.id.etTransectName)
        val etCountry =  mainView.findViewById<EditText>(R.id.etCountry)
        val etAnimales = mainView.findViewById<EditText>(R.id.etAnimales)
        val etLocalidad = mainView.findViewById<EditText>(R.id.etLocalidad)

        val validator = InputValidator()

        (mainView.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).also {
            if(it.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationController.getOneGPSPosition().subscribe { location ->
                    mainView.findViewById<EditText>(R.id.etCountry).setText(location.country)
                    mainView.findViewById<EditText>(R.id.etLocalidad).setText(location.place)
                }
            }
        }

        disposables.add(mainView.findViewById<Button>(R.id.btnStoreTransect).clicks().subscribe {
            if (validator.isEditTextEmpty(etTransectName)) {
                AlertDialog.Builder(context).setTitle(R.string.alertTitleTransect)
                        .setMessage(R.string.alertMessageTransect)
                        .setNeutralButton(R.string.cerrarAlertBoton) { dialogInterface, i -> dialogInterface.dismiss() }
                        .show()
            } else {
                transectViewModel.addTransect(Transect(
                        name = etTransect.text.toString(),
                        aniamlList = validator.nullOrEmpty(etAnimales.text.toString()),
                        country = validator.nullOrEmpty(etCountry.text.toString()),
                        locality = validator.nullOrEmpty(etLocalidad.text.toString()) )
                )
                this.dismiss()
            }
        })
        return mainView
    }

    override fun onDestroyView() {
        disposables.dispose()
        super.onDestroyView()
    }

    override fun dismiss() {
        disposables.dispose()
        super.dismiss()
    }
}