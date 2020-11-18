package com.example.myapplication.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

class ExportManager {
    private val disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Este metodo transforma una lista de animales (simples y advances) a un formato csv
     * @param context, contexto de la aplicacion
     * @param listOfAnimals, lista de animales (relaciones simple y advanec)
     * @param uri, donde va a ser almacenado
     */
    fun exportToCSV(uri: Uri?, context: Context, listOfAnimals: List<SimpleAdvanceRelation>?) {

            uri.also { uri ->
                context.contentResolver.openFileDescriptor(uri!!, "rw").use {
                    FileOutputStream(it!!.fileDescriptor).use {
                        it.write(listOfAnimals?.let { it1 -> createFileContent(it1).toByteArray() })
                        disposables.clear()
                    }
                }
            }
        }


    /**
     * Funcion que transforma una lista de animales (simples y advances) a un archivo csv y este archivo a otra plataforma
     * @param context, contexto de la aplicacion
     * @param listOfAnimals, lista de animales (realcion simple/advance)
     * @param name, nombre del archivo
     */
    fun exportToDrive(context: Context, listOfAnimals: List<SimpleAdvanceRelation>?, name: String?) {

            val outputStream = context.openFileOutput(createFileName(name), Context.MODE_PRIVATE)
            outputStream.write(listOfAnimals?.let { createFileContent(it).toByteArray() })
            outputStream.close()

            val fileLocation = File(context.filesDir, createFileName(name))
            val path = FileProvider.getUriForFile(context, "com.example.myapplication.fileprovider",fileLocation)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/csv"
            intent.putExtra(Intent.EXTRA_STREAM, path)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)

        }


    /**
     * Transforma una lista de animales (relacion simple/advance) a una string, concadenando los toStrings
     * de cada animal
     * @param animals, lista de animales (relacion simple/advance)
     * @return una string con el contenido del archivo .csv
     */
    private fun  createFileContent(animals:List<SimpleAdvanceRelation>):String{
        var content: StringBuilder = StringBuilder("Id simple,Id Advance,Especie,Longitud,Latitud,Fecha,Hora,Humedad,Temperatura,Es altitud estimada con presión, Altitud,Presion,Indicie UV,Lugar,Pais,Notas, Ubicación de la foto\n")
        animals .forEach {content.append(it.toString())}
        return content.toString()
    }

    /**
     *  Genera el nombre del arlchivo en base a otro introducido por  el usuario o uno completamente autogenerado
     *  @param name, nombre introducido por el usuario
     *  @return una string con el nombre construido
     */
    fun createFileName(name:String?): String {
        var tempName = "Avistamientos"
        if(name != null) tempName = name
        val theDate = Calendar.getInstance().time
        return tempName + "-"+SimpleDateFormat("hh.mm.ss-dd.mm.yyyy").format(theDate).toString() + ".csv"
    }


}