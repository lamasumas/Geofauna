package com.example.myapplication.fragments.animals_database

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.graphics.Canvas
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.export.dialog.ExportDialog
import com.example.myapplication.fragments.abstracts.AbstractDatabaseFragment
import com.example.myapplication.fragments.animals_database.database_recyclerview.AnimalViewHolder
import com.example.myapplication.fragments.animals_database.database_recyclerview.DatabaseRvAdapter
import com.example.myapplication.fragments.animals_database.dialogs.ChangeNextAltitudeDialog
import com.example.myapplication.fragments.animals_database.dialogs.ChangeSamplePressureDialog
import com.example.myapplication.viewmodels.controllers.BleControllerViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class AnimalDatabaseViewFragment : AbstractDatabaseFragment() {

    val bleControllerViewModel: BleControllerViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        animalDatabaseViewModel.loadData(transectViewModel.selectedId.value!!)?.let { disposables.add(it) }
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
            ItemTouchHelper(getSwipeLeftCallback()).attachToRecyclerView(this)
            ItemTouchHelper(getSwipeRightCallback()).attachToRecyclerView(this)
        }

        disposables.add(view.findViewById<Button>(R.id.btnDeleteAll).clicks().subscribe {
            AlertDialog.Builder(view.context).setMessage(R.string.dangerMessage)
                    .setTitle(R.string.dangerTitle)
                    .setNeutralButton(R.string.btnCancel) { dialog, id -> dialog.dismiss() }
                    .setPositiveButton(R.string.btnDeleteAll) { dialog, id ->
                        animalDatabaseViewModel.cleanDatabase(transectViewModel.selectedId.value!!)
                    }
                    .create().show()
        }
        )

        disposables.add(view.findViewById<Button>(R.id.bluetoothMenu).clicks().subscribe {

            bleControllerViewModel.scanDevicesAndConnect()?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                if (it != "")
                    generateConfirmationDialog(R.string.btnConected)
                else
                    generateConfirmationDialog(R.string.btnNotConected, false)
            }
        })


        view.findViewById<BottomAppBar>(R.id.bar).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.exportarMenu -> {


                    ExportDialog(animalDatabaseViewModel.dataList.value,
                            transectViewModel.selectedTransect.value?.name).show(requireActivity().supportFragmentManager.beginTransaction(), "New export dialog")

                    true
                }

                R.id.altitudeAndPressureMenu -> {
                    if (!(view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertDialog.Builder(view.context).setTitle(R.string.tvBluetoothLocalizacionTitule)
                                .setMessage(R.string.tvBluetoothLocalizacionApagado)
                                .setPositiveButton(R.string.cerrarAlertBoton) { button, _ -> button.dismiss() }
                                .show()
                    } else if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                        AlertDialog.Builder(view.context).setTitle(R.string.tvBluetoothScanTituloAlternativo)
                                .setMessage(R.string.tvBluetoothDesactivado)
                                .setPositiveButton(R.string.cerrarAlertBoton) { button, _ -> button.dismiss() }
                                .show()
                    } else if (bleControllerViewModel.macAddress == "") {
                        AlertDialog.Builder(view.context).setTitle(R.string.alertDialogMissingMicrocontollerTitle)
                                .setMessage(R.string.alertDialogMissingMicrocontollerMessage)
                                .setPositiveButton(R.string.cerrarAlertBoton) { button, _ -> button.dismiss() }
                                .show()
                    } else {
                        ChangeSamplePressureDialog().show(requireActivity().supportFragmentManager.beginTransaction(), "Change sample altitude and  pressure dialog")

                    }

                    true
                }
                R.id.altitudeMenu -> {
                    ChangeNextAltitudeDialog().show(requireActivity().supportFragmentManager.beginTransaction(), "Change only next altitude  dialog")
                    true
                }
                else -> false
            }
        }
    }

    private fun getSwipeLeftCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as AnimalViewHolder).also {
                    disposables.add(animalDatabaseViewModel.deleteAnimal(it.idSimple))

                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.delete_icon)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                        .setActionIconTint(ContextCompat.getColor(requireContext(), R.color.colorQuintoPaleta))
                        .create()
                        .decorate()


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
    }

    private fun getSwipeRightCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as AnimalViewHolder).also {
                    it.cv.findNavController().navigate(AnimalDatabaseViewFragmentDirections.actionMainFragment2ToEditSightseen(it.idAdvance, it.idSimple))

                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.edit_icon)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorCuartoPaleta))
                        .setActionIconTint(ContextCompat.getColor(requireContext(), R.color.colorQuintoPaleta))
                        .create()
                        .decorate()


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
    }

}

