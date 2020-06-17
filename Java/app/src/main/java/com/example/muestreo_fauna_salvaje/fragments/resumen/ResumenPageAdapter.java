package com.example.muestreo_fauna_salvaje.fragments.resumen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ResumenPageAdapter extends FragmentStateAdapter {


    public ResumenPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OfflineDB();
            case 1:
                return new OnlineDB();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
