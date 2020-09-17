package com.example.myapplication.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.fragments.animals_database.AnimalsDatabaseViewFragment

class MainAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 1;
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return AnimalsDatabaseViewFragment();
            else -> return AnimalsDatabaseViewFragment();
        }
    }

}