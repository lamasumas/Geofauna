package com.example.myapplication

import android.content.Context
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.disposables.CompositeDisposable
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import jxl.write.WritableSheet
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ExportManager {
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun exportToExcel(path: File, context: Context) {
        val wbSettings:WorkbookSettings = WorkbookSettings()

        try {
            val dbRepository = DatabaseRepository(context)
            disposables.add(dbRepository.retrieveSightseeing().subscribe {

                val newFile = File(path, "Avistamientos.xls")
                val excelWorkbook = Workbook.createWorkbook(newFile)
                val excelPage = excelWorkbook.createSheet("Avistamientos", 0)
                generateColumnTitles(excelPage)
                generateColumnValues(excelPage, it)
                excelWorkbook.write()
                excelWorkbook.close()
            })
        }catch (e:Exception){

        }
    }

    private fun generateColumnTitles(excelPage: WritableSheet) {
        excelPage.addCell(Label(0,0,"Id Simple"))
        excelPage.addCell(Label(1, 0,"Id Advance"))
        excelPage.addCell(Label(2, 0 ,"Especie"))
        excelPage.addCell(Label(3, 0 ,"Longitud"))
        excelPage.addCell(Label(4, 0 ,"Latitud"))
        excelPage.addCell(Label(5, 0 ,"Fecha"))
        excelPage.addCell(Label(6, 0 ,"Hora"))
        excelPage.addCell(Label(7, 0 ,"Humedad"))
        excelPage.addCell(Label(8, 0 ,"Temeperatura"))
        excelPage.addCell(Label(9, 0 ,"Altitud"))
        excelPage.addCell(Label(10, 0 ,"Presion"))
        excelPage.addCell(Label(11, 0 ,"Indice UV"))
        excelPage.addCell(Label(12, 0 ,"Lugar"))
        excelPage.addCell(Label(13, 0 ,"País"))
    }

    private fun generateColumnValues(excelPage: WritableSheet, it: List<SimpleAdvanceRelation>) {
        generateColumnValues(0, it.map { it.simpleData.simpleId.toString() }, excelPage)
        generateColumnValues(1, it.map { it.advanceData.uid.toString() }, excelPage)
        generateColumnValues(2, it.map { it.simpleData.especie }, excelPage)
        generateColumnValues(3, it.map { it.simpleData.longitude.toString() }, excelPage)
        generateColumnValues(4, it.map { it.simpleData.latitude.toString() }, excelPage)
        generateColumnValues(5, it.map { it.simpleData.date }, excelPage)
        generateColumnValues(6, it.map { it.simpleData.time }, excelPage)
        generateColumnValues(7, it.map { it.advanceData.humidity.toString() }, excelPage)
        generateColumnValues(8, it.map { it.advanceData.temperature.toString() }, excelPage)
        generateColumnValues(9, it.map { it.advanceData.altitude.toString() }, excelPage)
        generateColumnValues(10, it.map { it.advanceData.pressure.toString() }, excelPage)
        generateColumnValues(11, it.map { it.advanceData.index_uv.toString() }, excelPage)
        generateColumnValues(12, it.map { it.advanceData.lugar.toString() }, excelPage)
        generateColumnValues(13, it.map { it.advanceData.pais.toString() }, excelPage)
    }

    private fun generateColumnValues(column: Int, values: List<String>, page: WritableSheet) {
        var row = 1
        values.forEach {
            page.addCell(Label(column, row, it))
            row++
        }
    }
}