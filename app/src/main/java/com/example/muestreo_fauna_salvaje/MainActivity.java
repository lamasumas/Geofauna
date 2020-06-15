package com.example.muestreo_fauna_salvaje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.muestreo_fauna_salvaje.viewModels.LocationViewModel;

public class MainActivity extends AppCompatActivity {

    public LocationViewModel vwLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vwLocation = new ViewModelProvider(this).get(LocationViewModel.class);
        vwLocation.inicializar(this, this);
        setContentView(R.layout.activity_main);

    }
}