package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.export.ExportManager
import com.example.myapplication.adapters.MainAdapter
import com.example.myapplication.R
import com.example.myapplication.bluetooth.dialog.BluetoothScanDialog
import com.example.myapplication.export.dialog.ExportDialog
import com.example.myapplication.fragments.abstracts.GeneralFragmentRx
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.clicks

class MainDatabaseViewFragment : GeneralFragmentRx() {

    private var viewPager2: ViewPager2? = null

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
        val idTransect =  arguments?.getLong("transectId") as Long

        disposables.add(view.findViewById<FloatingActionButton>(R.id.fab).clicks()
                .subscribe {
                    val navController = view.findNavController()
                    if (navController.currentDestination?.id == R.id.mainFragment2)
                        navController.navigate(MainDatabaseViewFragmentDirections.actionMainFragment2ToAvistamiento2( idTransect))
                })
        viewPager2 = view.findViewById<ViewPager2>(R.id.vpMain)
        viewPager2?.adapter = MainAdapter(this, idTransect)

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
        viewPager2?.adapter = null
    }

}
