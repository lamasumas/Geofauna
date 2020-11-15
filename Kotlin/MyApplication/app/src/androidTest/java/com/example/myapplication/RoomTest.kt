package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.room.AppDatabase
import com.example.myapplication.room.AvistamientoDAO
import com.example.myapplication.room.data_classes.Transect
import org.junit.After
import org.junit.Before
import org.junit.Test

class RoomTest{
    private lateinit var db:AppDatabase
    private lateinit var dao:AvistamientoDAO

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries() //only for testing
                .build()
        dao  = db.avistamientoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testInsertTransect(){
        var transect = Transect(0,"e","e","e","e","e",null, null)
        dao.insertAnimal(transect).subscribe { _ ->Log.d("test","test") }
        dao.getAllTransect().test().assertValue{
                it.size ==1
        }
    }
}