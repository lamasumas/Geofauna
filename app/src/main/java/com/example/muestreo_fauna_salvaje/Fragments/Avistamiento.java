package com.example.muestreo_fauna_salvaje.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.muestreo_fauna_salvaje.R;
import com.example.muestreo_fauna_salvaje.ViewModels.AvistamientoViewModel;

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

        Location posicionGPS = getLocation();
        View vwAvistamiento = inflater.inflate(R.layout.fragment_avistamiento, container, false);

        ((EditText)vwAvistamiento.findViewById(R.id.etLatitud)).setText(String.valueOf(posicionGPS.getLatitude()));
        ((EditText)vwAvistamiento.findViewById(R.id.etLongitud)).setText(String.valueOf(posicionGPS.getLongitude()));

        return vwAvistamiento;
    }
    private Location getLocation(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
         Location posicionGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         

        return posicionGPS;
    }
}