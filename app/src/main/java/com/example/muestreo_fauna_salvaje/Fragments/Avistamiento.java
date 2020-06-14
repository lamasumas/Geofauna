package com.example.muestreo_fauna_salvaje.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.muestreo_fauna_salvaje.R;

import java.io.IOException;
import java.util.List;

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

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> test;
        try {
            test = geocoder.getFromLocation(posicionGPS.getLatitude(), posicionGPS.getLongitude(),3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vwAvistamiento;
    }
    private Location getLocation(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        Location posicionGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        boolean encendido = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!encendido)
        {
            Intent intentEncender = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getActivity().startActivity(intentEncender);
            return posicionGPS;
        }
        return posicionGPS;
    }
}