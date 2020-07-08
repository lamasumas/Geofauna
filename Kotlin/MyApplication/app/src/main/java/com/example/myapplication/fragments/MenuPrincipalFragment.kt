package com.example.myapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.room.data_classes.AvistamientoData
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.viewmodels.DatabaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MenuPrincipalFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vmDatabase = ViewModelProviders.of(this).get(DatabaseViewModel::class.java);
        val databaseRepository = DatabaseRepository(view.context)
        databaseRepository.retrieveSightsings()/*.subscribe{
            onNext -> generateTable(view, onNext)
        }*/
    }

    private fun generateTable(view:View, animal: AvistamientoData) {
        val table = view.findViewById<TableLayout>(R.id.tableSeen);
        fun createTextView(text:String):TextView{
            var newTextView = TextView(context);
            newTextView.text = text;
            return  newTextView;
        }

            val newRow = TableRow(context);
            newRow.addView(createTextView(animal.especie));
            newRow.addView(createTextView(animal.latitude.toString()));
            newRow.addView(createTextView(animal.longitude.toString()));
            newRow.addView(createTextView(animal.date));
            newRow.addView(createTextView(animal.time));
        table.addView(newRow)



    }


}