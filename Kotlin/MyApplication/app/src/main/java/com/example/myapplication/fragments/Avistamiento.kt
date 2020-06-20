package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.LocationManager
import com.example.myapplication.R
import io.reactivex.rxjava3.disposables.Disposable

class Avistamiento : Fragment() {

    var disposable:Disposable? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avistamiento, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

         disposable = LocationManager.getLocationObservable(view.context)?.subscribe{
                onNext->  run  {
            view.findViewById<TextView>(R.id.etLatitud).text = onNext.latitude.toString();
            view.findViewById<TextView>(R.id.etLongitud).text = onNext.longitude.toString();
        }
        }

    }

    override fun onDestroy() {

        disposable?.dispose();
        super.onDestroy();

    }
}