package com.example.myapplication

import android.text.Editable
import android.widget.EditText
import com.example.myapplication.utils.InputValidator
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class InputValidatorTest {
    private val validator= InputValidator()
    @Test
    fun doubleOrNullTest() {
        var testValue: String = "2.5"

        assertEquals(2.5, validator.doubleOrNull(testValue))

        testValue = ""
        assertEquals(null, validator.doubleOrNull(testValue))

        testValue = "a"
        assertEquals(null, validator.doubleOrNull(testValue))
    }


    @Test
    fun nullOrEmptyTest(){
        var testValue:String? = null
        assertEquals("", validator.nullOrEmpty(testValue))

        testValue = "test"
        assertEquals("test", validator.nullOrEmpty(testValue))

        testValue = "null"
        assertEquals("", validator.nullOrEmpty(testValue))
    }

    @Test
    fun isEditTextEmptyTest(){
        val editText = Mockito.mock(EditText::class.java)
        val x = Mockito.mock(Editable::class.java)

        Mockito.`when`(x.length).thenReturn(0)
        Mockito.`when`(editText.text).thenReturn(x)

        assertEquals(true, validator.isEditTextEmpty(editText))
        Mockito.`when`(x.length).thenReturn(1)

        assertEquals(false, validator.isEditTextEmpty(editText))
    }

}