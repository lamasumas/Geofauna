package com.example.muestreo_fauna_salvaje.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.muestreo_fauna_salvaje.MainActivity;
import com.example.muestreo_fauna_salvaje.R;
import com.example.muestreo_fauna_salvaje.databases.DBAnimalesLocal;
import com.example.muestreo_fauna_salvaje.viewModels.LocationViewModel;

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

        final View vAvistamiento = inflater.inflate(R.layout.fragment_avistamiento, container, false);
        setAutomaticInfo(vAvistamiento);
        ((Button)vAvistamiento.findViewById(R.id.btnAutocompletar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAutomaticInfo(vAvistamiento);
            }
        });
        ((Button) vAvistamiento.findViewById(R.id.btnAñadirAvistamiento)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAnimalesLocal dbAnimalesLocal = new DBAnimalesLocal(getContext());

            }
        });
        return vAvistamiento;
    }

    private void setAutomaticInfo(View vAvistamiento) {
        LocationViewModel vwLocation = ((MainActivity) getActivity()).vwLocation;
        try {
            ((EditText) vAvistamiento.findViewById(R.id.etLatitud)).setText(String.valueOf(vwLocation.getLatitude()));
            ((EditText) vAvistamiento.findViewById(R.id.etLongitud)).setText(String.valueOf(vwLocation.getLongitude()));
            ((EditText) vAvistamiento.findViewById(R.id.etLugar)).setText(vwLocation.getLugar());
            ((EditText) vAvistamiento.findViewById(R.id.etPais)).setText(vwLocation.getPais());
        }catch (NullPointerException e){
            AlertDialog.Builder alertaGPS = new AlertDialog.Builder(getActivity());
            alertaGPS.setTitle(R.string.alertaGPSTitulo)
                    .setMessage(R.string.alertaGPSMensaje)
                    .setPositiveButton(R.string.alertaGPSBoton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            alertaGPS.create().show();
        }
        Calendar calendario = new GregorianCalendar();
        ((EditText) vAvistamiento.findViewById(R.id.etHora)).setText(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        ((EditText) vAvistamiento.findViewById(R.id.etMinuto)).setText(String.valueOf(calendario.get(Calendar.MINUTE)));
        ((EditText) vAvistamiento.findViewById(R.id.etDia)).setText(String.valueOf(calendario.get(Calendar.DATE)));
        ((EditText) vAvistamiento.findViewById(R.id.etMes)).setText(String.valueOf(calendario.get(Calendar.MONTH)));
        ((EditText) vAvistamiento.findViewById(R.id.etAño)).setText(String.valueOf(calendario.get(Calendar.YEAR)));


    }

}