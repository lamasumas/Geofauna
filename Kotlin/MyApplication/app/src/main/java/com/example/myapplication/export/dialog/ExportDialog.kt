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
import com.jakewharton.rxbinding2.view.clicks

class ExportDialog(context: Context, private val activity: Activity):Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share_dialog)
        setCanceledOnTouchOutside(true);
        findViewById<Button>(R.id.btnShare).clicks().subscribe {
            ExportManager().exportToDrive(context.applicationContext)
            this.dismiss()
        }
        findViewById<Button>(R.id.btnExportPhone).clicks().subscribe {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/csv"
                putExtra(Intent.EXTRA_TITLE, "Avistamientos.csv")}
            activity.startActivityForResult(intent, MainActivity.EXPORT_CODE)
            this.dismiss()
        }


    }


}