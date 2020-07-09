package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.fragments.DatabaseViewFragment
import com.example.myapplication.fragments.MenuPrincipalFragment

class MainAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return MenuPrincipalFragment();
            1 -> return DatabaseViewFragment();
            else -> return MenuPrincipalFragment();
        }
    }

}