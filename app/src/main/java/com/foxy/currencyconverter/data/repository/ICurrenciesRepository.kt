package com.foxy.currencyconverter.data.repository

import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.data.model.Currency
import com.foxy.currencyconverter.data.Result

interface ICurrenciesRepository {

    fun observeCurrencies(): LiveData<Result<List<Currency>>>

    suspend fun refreshCurrencies(forceUpdate: Boolean, callback: (isSuccess: Boolean) -> Unit)

    suspend fun updateSelected(currencyId: String, isSelected: Boolean)
}