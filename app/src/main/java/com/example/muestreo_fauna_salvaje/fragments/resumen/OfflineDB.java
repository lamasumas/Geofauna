package com.example.muestreo_fauna_salvaje.fragments.resumen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muestreo_fauna_salvaje.R;

public class OfflineDB extends Fragment {


    public OfflineDB() {
        // Required empty public constructor
    }

    public static OfflineDB newInstance(String param1, String param2) {
        OfflineDB fragment = new OfflineDB();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline_d_b, container, false);
    }
}