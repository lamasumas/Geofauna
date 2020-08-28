package com.example.myapplication.fragments.abstracts

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class GeneralFragmentRx : Fragment(){

    protected var disposables = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}