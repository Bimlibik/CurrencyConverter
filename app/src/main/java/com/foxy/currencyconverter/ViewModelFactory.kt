package com.foxy.currencyconverter

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.foxy.currencyconverter.data.repository.ICurrenciesRepository
import com.foxy.currencyconverter.ui.currencies.CurrenciesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val currenciesRepository: ICurrenciesRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(CurrenciesViewModel::class.java) ->
                CurrenciesViewModel(currenciesRepository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}