package com.example.myapplication.database

import android.content.Context
import androidx.room.Room

object DatabaseInstance{
    private var db:AppDatabase ?= null;

    fun getInstance(context: Context):AppDatabase{
        if(db != null) return db!!;
        else{
            db = Room.databaseBuilder(context, AppDatabase::class.java, "animal-database").build()
            return db!!;
        }

    }
}