package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.AnimalViewHolder
import com.example.myapplication.R
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AvistamientoData

class DatabaseRvAdapter(val elements:List<AvistamientoData>): RecyclerView.Adapter<AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder{
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.card_scheme, parent,false)
        return AnimalViewHolder(theView);
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.species.setText(elements[position].especie)

    }

}