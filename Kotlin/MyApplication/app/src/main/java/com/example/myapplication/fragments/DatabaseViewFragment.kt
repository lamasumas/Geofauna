package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.AbstractDatabaseFragment
import com.example.myapplication.room.database_recyclerview.DatabaseRvAdapter
import com.example.myapplication.room.DatabaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DatabaseViewFragment : AbstractDatabaseFragment() {

    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_database_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewManager = LinearLayoutManager(view.context);

        recyclerView = view.findViewById<RecyclerView>(R.id.rvDatabase).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            disposables.add(Observable.just(DatabaseRepository(view.context)).observeOn(Schedulers.io())
                    .flatMap { it.retrieveSightseeing() }
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {
                        adapter = DatabaseRvAdapter(it)
                        visibility = View.VISIBLE
                        view.findViewById<ProgressBar>(R.id.databaseMiddleware).visibility = View.GONE
                    })
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView?.adapter = null
    }
}