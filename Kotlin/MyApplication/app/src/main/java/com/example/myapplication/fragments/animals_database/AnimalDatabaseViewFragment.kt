package com.example.myapplication.fragments.animals_database

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bluetooth.dialog.BluetoothScanDialog
import com.example.myapplication.export.dialog.ExportDialog
import com.example.myapplication.fragments.abstracts.GeneralFragmentRx
import com.example.myapplication.fragments.animals_database.database_recyclerview.DatabaseRvAdapter
import com.example.myapplication.fragments.animals_database.database_recyclerview.TestViewModel
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.SimpleAdvanceRelation
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AnimalDatabaseViewFragment : GeneralFragmentRx() {

    var recyclerView: RecyclerView? = null
    val testViewModel: TestViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bb, menu)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idTransect = arguments?.getLong("transectId") as Long

        disposables.add(view.findViewById<FloatingActionButton>(R.id.fab).clicks()
                .subscribe {
                    val navController = view.findNavController()
                    if (navController.currentDestination?.id == R.id.mainFragment2)
                        navController.navigate(AnimalDatabaseViewFragmentDirections.actionMainFragment2ToAvistamiento2(idTransect))
                })


        view.findViewById<RecyclerView>(R.id.rvDatabase).apply {
            setHasFixedSize(true)
            adapter = DatabaseRvAdapter(testViewModel.dataList.value!!, idTransect, disposables)
            layoutManager = LinearLayoutManager(view.context)

            testViewModel.clear()
            DatabaseRepository(view.context).retrieveAllAnimalDataFromATransect(idTransect)
                    ?.subscribeOn(AndroidSchedulers.mainThread())?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe {
                        testViewModel.dataList.value?.add(it)
                        testViewModel.dataList.value = testViewModel.dataList.value
                    }

            testViewModel.dataList.observe(viewLifecycleOwner, Observer {
                adapter?.notifyDataSetChanged()
                if (it.isEmpty()) {
                    view.findViewById<RecyclerView>(R.id.rvDatabase).visibility = View.GONE
                    view.findViewById<Button>(R.id.btnDeleteAll).visibility = View.GONE
                    view.findViewById<TextView>(R.id.tvEmptyDatabase).visibility = View.VISIBLE
                } else {
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
                        DatabaseRepository(view.context).cleanTransectAnaimals(idTransect)
                        testViewModel.dataList.value?.clear()

                    }
                    .create().show()
        }
        )

        disposables.add(view.findViewById<Button>(R.id.bluetoothMenu).clicks().subscribe {
            BluetoothScanDialog(view.context).show()
        })


        view.findViewById<BottomAppBar>(R.id.bar).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.exportarMenu -> {

                    ExportDialog(requireContext(), requireActivity()).show()

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

