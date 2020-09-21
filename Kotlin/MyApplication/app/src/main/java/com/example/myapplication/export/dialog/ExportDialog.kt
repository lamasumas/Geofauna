package com.example.myapplication.export.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.export.ExportManager
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable

class ExportDialog(context: Context,
                   private val activity: Activity,
                   private val listOfAnimals: List<SimpleAdvanceRelation>?,
                   private val transectName: String?
) : Dialog(context) {

    private val disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share_dialog)
        setCanceledOnTouchOutside(true);
        ExportManager().also {
            disposables.addAll(
                    findViewById<Button>(R.id.btnShare).clicks().subscribe { _->
                        it.exportToDrive(context.applicationContext, listOfAnimals, transectName)
                        this.dismiss()
                    },
                    findViewById<Button>(R.id.btnExportPhone).clicks().subscribe { _->
                        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "text/csv"
                            putExtra(Intent.EXTRA_TITLE, it.createFileName(transectName))
                        }
                        activity.startActivityForResult(intent, MainActivity.EXPORT_CODE)
                        this.dismiss()
                    })
        }


    }

    override fun dismiss() {
        disposables.dispose()
        super.dismiss()
    }


}