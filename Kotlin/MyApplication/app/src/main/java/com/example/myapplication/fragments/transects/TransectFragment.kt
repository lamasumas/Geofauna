package com.example.myapplication.fragments.transects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.GeneralFragmentRx
import com.example.myapplication.fragments.transects.dialog.NewTransectDialog
import com.example.myapplication.fragments.transects.recyclerview.TransectAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers


class TransectFragment : GeneralFragmentRx() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transectViewModel = TransectRecyclerViewData(view.context)
        view.findViewById<RecyclerView>(R.id.rvTransects).apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = TransectAdapter(transectViewModel.dataList.value!!)
            visibility = View.VISIBLE


            disposables.add(view.findViewById<FloatingActionButton>(R.id.btnAddNewTransect).clicks()
                    .subscribe {
                        val dialog = NewTransectDialog(view.context)
                        dialog.setOnDismissListener {
                            transectViewModel.checkForNews()
                            this.adapter?.notifyDataSetChanged()
                        }
                        dialog.show()


                    })
        }

        disposables.add(view.findViewById<Button>(R.id.btnUse).clicks().subscribeOn(AndroidSchedulers.mainThread()).subscribe {

            (view.findViewById<RecyclerView>(R.id.rvTransects).adapter as TransectAdapter).also {
                view.findNavController().navigate(TransectFragmentDirections.actionMenuPrincipalToMainFragment2(it.selectedHolder!!.value!!.idDb))
                it.disposables.dispose()
            }

        })

    }

}