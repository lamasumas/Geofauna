package com.example.myapplication

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var cv:CardView
    var species:TextView;
    init {
        cv = itemView.findViewById(R.id.cvAnimalScheme);
        species = itemView.findViewById(R.id.tvCvSpecies);
    }


}