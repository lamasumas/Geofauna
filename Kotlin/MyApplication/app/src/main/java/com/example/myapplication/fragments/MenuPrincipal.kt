package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.data_classes.AvistamientoData
import com.example.myapplication.database.DatabaseInstance
import com.example.myapplication.viewmodels.DatabaseViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_avistamiento.*
import java.util.*

class MenuPrincipal : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vmDatabase = ViewModelProviders.of(this).get(DatabaseViewModel(DatabaseInstance.getInstance(view.context))::class.java);
        vmDatabase.retrieveSightsings();

        vmDatabase.sightings.observe(viewLifecycleOwner, Observer {
                if(!it.isEmpty()) generateTable(view, it)
        })
    }

    private fun generateTable(view:View, animals: List<AvistamientoData>?) {
        val table = view.findViewById<TableLayout>(R.id.tableSeen);
        fun createTextView(text:String):TextView{
            var newTextView = TextView(context);
            newTextView.text = text;
            return  newTextView;
        }
        animals?.forEach({
            val newRow = TableRow(context);
            newRow.addView(createTextView(it.especie));
            newRow.addView(createTextView(it.latitude.toString()));
            newRow.addView(createTextView(it.longitude.toString()));
            newRow.addView(createTextView(it.date));
            newRow.addView(createTextView(it.time));
            table.addView(newRow)
        })


    }


}