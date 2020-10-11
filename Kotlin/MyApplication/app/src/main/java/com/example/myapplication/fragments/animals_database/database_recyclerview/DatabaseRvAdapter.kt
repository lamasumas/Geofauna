package com.example.myapplication.fragments.animals_database.database_recyclerview

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.AbstractRecyclerViewAdapter
import com.example.myapplication.fragments.animals_database.AnimalDatabaseViewFragmentDirections
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import com.example.myapplication.viewmodels.AnimalDatabaseViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class DatabaseRvAdapter(val elements: MutableList<SimpleAdvanceRelation>,  val disposables: CompositeDisposable) : AbstractRecyclerViewAdapter<AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.animal_card_scheme, parent, false)
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
        holder.humidity.text = elements[position].advanceData.humidity.toString()
        holder.altitude.text = elements[position].advanceData.altitude.toString()
        holder.temperature.text = elements[position].advanceData.temperature.toString()
        holder.uv.text = elements[position].advanceData.index_uv.toString()
        holder.pressures.text = elements[position].advanceData.pressure.toString()
        holder.idSimple = elements[position].simpleData.simpleId
        holder.idAdvance = elements[position].advanceData.uid
        holder.notes.text = elements[position].advanceData.notes

        if (!elements[position].advanceData.photoPlace.isNullOrEmpty())
            holder.ivPhoto.setImageURI(Uri.fromFile(File(elements[position].advanceData.photoPlace!!)))


        val temp = listOf<TempViewsHolders>(
                TempViewsHolders(holder.altitude, R.id.lCvAltitud, holder.generalView),
                TempViewsHolders(holder.pressures, R.id.lCvPressure, holder.generalView),
                TempViewsHolders(holder.uv, R.id.lCvUV, holder.generalView),
                TempViewsHolders(holder.temperature, R.id.lCvTemperature, holder.generalView),
                TempViewsHolders(holder.humidity, R.id.lCvHumedad, holder.generalView),
                TempViewsHolders(holder.notes, R.id.lCvNotes, holder.generalView)
        )
        hideNullTextViews(temp)

        disposables.add(holder.cv.clicks().subscribe {
            holder.hiddenViews.visibility = if (holder.hiddenViews.isShown()) View.GONE else View.VISIBLE
        })




    }

}