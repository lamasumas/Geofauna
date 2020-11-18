package com.example.myapplication.utils

import android.widget.EditText
import java.lang.NumberFormatException

class InputValidator {
    /**
     * Comprueba si una string es null y si lo es te devuelve una string vacio
     * si no lo es te devuelve la string
     * @param input, string a comprobar
     * @return string vacia si era null la que habia comprobar o la recibidia
     */
    fun nullOrEmpty(input: String?): String {
        if (input == null || input.equals("null"))
            return ""
        else
            return input
    }

    /**
     * Comprueba si una string tiene almacenado un double en él
     * @param input, string con el supuesto double
     * @return double que habia almacenado en la string
     */
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

    /**
     * Comprueba si un edittext esta vacio o con una string con solo espacios
     * @param editText, edittext a comprobar
     * @return boolean, true si esta vacio, false si no.
     */
    fun isEditTextEmpty(editText: EditText): Boolean {
        return editText.text.isEmpty() || editText.text.isBlank()

    }
}