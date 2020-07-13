package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.AnimalViewHolder
import com.example.myapplication.R
import com.example.myapplication.fragments.DatabaseViewFragment
import com.example.myapplication.fragments.EditSightseenDirections
import com.example.myapplication.fragments.MainFragment
import com.example.myapplication.fragments.MainFragmentDirections
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AvistamientoData
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

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
        holder.country.text = elements[position].pais
        holder.place.text = elements[position].lugar
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