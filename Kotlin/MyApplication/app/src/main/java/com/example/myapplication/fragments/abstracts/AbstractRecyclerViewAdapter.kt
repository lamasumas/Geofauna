package com.example.myapplication.fragments.abstracts

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.animals_database.database_recyclerview.AnimalViewHolder

abstract class AbstractRecyclerViewAdapter <T : RecyclerView.ViewHolder>: RecyclerView.Adapter<T>(){


    protected data class TempViewsHolders(val view: TextView, val id:Int, val generalView:View)
    protected fun hideNullTextViews(x:List<TempViewsHolders>) {
        x.forEach { hideIfNull(it.view, it.id, it.generalView) }
    }


    private fun hideIfNull(textView: TextView, titleId:Int, itemView: View){
        if(textView.text == null || textView.text == "null" || textView.text == ""){
            itemView.findViewById<LinearLayout>(titleId)?.visibility = LinearLayout.GONE

        }
    }


}