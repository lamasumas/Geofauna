package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.DatabaseRvAdapter
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AvistamientoData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_database_view.*


class DatabaseViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_database_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DatabaseRepository(view.context);
        val viewManager = LinearLayoutManager(view.context);
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvDatabase).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            db.retrieveSightsings().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                adapter= DatabaseRvAdapter(it)
                visibility =  View.VISIBLE
                view.findViewById<ProgressBar>(R.id.databaseMiddleware).visibility = View.GONE

            }
            }

    }


}