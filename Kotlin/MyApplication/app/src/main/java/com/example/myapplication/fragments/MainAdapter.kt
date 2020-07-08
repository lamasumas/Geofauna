package com.example.myapplication.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 1;
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return MenuPrincipalFragment();
            else -> return MenuPrincipalFragment();
        }
    }

}