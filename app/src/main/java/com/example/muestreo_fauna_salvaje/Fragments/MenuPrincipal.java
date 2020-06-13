package com.example.muestreo_fauna_salvaje.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.muestreo_fauna_salvaje.R;

public class MenuPrincipal extends Fragment {



    public MenuPrincipal() {
        // Required empty public constructor
    }

    public static MenuPrincipal newInstance() {
        MenuPrincipal fragment = new MenuPrincipal();
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

        View theView = inflater.inflate(R.layout.fragment_menu_principal, container, false);
        Button btnAñadir = (Button)theView.findViewById(R.id.btnAñadir);
        btnAñadir.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_menuPrincipal_to_avistamiento);
            }
        });

        return theView;
    }

}