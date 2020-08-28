package com.example.myapplication.fragments

import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.room.data_classes.AnimalAdvanceData
import com.example.myapplication.room.data_classes.AnimalSimpleData
import io.reactivex.disposables.CompositeDisposable
import java.lang.NumberFormatException

abstract class AbstractDatabaseFragment : Fragment() {

    protected var disposables = CompositeDisposable()


    protected fun createSimpleAnimalObject(view: View): AnimalSimpleData {
        val species = view.findViewById<EditText>(R.id.etEspecie).text.toString()
        val latitude = view.findViewById<EditText>(R.id.etLatitud).text.toString()
        val longitude = view.findViewById<EditText>(R.id.etLongitud).text.toString()
        val time = view.findViewById<EditText>(R.id.etHour).text.toString() +
                ":" + view.findViewById<EditText>(R.id.etMinute).text.toString()
        val date = view.findViewById<EditText>(R.id.etDay).text.toString() + "/" +
                view.findViewById<EditText>(R.id.etMonth).text.toString() +
                "/" + view.findViewById<EditText>(R.id.etYear).text.toString()

        return AnimalSimpleData(especie = species, date = date, latitude = latitude.toDouble(),
                longitude = longitude.toDouble(), time = time)


    }

    protected fun createAdvanceAnimalObject(view: View): AnimalAdvanceData {
        val place = view.findViewById<EditText>(R.id.etLugar).text.toString()
        val country = view.findViewById<EditText>(R.id.etPais).text.toString()
        val humidity = view.findViewById<EditText>(R.id.etHumidity).text.toString()
        val altitude = view.findViewById<EditText>(R.id.etAltitude).text.toString()
        val uv = view.findViewById<EditText>(R.id.etIndexUV).text.toString()
        val temperature = view.findViewById<EditText>(R.id.etTemperature).text.toString()
        val pressure = view.findViewById<EditText>(R.id.etPressure).text.toString()

        return AnimalAdvanceData(pais = country, lugar = place, humidity = checkDataInput(humidity),
                temperature = checkDataInput(temperature), pressure = checkDataInput(pressure),
                altitude = checkDataInput(altitude), index_uv = checkDataInput(uv)?.toInt())
    }

    private fun checkDataInput(input:String):Double?{
        var temp:Double? = null
        try {
            temp = input.toDouble()
        }catch (e:NumberFormatException){
            return null
        }finally {
            return temp
        }
    }

    fun ifNullEmptyString(input: String?):String{
        if(input == null || input.equals("null"))
            return ""
        else
            return input
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}