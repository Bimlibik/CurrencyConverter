package com.foxy.currencyconverter.view_models

import androidx.lifecycle.*
import com.foxy.currencyconverter.R
import com.foxy.currencyconverter.data.Result
import com.foxy.currencyconverter.data.Result.Success
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository
import kotlinx.coroutines.launch

class CurrenciesViewModel(private val repository: ICurrenciesRepository) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

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

    private val _isCollapse = MutableLiveData(false)
    val isCollapse: LiveData<Boolean> = _isCollapse

    private val _collapseIconRes = MutableLiveData(R.drawable.ic_collapse)
    val collapseIconRes: LiveData<Int> = _collapseIconRes

    private val _collapseLabel = MutableLiveData(R.string.collapse)
    val collapseLabel: LiveData<Int> = _collapseLabel


    init {
        loadCurrencies(true)
    }

    fun refresh() {
        loadCurrencies(true)
    }

    fun showHideConverterPanel(isShown: Boolean) {
        _isCollapse.value = isShown
        changeCollapseInfo(isShown)
    }

    private fun changeCollapseInfo(isCollapse: Boolean) {
        if (isCollapse) {
            _collapseIconRes.value = R.drawable.ic_expand
            _collapseLabel.value = R.string.expand
        } else {
            _collapseIconRes.value = R.drawable.ic_collapse
            _collapseLabel.value = R.string.collapse
        }
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
        return result
    }
}