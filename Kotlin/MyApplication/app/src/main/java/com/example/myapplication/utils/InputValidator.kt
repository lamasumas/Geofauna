package com.example.myapplication.utils

import android.widget.EditText
import java.lang.NumberFormatException

class InputValidator {

    fun nullOrEmpty(input: String?): String {
        if (input == null || input.equals("null"))
            return ""
        else
            return input
    }

    fun doubleOrNull(input: String): Double? {
        var temp: Double? = null
        try {
            temp = input.toDouble()
        } catch (e: NumberFormatException) {
            return null
        } finally {
            return temp
        }
    }

    fun isEditTextEmpty(editText: EditText):Boolean{
        return editText.text.isEmpty() || editText.text.isBlank()

    }
}