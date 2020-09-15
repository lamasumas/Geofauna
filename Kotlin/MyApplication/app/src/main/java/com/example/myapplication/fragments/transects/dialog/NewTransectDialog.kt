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

class NewTransectDialog(theContext: Context) : Dialog(theContext) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_transect_dialog)
        setCanceledOnTouchOutside(true);
        val validator = InputValidator()
        findViewById<Button>(R.id.btnStoreTransect).clicks().subscribe {
            if (validator.isEditTextEmpty(findViewById(R.id.etTransectName)) ||
                    validator.isEditTextEmpty(findViewById(R.id.etCountry))) {
                AlertDialog.Builder(context).setTitle(R.string.alertTitleTransect)
                        .setMessage(R.string.alertMessageTransect)
                        .setNeutralButton(R.string.cerrarAlertBoton){dialogInterface, i -> dialogInterface.dismiss() }
            } else {
                DatabaseRepository(context).insertNewTransect(Transect(
                        name = findViewById<EditText>(R.id.etTransectName).text.toString(),
                        aniamlList = validator.nullOrEmpty(findViewById<EditText>(R.id.etAnimales).toString()),
                        country = findViewById<EditText>(R.id.etCountry).text.toString(),
                        locality = findViewById<EditText>(R.id.etLocalidad).text.toString())
                )
            }
        }
    }

}