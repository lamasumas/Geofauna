package com.example.myapplication.fragments.transects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.GeneralFragmentRx
import com.example.myapplication.fragments.transects.dialog.NewTransectDialog
import com.example.myapplication.fragments.transects.recyclerview.TransectAdapter
import com.example.myapplication.viewmodels.TransectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import io.reactivex.android.schedulers.AndroidSchedulers


class TransectFragment : GeneralFragmentRx() {


    val transectViewModel: TransectViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rvTransects).apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = TransectAdapter(transectViewModel.transectList.value!!)
            visibility = View.VISIBLE
            disposables.add(view.findViewById<FloatingActionButton>(R.id.btnAddNewTransect).clicks()
                    .subscribe {
                        NewTransectDialog(view.context, transectViewModel).show()
                    })

            transectViewModel.transectList.observe(viewLifecycleOwner, Observer {
                view.findViewById<RecyclerView>(R.id.rvTransects)?.adapter?.notifyDataSetChanged()
            })


            (this.adapter as TransectAdapter).also {adapter ->
                adapter.selectedHolder.observe(viewLifecycleOwner) {
                        view.findViewById<Button>(R.id.btnUse).isEnabled = transectViewModel.transectList.value!!.size >0
                        view.findViewById<Button>(R.id.btnDeleteTransect).isEnabled = transectViewModel.transectList.value!!.size >0
                }

                disposables.add(view.findViewById<Button>(R.id.btnDeleteTransect).clicks().subscribe {
                    disposables.add(transectViewModel.deleteTransect(adapter.selectedHolder.value!!.idDb))

                })

                disposables.add(view.findViewById<Button>(R.id.btnUse).clicks().subscribeOn(AndroidSchedulers.mainThread()).subscribe {

                    transectViewModel.choosenTransect(adapter.selectedHolder!!.value!!.idDb)
                    view.findNavController().navigate(TransectFragmentDirections.actionMenuPrincipalToMainFragment2())
                    adapter.disposables.dispose()


                })
            }

        }
    }

}