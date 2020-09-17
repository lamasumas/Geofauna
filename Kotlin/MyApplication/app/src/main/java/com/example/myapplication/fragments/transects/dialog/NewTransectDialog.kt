package com.example.myapplication.fragments.transects.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.R
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.Transect
import com.example.myapplication.utils.InputValidator
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.new_transect_dialog.*

class NewTransectDialog(theContext: Context) : Dialog(theContext) {
    val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_transect_dialog)
        val etTransect = findViewById<EditText>(R.id.etTransectName)
        val etCountry = findViewById<EditText>(R.id.etCountry)
        val etAnimales = findViewById<EditText>(R.id.etAnimales)
        val etLocalidad = findViewById<EditText>(R.id.etLocalidad)
        setCanceledOnTouchOutside(true);
        val validator = InputValidator()
        disposables.add(findViewById<Button>(R.id.btnStoreTransect).clicks().subscribe {
            if (validator.isEditTextEmpty(etTransectName) ||
                    validator.isEditTextEmpty(etCountry)) {
                AlertDialog.Builder(context).setTitle(R.string.alertTitleTransect)
                        .setMessage(R.string.alertMessageTransect)
                        .setNeutralButton(R.string.cerrarAlertBoton) { dialogInterface, i -> dialogInterface.dismiss() }
                        .show()
            } else {
                DatabaseRepository(context).insertNewTransect(Transect(
                        name = etTransect.text.toString(),
                        aniamlList = validator.nullOrEmpty(etAnimales.text.toString()),
                        country = etCountry.text.toString(),
                        locality = validator.nullOrEmpty(etLocalidad.text.toString()))
                )
                this.dismiss()
            }
        })
    }

    override fun dismiss() {
        disposables.dispose()
        super.dismiss()
    }
}