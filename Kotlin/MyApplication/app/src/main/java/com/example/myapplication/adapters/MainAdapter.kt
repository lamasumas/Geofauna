package com.example.myapplication.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.fragments.animals_database.AnimalsDatabaseViewFragment

class MainAdapter(fragment: Fragment, private val transectId:Long ) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 1;
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return AnimalsDatabaseViewFragment(transectId);
            else -> return AnimalsDatabaseViewFragment(transectId);
        }
    }

}