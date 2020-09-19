package com.example.myapplication.fragments.animals_database

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.AbstractDatabaseFragment
import com.example.myapplication.fragments.animals_database.database_recyclerview.DatabaseRvAdapter
import com.example.myapplication.room.DatabaseRepository
import com.jakewharton.rxbinding2.view.clicks


class AnimalsDatabaseViewFragment(private val transectId:Long) : AbstractDatabaseFragment() {

    var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_database_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val animalDatabaseViewModel = AnimalDatabaseViewData(transectId, view.context)

        view.findViewById<RecyclerView>(R.id.rvDatabase).apply {
            setHasFixedSize(true)
            adapter  = DatabaseRvAdapter(animalDatabaseViewModel.dataList.value!!,transectId, disposables)
            layoutManager =  LinearLayoutManager(view.context)

            animalDatabaseViewModel.dataList.observe(viewLifecycleOwner, Observer{
                adapter?.notifyDataSetChanged()
                if(it.isEmpty()){
                    view.findViewById<RecyclerView>(R.id.rvDatabase).visibility = View.GONE
                    view.findViewById<Button>(R.id.btnDeleteAll).visibility = View.GONE
                    view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.VISIBLE
                }
                else{
                    view.findViewById<RecyclerView>(R.id.rvDatabase).visibility = View.VISIBLE
                    view.findViewById<Button>(R.id.btnDeleteAll).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.GONE
                }

            })
        }

        disposables.add(view.findViewById<Button>(R.id.btnDeleteAll).clicks().subscribe {
            AlertDialog.Builder(view.context).setMessage(R.string.dangerMessage)
                    .setTitle(R.string.dangerTitle)
                    .setNegativeButton(R.string.btnCancel) { dialog, id -> dialog.dismiss() }
                    .setPositiveButton(R.string.btnDeleteAll) { dialog, id ->
                        DatabaseRepository(view.context).cleanTransectAnaimals(transectId)
                        animalDatabaseViewModel.checkForNews()
                    }
                    .create().show()
        }
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView?.adapter = null
    }

}