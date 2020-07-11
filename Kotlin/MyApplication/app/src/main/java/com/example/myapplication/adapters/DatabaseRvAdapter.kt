package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.AnimalViewHolder
import com.example.myapplication.R
import com.example.myapplication.room.data_classes.AvistamientoData

class DatabaseRvAdapter(val elements:List<AvistamientoData>): RecyclerView.Adapter<AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder{
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.card_scheme, parent,false)
        return AnimalViewHolder(theView)
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.species.text = elements[position].especie
        holder.lon.text = elements[position].longitude.toString()
        holder.lat.text = elements[position].latitude.toString()
        holder.country.text = elements[position].latitude.toString()
        holder.place.text = elements[position].latitude.toString()
        holder.time.text = elements[position].time
        holder.date.text = elements[position].date

    }

}