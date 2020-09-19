package com.example.myapplication.fragments.animals_database

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.AbstractDatabaseFragment
import com.example.myapplication.fragments.animals_database.database_recyclerview.DatabaseRvAdapter
import com.example.myapplication.room.DatabaseRepository
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AnimalsDatabaseViewFragment : AbstractDatabaseFragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_database_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.rvDatabase)
        setDatabaseRecyclerView(recyclerView, view)
        disposables.add(view.findViewById<Button>(R.id.btnDeleteAll).clicks().subscribe {
            AlertDialog.Builder(view.context).setMessage(R.string.dangerMessage)
                    .setTitle(R.string.dangerTitle)
                    .setNegativeButton(R.string.btnCancel) { dialog, id -> dialog.dismiss() }
                    .setPositiveButton(R.string.btnDeleteAll) { dialog, id ->
                        DatabaseRepository(view.context).cleanDatabase()
                        resetIndicators(view)
                        view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.VISIBLE

                    }
                    .create().show()
        }
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView?.adapter = null
    }

    private fun setDatabaseRecyclerView(theRecyclerView: RecyclerView, view: View) {
        resetIndicators(view)
        val viewManager = LinearLayoutManager(view.context);
        theRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            disposables.add(Observable.just(DatabaseRepository(view.context)).observeOn(Schedulers.io())
                    .flatMap {
                        view.findViewById<ProgressBar>(R.id.databaseMiddleware).visibility = View.VISIBLE
                        it.retrieveAllAnimalDataFromATransect(transectId.value)
                    }
                    .observeOn(AndroidSchedulers.mainThread()).toList().subscribe { animalList ->

                        view.findViewById<ProgressBar>(R.id.databaseMiddleware).visibility = View.GONE
                        if (animalList.isNotEmpty()) {
                            adapter = DatabaseRvAdapter(animalList, disposables)
                            visibility = View.VISIBLE
                            view.findViewById<Button>(R.id.btnDeleteAll).visibility = View.VISIBLE
                        } else {

                            view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.VISIBLE
                        }
                    })
        }

    }

    private fun resetIndicators(view: View) {
        view.findViewById<RecyclerView>(R.id.rvDatabase).visibility = View.GONE
        view.findViewById<Button>(R.id.btnDeleteAll).visibility = View.GONE
        view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.GONE
        view.findViewById<ProgressBar>(R.id.databaseMiddleware).visibility = View.GONE
    }
}