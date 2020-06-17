package com.example.muestreo_fauna_salvaje.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.muestreo_fauna_salvaje.R;
import com.example.muestreo_fauna_salvaje.viewModels.LocationViewModel;

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
        iniciarServicioLocalizacion();
        return  inflater.inflate(R.layout.fragment_menu_principal, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnAñadir = (Button)view.findViewById(R.id.btnAñadir);
        btnAñadir.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_menuPrincipal_to_avistamiento);
            }
        });
        ((Button) view.findViewById(R.id.btnResumen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_menuPrincipal_to_resumen);
            }
        });
    }

    private void iniciarServicioLocalizacion() {
        LocationViewModel vwLocation = new ViewModelProvider(this).get(LocationViewModel.class);
        vwLocation.inicializar(getContext(),getActivity());
    }

}