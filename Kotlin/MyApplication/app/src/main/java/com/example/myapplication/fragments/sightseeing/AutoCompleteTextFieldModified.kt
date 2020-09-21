package com.example.myapplication.fragments.sightseeing

import android.R
import android.content.Context
import android.widget.ArrayAdapter

class AutoCompleteTextFieldModified(context: Context) : androidx.appcompat.widget.AppCompatAutoCompleteTextView(context) {

    fun setup(animalList: MutableList<String>) {
        ArrayAdapter<String>(this.context, R.layout.simple_list_item_1, animalList).also {
            this.setAdapter(it)
        }
    }

    override fun enoughToFilter(): Boolean {
        return true
    }
}