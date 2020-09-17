package com.example.myapplication.fragments.transects.recyclerview

import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class TransectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val carView: CardView
    val name :TextView
    val country :TextView
    val county :TextView
    val animalList :TextView
    var originalColor:ColorStateList

    init {
        carView = itemView.findViewById(R.id.cvTrasnectCard)
        name = itemView.findViewById(R.id.tvTransectName)
        country = itemView.findViewById(R.id.tvCountry)
        county = itemView.findViewById(R.id.tvCounty)
        animalList = itemView.findViewById(R.id.tvAnimals)
        originalColor = carView.cardBackgroundColor
    }

}