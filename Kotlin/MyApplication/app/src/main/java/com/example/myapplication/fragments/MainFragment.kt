package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.adapters.MainAdapter
import com.example.myapplication.R
import com.example.myapplication.bluetooth.BluetoothManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MainFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        compositeDisposable.add(menu.findItem(R.id.bluetoothMenu).clicks().subscribe {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Lista bluetooth")
            BluetoothManager.scanDevices(requireContext()).toList().subscribe { onNext ->
                builder.setItems(onNext.map { it.bleDevice.name }.toTypedArray()){
                    when(it){

                    }
                }

            }

            builder.setMessage(R.string.bluetoothDenegadoDescripcion)
            builder.setPositiveButton(R.string.cerrarAlertBoton) { dialog, _ ->
                dialog.dismiss()
            };
            builder.create().show()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable.add(view.findViewById<FloatingActionButton>(R.id.fab).clicks()
                .subscribe {
                    val navController = view.findNavController()
                    if(navController.currentDestination?.id == R.id.mainFragment2)
                        navController.navigate( MainFragmentDirections.actionMainFragment2ToAvistamiento2())
                })
        val viewpager = view.findViewById<ViewPager2>(R.id.vpMain)
        viewpager.adapter = MainAdapter(this);




    }

}

