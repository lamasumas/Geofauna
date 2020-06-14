package com.example.muestreo_fauna_salvaje.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.muestreo_fauna_salvaje.MainActivity;
import com.example.muestreo_fauna_salvaje.R;
import com.example.muestreo_fauna_salvaje.ViewModels.LocationViewModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Avistamiento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Avistamiento extends Fragment {


    public Avistamiento() {
        // Required empty public constructor
    }


    public static Avistamiento newInstance() {
        Avistamiento fragment = new Avistamiento();
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

        View vAvistamiento = inflater.inflate(R.layout.fragment_avistamiento, container, false);
        setAutomaticInfo(vAvistamiento);

        return vAvistamiento;
    }

    private void setAutomaticInfo(View vAvistamiento) {
        LocationViewModel vwLocation = ((MainActivity) getActivity()).vwLocation;
        ((EditText)vAvistamiento.findViewById(R.id.etLatitud)).setText(String.valueOf(vwLocation.getLatitude()));
        ((EditText)vAvistamiento.findViewById(R.id.etLongitud)).setText(String.valueOf(vwLocation.getLongitude()));
        ((EditText)vAvistamiento.findViewById(R.id.etLugar)).setText(vwLocation.getLugar());
        ((EditText)vAvistamiento.findViewById(R.id.etPais)).setText(vwLocation.getPais());

        Calendar calendario = new GregorianCalendar();
        String hora = calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND);
        ((EditText) vAvistamiento.findViewById(R.id.etHora)).setText(hora);


    }

}