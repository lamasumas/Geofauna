package com.example.myapplication.fragments.animals_database

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bluetooth.dialog.BluetoothScanDialog
import com.example.myapplication.export.dialog.ExportDialog
import com.example.myapplication.fragments.abstracts.AbstractDatabaseFragment
import com.example.myapplication.fragments.animals_database.database_recyclerview.DatabaseRvAdapter
import com.example.myapplication.viewmodels.controllers.BleControllerViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks


class AnimalDatabaseViewFragment : AbstractDatabaseFragment() {

    val bleControllerViewModel: BleControllerViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        animalDatabaseViewModel.loadData(transectViewModel.selectedId.value!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bb, menu)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(view.findViewById<FloatingActionButton>(R.id.fab).clicks()
                .subscribe {
                    val navController = view.findNavController()
                    if (navController.currentDestination?.id == R.id.mainFragment2)
                        navController.navigate(AnimalDatabaseViewFragmentDirections.actionMainFragment2ToAvistamiento2())
                })


        view.findViewById<RecyclerView>(R.id.rvDatabase).apply {
            setHasFixedSize(true)
            adapter = DatabaseRvAdapter(animalDatabaseViewModel.dataList.value!!, disposables)
            layoutManager = LinearLayoutManager(view.context)

            animalDatabaseViewModel.dataList.observe(viewLifecycleOwner, Observer {
                adapter?.notifyDataSetChanged()
                view.findViewById<View>(R.id.databaseMiddleware).visibility = View.GONE
                if (it.isEmpty()) {
                    view.findViewById<RecyclerView>(R.id.rvDatabase).visibility = View.GONE
                    view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.VISIBLE
                } else {
                    view.findViewById<RecyclerView>(R.id.rvDatabase).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.GONE
                }

            })
        }

        disposables.add(view.findViewById<Button>(R.id.btnDeleteAll).clicks().subscribe {
            AlertDialog.Builder(view.context).setMessage(R.string.dangerMessage)
                    .setTitle(R.string.dangerTitle)
                    .setNegativeButton(R.string.btnCancel) { dialog, id -> dialog.dismiss() }
                    .setPositiveButton(R.string.btnDeleteAll) { dialog, id ->
                        animalDatabaseViewModel.cleanDatabase(transectViewModel.selectedId.value!!)
                    }
                    .create().show()
        }
        )

        disposables.add(view.findViewById<Button>(R.id.bluetoothMenu).clicks().subscribe {
            BluetoothScanDialog(view.context, bleControllerViewModel.scanDevices()).show()
        })


        view.findViewById<BottomAppBar>(R.id.bar).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.exportarMenu -> {

                    ExportDialog(requireContext(),
                            requireActivity(),
                            animalDatabaseViewModel.dataList.value,
                    transectViewModel.selectedTransect.value?.name).show()

                    true
                }
                else -> false
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

