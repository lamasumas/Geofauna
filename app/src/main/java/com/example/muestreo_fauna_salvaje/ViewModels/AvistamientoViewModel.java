package com.example.muestreo_fauna_salvaje.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AvistamientoViewModel extends ViewModel {
    private MutableLiveData<String> especie;

    public LiveData<String> getEspecie(){
        if(especie == null){
            especie = new MutableLiveData<String>();
        }
        return especie;
    }
}
