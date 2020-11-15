package com.lamasumas.myapplication.export.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.lamasumas.myapplication.MainActivity
import com.lamasumas.myapplication.R
import com.lamasumas.myapplication.export.ExportManager
import com.lamasumas.myapplication.room.data_classes.SimpleAdvanceRelation
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable

class ExportDialog(private val listOfAnimals: List<SimpleAdvanceRelation>?,
                   private val transectName: String?
) : DialogFragment() {

    private val disposables = CompositeDisposable()





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val theView = inflater.inflate(R.layout.share_dialog, container)
        ExportManager().also {
            disposables.addAll(
                    theView.findViewById<Button>(R.id.btnShare).clicks().subscribe { _->
                        it.exportToDrive(theView.context.applicationContext, listOfAnimals, transectName)
                        this.dismiss()
                    },
                    theView.findViewById<Button>(R.id.btnExportPhone).clicks().subscribe { _->
                        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "text/csv"
                            putExtra(Intent.EXTRA_TITLE, it.createFileName(transectName))
                        }
                        requireActivity().startActivityForResult(intent, MainActivity.EXPORT_CODE)
                        this.dismiss()
                    })
        }


        return theView
    }

    override fun dismiss() {
        disposables.dispose()
        super.dismiss()
    }


}