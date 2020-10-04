package com.example.myapplication.fragments.transects

import android.graphics.Canvas
import android.graphics.Color.red
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.fragments.abstracts.GeneralFragmentRx
import com.example.myapplication.fragments.transects.dialog.NewTransectDialog
import com.example.myapplication.fragments.transects.recyclerview.TransectAdapter
import com.example.myapplication.fragments.transects.recyclerview.TransectViewHolder
import com.example.myapplication.viewmodels.TransectViewModel
import com.example.myapplication.viewmodels.controllers.LocationControllerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import io.reactivex.android.schedulers.AndroidSchedulers
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class TransectFragment : GeneralFragmentRx() {


    val transectViewModel: TransectViewModel by activityViewModels()
    val locationController: LocationControllerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rvTransects).apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(view.context)
            adapter = TransectAdapter(transectViewModel.transectList.value!!).also { adapter ->

                disposables.add(view.findViewById<Button>(R.id.btnAddNewTransect).clicks()
                        .subscribe {
                            NewTransectDialog(view.context, transectViewModel, locationController.getOneGPSPosition()).show()
                            adapter.isSelected.value = false
                        })


                adapter.selectedHolder.observe(viewLifecycleOwner) {
                    transectViewModel.choosenTransect(adapter.selectedHolder!!.value!!.idDb)
                    view.findNavController().navigate(TransectFragmentDirections.actionMenuPrincipalToMainFragment2())
                    adapter.disposables.dispose()
                }

                ItemTouchHelper(getSwipLeftCallback()).attachToRecyclerView(this)
            }


            transectViewModel.transectList.observe(viewLifecycleOwner, Observer {
                view.findViewById<RecyclerView>(R.id.rvTransects)?.adapter?.notifyDataSetChanged()
            })


        }
    }

    fun getSwipLeftCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as TransectViewHolder).also {
                    transectViewModel.deleteTransect(it.idDb)
                }


            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.delete_icon)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                        .setActionIconTint(ContextCompat.getColor(requireContext(), R.color.colorQuintoPaleta))
                        .create()
                        .decorate()


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
    }

}