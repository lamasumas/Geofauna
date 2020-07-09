package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.room.DatabaseRepository
import com.example.myapplication.room.data_classes.AvistamientoData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DatabaseViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_database_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val databaseRepository = DatabaseRepository(view.context)
        databaseRepository.retrieveSightsings().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    onNext -> generateTable(view, onNext)
                }

    }

    private fun generateTable(view:View, animals: List<AvistamientoData>) {
        val table = view.findViewById<TableLayout>(R.id.tableSeen);
        fun createTextView(text: String): TextView {
            var newTextView = TextView(context);
            newTextView.text = text;
            return newTextView;
        }

        animals.forEach { animal ->
            run {
                val newRow = TableRow(context);
                newRow.addView(createTextView(animal.especie));
                newRow.addView(createTextView(animal.latitude.toString()));
                newRow.addView(createTextView(animal.longitude.toString()));
                newRow.addView(createTextView(animal.date));
                newRow.addView(createTextView(animal.time));
                table.addView(newRow)
            }


        }
    }
}