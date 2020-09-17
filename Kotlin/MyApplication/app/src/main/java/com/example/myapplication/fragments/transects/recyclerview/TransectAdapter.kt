package com.example.myapplication.fragments.transects.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.AbstractRecyclerViewAdapter
import com.example.myapplication.room.data_classes.Transect
import com.jakewharton.rxbinding2.view.clicks

class TransectAdapter( private val storedTransects:List<Transect>) : AbstractRecyclerViewAdapter<TransectViewHolder>() {
    private var secondColor = false
    var selectedHolder:TransectViewHolder? = null

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): TransectViewHolder {
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.transect_card_scheme, parent,false)
        return TransectViewHolder(theView)
    }

    override fun onBindViewHolder(holder: TransectViewHolder, position: Int) {
        holder.name.setText(storedTransects[position].name)
        holder.country.setText(storedTransects[position].country)
        holder.county.setText(storedTransects[position].locality)
        holder.animalList.setText(storedTransects[position].aniamlList)
        if(secondColor) {
            holder.carView.setCardBackgroundColor( holder.carView.resources.getColor(R.color.colorTerciarioPaletaVariante, null))
            holder.originalColor = holder.carView.cardBackgroundColor
            secondColor = false
        }else{
            secondColor= true
        }

        val temp = listOf<TempViewsHolders>(
                TempViewsHolders(holder.county, R.id.lTvCounty, holder.carView),
                TempViewsHolders(holder.animalList, R.id.lTvAnimals,holder.carView)
        )
        holder.carView.clicks().subscribe{
            selectedHolder?.carView?.setCardBackgroundColor(selectedHolder?.originalColor)
            holder.carView.setCardBackgroundColor(holder.carView.resources.getColor(R.color.colorSecundarioPaleta, null))
            selectedHolder = holder
        }
        hideNullTextViews(temp)

    }

    override fun getItemCount(): Int {
        return storedTransects.size
    }

    fun returnSelectedElement(): TransectViewHolder?{
        return selectedHolder
    }
}