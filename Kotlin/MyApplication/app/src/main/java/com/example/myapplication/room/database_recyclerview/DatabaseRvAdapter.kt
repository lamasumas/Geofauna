package com.example.myapplication.room.database_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        holder.idSimple = elements[position].simpleData.simpleId
        holder.idAdvance = elements[position].advanceData.uid
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

}