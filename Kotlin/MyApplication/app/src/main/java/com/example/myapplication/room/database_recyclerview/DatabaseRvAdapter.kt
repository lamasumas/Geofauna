package com.example.myapplication.room.database_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.MainFragmentDirections
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseRvAdapter(val elements:List<SimpleAdvanceRelation>): RecyclerView.Adapter<AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.animal_card_scheme, parent,false)
        return AnimalViewHolder(theView)
    }

    override fun getItemCount(): Int {
        return elements.size
    }
    

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.species.text = elements[position].simpleData.especie
        holder.lon.text = elements[position].simpleData.longitude.toString()
        holder.lat.text = elements[position].simpleData.latitude.toString()
        holder.time.text = elements[position].simpleData.time
        holder.date.text = elements[position].simpleData.date
        holder.country.text = elements[position].advanceData.pais
        holder.place.text = elements[position].advanceData.lugar
        holder.humidity.text = elements[position].advanceData.humidity.toString()
        holder.altitude.text = elements[position].advanceData.altitude.toString()
        holder.temperature.text = elements[position].advanceData.temperature.toString()
        holder.uv.text = elements[position].advanceData.index_uv.toString()
        holder.pressures.text = elements[position].advanceData.pressure.toString()
        holder.idSimple = elements[position].simpleData.simpleId
        holder.idAdvance = elements[position].advanceData.uid
        hideNullTextViews(holder)

        holder.cv.clicks().subscribe {
            holder.hiddenViews.visibility = if (holder.hiddenViews.isShown()) View.GONE else View.VISIBLE
        }

        holder.btnEdit.clicks().observeOn(AndroidSchedulers.mainThread()).subscribe {
            holder.btnEdit.findNavController().navigate(MainFragmentDirections.actionMainFragment2ToEditSightseen(holder.idSimple))
        }

        holder.btnDelete.clicks().observeOn(AndroidSchedulers.mainThread()).doOnNext{
            holder.cv.removeAllViews()
    }.observeOn(Schedulers.io()).subscribe {
            val dbRepository = DatabaseRepository(holder.cv.context)
            dbRepository.deleteAnimal(elements[position].simpleData)

        }


    }

    private fun hideNullTextViews(holder: AnimalViewHolder) {
        hideIfNull(holder.altitude, R.id.lCvAltitud, holder.generalView)
        hideIfNull(holder.pressures, R.id.lCvPressure, holder.generalView)
        hideIfNull(holder.uv, R.id.lCvUV, holder.generalView)
        hideIfNull(holder.temperature, R.id.lCvTemperature, holder.generalView)
        hideIfNull(holder.place, R.id.lCvPlace, holder.generalView)
        hideIfNull(holder.country, R.id.lCvCountry, holder.generalView)
        hideIfNull(holder.humidity, R.id.lCvHumedad, holder.generalView)
    }


    private fun hideIfNull(textView: TextView, titleId:Int, itemView:View){
        if(textView.text == null || textView.text == "null" || textView.text == ""){
            itemView.findViewById<LinearLayout>(titleId).visibility = LinearLayout.GONE

        }
    }

}