package com.foxy.currencyconverter.view_models

import androidx.lifecycle.ViewModel
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository

class CurrenciesViewModel(private val repository: ICurrenciesRepository) : ViewModel() {
}