package com.foxy.currencyconverter.util

import androidx.fragment.app.Fragment
import com.foxy.currencyconverter.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = InjectorUtil.getCurrenciesRepository()
    return ViewModelFactory(repository, this)
}