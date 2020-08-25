package com.example.myapplication.room.database_recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.MainFragmentDirections
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AnimalSimpleData
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseRvAdapter(val elements:List<AnimalSimpleData>): RecyclerView.Adapter<AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.animal_card_scheme, parent,false)
        return AnimalViewHolder(theView)
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.species.text = elements[position].especie
        holder.lon.text = elements[position].longitude.toString()
        holder.lat.text = elements[position].latitude.toString()
        holder.time.text = elements[position].time
        holder.date.text = elements[position].date
        holder.uid = elements[position].uid
        holder.cv.clicks().subscribe {
            holder.hiddenViews.visibility = if (holder.hiddenViews.isShown()) View.GONE else View.VISIBLE
        }

        holder.btnEdit.clicks().observeOn(AndroidSchedulers.mainThread()).subscribe {
            holder.btnEdit.findNavController().navigate(MainFragmentDirections.actionMainFragment2ToEditSightseen(holder.uid))
        }

        holder.btnDelete.clicks().observeOn(AndroidSchedulers.mainThread()).doOnNext{
            holder.cv.removeAllViews()
    }.observeOn(Schedulers.io()).subscribe {
            val dbRepository = DatabaseRepository(holder.cv.context)
            dbRepository.deleteAnimal(elements[position])

        }


    }

}