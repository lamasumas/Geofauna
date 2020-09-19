package com.example.myapplication.fragments.transects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.GeneralFragmentRx
import com.example.myapplication.fragments.transects.dialog.NewTransectDialog
import com.example.myapplication.fragments.transects.recyclerview.TransectAdapter
import com.example.myapplication.room.DatabaseRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_menu_principal.*


class TransectFragment : GeneralFragmentRx() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRecyclerView(view)

        disposables.add(view.findViewById<FloatingActionButton>(R.id.btnAddNewTransect).clicks()
                .subscribe {
                    val dialog = NewTransectDialog(view.context)
                    dialog.setOnDismissListener {
                        loadRecyclerView(view)
                    }
                    dialog.show()


                })

        disposables.add(view.findViewById<Button>(R.id.btnUse).clicks().subscribeOn(AndroidSchedulers.mainThread()).subscribe {

            (view.findViewById<RecyclerView>(R.id.rvTransects).adapter as TransectAdapter).also {
                findNavController().navigate(TransectFragmentDirections.actionMenuPrincipalToMainFragment2(it.selectedHolder!!.idDb))
                it.disposables.dispose()
            }

        })

    }

    private fun loadRecyclerView(view: View) {

        view.findViewById<RecyclerView>(R.id.rvTransects).apply {
            layoutManager = LinearLayoutManager(view.context)
            disposables.add(Observable.just(DatabaseRepository(view.context)).flatMap { it.retrieveTransects() }.subscribe {
                adapter = TransectAdapter(it)
                visibility = View.VISIBLE
            })

        }
    }
}