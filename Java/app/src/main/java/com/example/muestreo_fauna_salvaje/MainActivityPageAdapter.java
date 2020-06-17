package com.example.muestreo_fauna_salvaje;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.muestreo_fauna_salvaje.fragments.Avistamiento;
import com.example.muestreo_fauna_salvaje.fragments.resumen.OfflineDB;
import com.example.muestreo_fauna_salvaje.fragments.resumen.OnlineDB;

public class MainActivityPageAdapter extends FragmentStateAdapter {


    public MainActivityPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Avistamiento();
            case 1:
                return new OfflineDB();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
