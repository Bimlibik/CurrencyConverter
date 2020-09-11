package com.foxy.currencyconverter.view_models

import android.util.Log
import androidx.lifecycle.*
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.*
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository
import kotlinx.coroutines.launch

class CurrenciesViewModel(private val repository: ICurrenciesRepository) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _currencies: LiveData<List<Currency>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            viewModelScope.launch {
                repository.refreshCurrencies()
            }
        }
        repository.observeCurrencies().distinctUntilChanged().switchMap { computeResult(it) }
    }

    val currencies: LiveData<List<Currency>> = _currencies

    val empty: LiveData<Boolean> = Transformations.map(_currencies) {
        it.isEmpty()
    }


    init {
        loadCurrencies(true)
    }

    fun refresh() {
        loadCurrencies(true)
    }

    private fun loadCurrencies(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun computeResult(currenciesResult: Result<List<Currency>>): LiveData<List<Currency>> {
        val result = MutableLiveData<List<Currency>>()

        if (currenciesResult is Success) {
            result.value = currenciesResult.data
        } else {
            result.value = emptyList()
        }
        Log.i("TAG2", "computeResult: ${result.value}")
        return result
    }
}