package com.example.muestreo_fauna_salvaje.fragments.resumen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muestreo_fauna_salvaje.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlineDB#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineDB extends Fragment {


    public OnlineDB() {
        // Required empty public constructor
    }


    public static OnlineDB newInstance(String param1, String param2) {
        OnlineDB fragment = new OnlineDB();
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
        return inflater.inflate(R.layout.fragment_online_d_b, container, false);
    }
}