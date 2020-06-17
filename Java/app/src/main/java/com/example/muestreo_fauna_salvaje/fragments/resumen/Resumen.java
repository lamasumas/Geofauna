package com.example.muestreo_fauna_salvaje.fragments.resumen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muestreo_fauna_salvaje.R;
import com.example.muestreo_fauna_salvaje.MainActivityPageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Resumen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Resumen extends Fragment {

    public Resumen() {
        // Required empty public constructor
    }


    public static Resumen newInstance(String param1, String param2) {
        Resumen fragment = new Resumen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ResumenPageAdapter mainActivityPageAdapter = new ResumenPageAdapter(getChildFragmentManager(), getLifecycle());
        ViewPager2 viewPager2 = view.findViewById(R.id.vpPager);
        viewPager2.setAdapter(mainActivityPageAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resumen, container, false);
    }


}